package io.nuun.kernel.core.internal;

import com.google.common.base.Strings;
import com.google.inject.Module;
import com.google.inject.Scopes;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.annotations.KernelModule;
import io.nuun.kernel.api.config.KernelOptions;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.RequestType;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.injection.ModuleEmbedded;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner;
import io.nuun.kernel.core.internal.scanner.ClasspathScannerFactory;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathStrategy;
import io.nuun.kernel.core.internal.utils.NuunReflectionUtils;
import org.kametic.specifications.Specification;
import org.kametic.specifications.reflect.DescendantOfSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

import static io.nuun.kernel.api.config.KernelOptions.CLASSPATH_SCAN_MODE;
import static io.nuun.kernel.api.config.KernelOptions.PRINT_SCAN_WARN;
import static java.util.Collections.unmodifiableMap;

/**
 * @author Pierre THIROUIN (pierre.thirouin@ext.inetpsa.com)
 */
public class RequestHandler extends ScanResults
{
    private static final String SCAN_WHOLE_CLASSPATH_WARN_MESSAGE = "\n================================ WARNING ================================\n" +
            "   You're actually scanning the WHOLE classpath , this can be time consuming.\n" +
            "   Please update your application configuration, to narrow the scan to your application.\n" +
            "\n" +
            " 1) You can update /nuun.conf in the classpath with the following content\n" +
            "\n" +
            "          rootPackage = com.acme1, com.acme2\n" +
            "\n" +
            "   where com.acme1 and com.acme2 are your root packages.\n" +
            "\n" +
            " 2) You can programmatically use KernelConfiguration.rootPackage(\"com.acme1\" , \"com.acme2\")\n" +
            "\n" +
            "   Same as above\n" +
            "\n" +
            " 3) You can programmatically use KernelConfiguration.param(\"scan.warn.disable\" , \"true\")\n" +
            "\n" +
            "   This will disable the warning. It implies, you know what you are doing.\n" +
            "\n" +
            "========================================================================";

    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final List<String> propertiesPrefix = new ArrayList<String>();
    private final List<Class<?>> parentTypesClassesToScan = new ArrayList<Class<?>>();
    private final List<Class<?>> ancestorTypesClassesToScan = new ArrayList<Class<?>>();
    private final List<Specification<Class<?>>> specificationsToScan = new ArrayList<Specification<Class<?>>>();
    private final List<String> typesRegexToScan = new ArrayList<String>();
    private final List<String> resourcesRegexToScan = new ArrayList<String>();
    private final List<String> parentTypesRegexToScan = new ArrayList<String>();
    private final List<Class<? extends Annotation>> annotationTypesToScan = new ArrayList<Class<? extends Annotation>>();
    private final List<String> annotationRegexToScan = new ArrayList<String>();

    private final List<Class<?>> parentTypesClassesToBind = new ArrayList<Class<?>>();
    private final List<Class<?>> ancestorTypesClassesToBind = new ArrayList<Class<?>>();
    private final List<Specification<Class<?>>> specificationsToBind = new ArrayList<Specification<Class<?>>>();
    private final List<String> parentTypesRegexToBind = new ArrayList<String>();
    private final List<Class<? extends Annotation>> annotationTypesToBind = new ArrayList<Class<? extends Annotation>>();
    private final List<Class<? extends Annotation>> metaAnnotationTypesToBind = new ArrayList<Class<? extends Annotation>>();
    private final List<String> annotationRegexToBind = new ArrayList<String>();
    private final List<String> metaAnnotationRegexToBind = new ArrayList<String>();

    private final Map<Class<?>, Object> classesWithScopes = new HashMap<Class<?>, Object>();
    private final Map<Key, Object> mapOfScopes = new HashMap<Key, Object>();

    private final List<String> packageRoots;

    private Set<URL> additionalClasspathScan;
    private ClasspathStrategy classpathStrategy;
    private ClasspathScanner classpathScanner;
    private KernelOptions options;

    public RequestHandler(Map<String, String> kernelParams, KernelOptions options)
    {
        setClasspathStrategy(kernelParams);

        this.packageRoots = new LinkedList<String>();
        this.propertiesPrefix.add(Kernel.NUUN_PROPERTIES_PREFIX);
        this.additionalClasspathScan = new HashSet<URL>();
        this.options = options;
    }

    private void setClasspathStrategy(Map<String, String> kernelParams)
    {
        String classpathStrategyName = kernelParams.get(KernelCore.NUUN_CP_STRATEGY_NAME);
        String classpathStrategyAdditionalParam = kernelParams.get(KernelCore.NUUN_CP_STRATEGY_ADD);
        boolean additional = classpathStrategyAdditionalParam == null || Boolean.parseBoolean(classpathStrategyAdditionalParam);
        classpathStrategy = new ClasspathStrategy(classpathStrategyName, additional);
    }

    public void registerRequests(List<Plugin> plugins)
    {
        for (Plugin plugin : plugins)
        {
            registerClasspathRequests(plugin);
            registerBindingRequests(plugin);
            registerPropertyPrefix(plugin);
        }
    }

    private void registerPropertyPrefix(Plugin plugin)
    {
        String pluginPropertiesPrefix = plugin.pluginPropertiesPrefix();
        if (!Strings.isNullOrEmpty(pluginPropertiesPrefix))
        {
            addPropertiesPrefix(pluginPropertiesPrefix);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerClasspathRequests(Plugin plugin)
    {
        Collection<ClasspathScanRequest> classpathScanRequests = plugin.classpathScanRequests();
        if (classpathScanRequests != null && classpathScanRequests.size() > 0)
        {
            for (ClasspathScanRequest request : classpathScanRequests)
            {
                switch (request.requestType)
                {
                    case ANNOTATION_TYPE:
                        addAnnotationTypesToScan((Class<? extends Annotation>) request.objectRequested);
                        break;
                    case ANNOTATION_REGEX_MATCH:
                        addAnnotationRegexesToScan((String) request.objectRequested);
                        break;
                    case SUBTYPE_OF_BY_CLASS:
                        addParentTypeClassToScan((Class<?>) request.objectRequested);
                        break;
                    case SUBTYPE_OF_BY_TYPE_DEEP:
                        addAncestorTypeClassToScan((Class<?>) request.objectRequested);
                        break;
                    case SUBTYPE_OF_BY_REGEX_MATCH:
                        addParentTypeRegexesToScan((String) request.objectRequested);
                        break;
                    case RESOURCES_REGEX_MATCH:
                        addResourcesRegexToScan((String) request.objectRequested);
                        break;
                    case TYPE_OF_BY_REGEX_MATCH:
                        addTypeRegexesToScan((String) request.objectRequested);
                        break;
                    case VIA_SPECIFICATION:
                        addSpecificationToScan(request.specification);
                        break;
                    default:
                        logger.warn("{} is not a ClasspathScanRequestType a o_O", request.requestType);
                        break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void registerBindingRequests(Plugin plugin)
    {
        Collection<BindingRequest> bindingRequests = plugin.bindingRequests();
        if (bindingRequests != null && bindingRequests.size() > 0)
        {
            for (BindingRequest request : bindingRequests)
            {
                switch (request.requestType)
                {
                    case ANNOTATION_TYPE:
                        addAnnotationTypesToBind((Class<? extends Annotation>) request.requestedObject, request.requestedScope);
                        break;
                    case ANNOTATION_REGEX_MATCH:
                        addAnnotationRegexesToBind((String) request.requestedObject, request.requestedScope);
                        break;
                    case META_ANNOTATION_TYPE:
                        addMetaAnnotationTypesToBind((Class<? extends Annotation>) request.requestedObject, request.requestedScope);
                        break;
                    case META_ANNOTATION_REGEX_MATCH:
                        addMetaAnnotationRegexesToBind((String) request.requestedObject, request.requestedScope);
                        break;
                    case SUBTYPE_OF_BY_CLASS:
                        addParentTypeClassToBind((Class<?>) request.requestedObject, request.requestedScope);
                        break;
                    case SUBTYPE_OF_BY_TYPE_DEEP:
                        addAncestorTypeClassToBind((Class<?>) request.requestedObject, request.requestedScope);
                        break;
                    case SUBTYPE_OF_BY_REGEX_MATCH:
                        addTypeRegexesToBind((String) request.requestedObject, request.requestedScope);
                        break;
                    case VIA_SPECIFICATION:
                        addSpecificationToBind(request.specification, request.requestedScope);
                        break;
                    default:
                        logger.warn("{} is not a BindingRequestType o_O !", request.requestType);
                        break;
                }
            }
        }
    }

    public void executeRequests()
    {
        initScanner();

        scanKernelModules();
        scanClasses();
        scanClassesToBind();
        scanResources();
        scanPropertyFiles();
    }

    private void initScanner()
    {
        printWarnWhenScanningAllClasspath();
        ClasspathScannerFactory classpathScannerFactory = new ClasspathScannerFactory(options.get(CLASSPATH_SCAN_MODE));
        classpathScanner = classpathScannerFactory.create(classpathStrategy, additionalClasspathScan, packageRoots);
    }

    private void printWarnWhenScanningAllClasspath()
    {
        if (packageRoots.isEmpty() && options.get(PRINT_SCAN_WARN))
        {
            logger.warn(SCAN_WHOLE_CLASSPATH_WARN_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void scanKernelModules()
    {
        Collection<Class<? extends Module>> scanResult = (Collection) classpathScanner.scanTypesAnnotatedBy(KernelModule.class);

        for (Class<? extends Module> aClass : scanResult)
        {
            if (isNotAbstract(aClass))
            {
                final Object module = NuunReflectionUtils.instantiateSilently(aClass);
                ModuleEmbedded moduleEmbedded = module != null ? new ModuleEmbedded(module) : null;

                if (isOverridingModule(aClass))
                {
                    addChildOverridingModule(moduleEmbedded);
                } else
                {
                    addChildModule(moduleEmbedded);
                }
            }
        }
    }

    private boolean isOverridingModule(Class<? extends Module> aClass)
    {
        KernelModule annotation = aClass.getAnnotation(KernelModule.class);
        return annotation != null && annotation.overriding();
    }

    private boolean isNotAbstract(Class<?> aClass)
    {
        return !Modifier.isAbstract(aClass.getModifiers());
    }

    private void scanClasses()
    {
        for (final Class<?> parentType : parentTypesClassesToScan)
        {
            super.addSubtypes(parentType, classpathScanner.scanSubTypesOf(parentType));
        }

        for (final Class<?> parentType : ancestorTypesClassesToScan)
        {
            super.addAncestorTypes(parentType, classpathScanner.scanTypes(new DescendantOfSpecification(parentType)));
        }

        for (final String typeName : parentTypesRegexToScan)
        {
            super.addSubTypesByName(typeName, classpathScanner.scanSubTypesOf(typeName));
        }

        for (final String typeName : typesRegexToScan)
        {
            super.addTypesByName(typeName, classpathScanner.scanTypes(typeName));
        }

        for (final Specification<Class<?>> spec : specificationsToScan)
        {
            super.addTypesBySpecification(spec, classpathScanner.scanTypes(spec));
        }

        for (final Class<? extends Annotation> annotationType : annotationTypesToScan)
        {
            super.addAnnotationTypes(annotationType, classpathScanner.scanTypesAnnotatedBy(annotationType));
        }

        for (final String annotationName : annotationRegexToScan)
        {
            super.addAnnotationTypesByName(annotationName, classpathScanner.scanTypesAnnotatedBy(annotationName));
        }
    }

    private void scanClassesToBind()
    {
        for (final Class<?> parentType : parentTypesClassesToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanSubTypesOf(parentType);
            RequestType requestType = RequestType.SUBTYPE_OF_BY_CLASS;
            addScopeToClasses(scanResult, scope(requestType, parentType), classesWithScopes);

            addClassesToBind(scanResult);
        }

        for (final Class<?> ancestorType : ancestorTypesClassesToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanTypes(new DescendantOfSpecification(ancestorType));
            RequestType requestType = RequestType.SUBTYPE_OF_BY_TYPE_DEEP;
            addScopeToClasses(scanResult, scope(requestType, ancestorType), classesWithScopes);
            addClassesToBind(scanResult);
        }

        // TODO vérifier si ok parent types vs type. si ok changer de nom
        for (final String typeName : parentTypesRegexToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanTypes(typeName);
            RequestType requestType = RequestType.SUBTYPE_OF_BY_REGEX_MATCH;
            addScopeToClasses(scanResult, scope(requestType, typeName), classesWithScopes);
            addClassesToBind(scanResult);
        }

        for (final Specification<Class<?>> spec : specificationsToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanTypes(spec);
            RequestType requestType = RequestType.VIA_SPECIFICATION;
            addScopeToClasses(scanResult, scope(requestType, spec), classesWithScopes);
            addClassesToBind(scanResult);
        }

        for (final Class<? extends Annotation> annotationType : annotationTypesToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanTypesAnnotatedBy(annotationType);
            RequestType requestType = RequestType.ANNOTATION_TYPE;
            addScopeToClasses(scanResult, scope(requestType, annotationType), classesWithScopes);
            addClassesToBind(scanResult);
        }

        for (final String annotationNameRegex : annotationRegexToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanTypesAnnotatedBy(annotationNameRegex);
            RequestType requestType = RequestType.ANNOTATION_REGEX_MATCH;
            addScopeToClasses(scanResult, scope(requestType, annotationNameRegex), classesWithScopes);
            addClassesToBind(scanResult);
        }

        for (final Class<? extends Annotation> metaAnnotationType : metaAnnotationTypesToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanTypesMetaAnnotated(metaAnnotationType);
            RequestType requestType = RequestType.META_ANNOTATION_TYPE;
            addScopeToClasses(scanResult, scope(requestType, metaAnnotationType), classesWithScopes);
            addClassesToBind(scanResult);
        }

        for (final String metaAnnotationNameRegex : metaAnnotationRegexToBind)
        {
            final Collection<Class<?>> scanResult = classpathScanner.scanTypesMetaAnnotated(metaAnnotationNameRegex);
            RequestType requestType = RequestType.META_ANNOTATION_REGEX_MATCH;
            addScopeToClasses(scanResult, scope(requestType, metaAnnotationNameRegex), classesWithScopes);
            addClassesToBind(scanResult);
        }
    }

    private Object scope(RequestType requestType, Object criteria)
    {
        Object scope = mapOfScopes.get(key(requestType, criteria));
        if (null == scope)
        {
            scope = Scopes.NO_SCOPE;
        }
        return scope;
    }

    private void addScopeToClasses(Collection<Class<?>> classes, Object scope, Map<Class<?>, Object> inClassesWithScopes)
    {
        for (Class<?> aClass : classes)
        {
            if (!inClassesWithScopes.containsKey(aClass) && scope != null)
            {
                inClassesWithScopes.put(aClass, scope);
            } else
            {
                Object insideScope = inClassesWithScopes.get(aClass);
                if (!insideScope.equals(scope))
                {
                    String format = String.format("Class %s is already associated with scope %s but  %s", aClass.getName(), insideScope, scope);
                    logger.error(format);
                    throw new KernelException(format);
                }
            }
        }
    }

    private void scanResources()
    {
        for (final String regex : resourcesRegexToScan)
        {
            super.addResourcesByRegex(regex, classpathScanner.scanResources(regex));
        }
    }

    private void scanPropertyFiles()
    {
        super.addPropertyFiles(classpathScanner.scanResources(".*\\.properties"));

        for (final String prefix : propertiesPrefix)
        {
            super.addPropertyFilesByPrefix(prefix, classpathScanner.scanResources(prefix + ".*\\.properties"));
        }
    }

    public void addClasspathToScan(URL path)
    {
        if (path != null)
        {
            additionalClasspathScan.add(path);
        }
    }

    public void addPropertiesPrefix(String prefix)
    {
        propertiesPrefix.add(prefix);
    }

    public void addRootPackage(String root)
    {
        packageRoots.add(root);
    }

    public void addParentTypeClassToScan(Class<?> type)
    {
        parentTypesClassesToScan.add(type);
    }

    public void addAncestorTypeClassToScan(Class<?> type)
    {
        ancestorTypesClassesToScan.add(type);
    }

    public void addResourcesRegexToScan(String regex)
    {
        resourcesRegexToScan.add(regex);
    }

    private Key key(RequestType type, Object key)
    {
        return new Key(type, key);
    }

    public void addParentTypeClassToBind(Class<?> type, Object scope)
    {
        updateScope(key(RequestType.SUBTYPE_OF_BY_CLASS, type), scope);
        parentTypesClassesToBind.add(type);
    }

    public void addAncestorTypeClassToBind(Class<?> type, Object scope)
    {
        updateScope(key(RequestType.SUBTYPE_OF_BY_TYPE_DEEP, type), scope);
        ancestorTypesClassesToBind.add(type);
    }

    public void addTypeRegexesToScan(String type)
    {
        typesRegexToScan.add(type);
    }

    public void addSpecificationToScan(Specification<Class<?>> specification)
    {
        specificationsToScan.add(specification);
    }

    public void addParentTypeRegexesToScan(String type)
    {
        parentTypesRegexToScan.add(type);
    }

    public void addTypeRegexesToBind(String type, Object scope)
    {
        updateScope(key(RequestType.TYPE_OF_BY_REGEX_MATCH, type), scope);
        parentTypesRegexToBind.add(type);
    }

    private void updateScope(Key key, Object scope)
    {
        if (scope != null)
        {
            mapOfScopes.put(key, scope);
        } else
        {
            mapOfScopes.put(key, Scopes.NO_SCOPE);
        }
    }

    public void addSpecificationToBind(Specification<Class<?>> specification, Object scope)
    {
        specificationsToBind.add(specification);
        updateScope(key(RequestType.VIA_SPECIFICATION, specification), scope);
    }

    public void addAnnotationTypesToScan(Class<? extends Annotation> types)
    {
        annotationTypesToScan.add(types);
    }

    public void addAnnotationTypesToBind(Class<? extends Annotation> types, Object scope)
    {
        annotationTypesToBind.add(types);
        updateScope(key(RequestType.ANNOTATION_TYPE, types), scope);
    }

    public void addMetaAnnotationTypesToBind(Class<? extends Annotation> types, Object scope)
    {
        metaAnnotationTypesToBind.add(types);
        updateScope(key(RequestType.META_ANNOTATION_TYPE, types), scope);
    }

    public void addAnnotationRegexesToScan(String names)
    {
        annotationRegexToScan.add(names);
    }

    public void addAnnotationRegexesToBind(String names, Object scope)
    {
        annotationRegexToBind.add(names);
        updateScope(key(RequestType.ANNOTATION_REGEX_MATCH, names), scope);
    }

    public void addMetaAnnotationRegexesToBind(String names, Object scope)
    {
        metaAnnotationRegexToBind.add(names);
        updateScope(key(RequestType.META_ANNOTATION_REGEX_MATCH, names), scope);
    }

    public Map<Class<?>, Object> getClassesWithScopes()
    {
        return unmodifiableMap(classesWithScopes);
    }

}
