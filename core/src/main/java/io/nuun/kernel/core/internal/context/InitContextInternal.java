/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nuun.kernel.core.internal.context;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.annotations.KernelModule;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.inmemory.Classpath;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.RequestType;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.KernelCore;
import io.nuun.kernel.core.internal.ModuleEmbedded;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner.Callback;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner.CallbackResources;
import io.nuun.kernel.core.internal.scanner.ClasspathScannerFactory;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathStrategy;
import io.nuun.kernel.core.internal.scanner.inmemory.InMemoryMultiThreadClasspath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.kametic.specifications.Specification;
import org.kametic.specifications.reflect.DescendantOfSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.inject.Module;
import com.google.inject.Scopes;

@SuppressWarnings("rawtypes")
public class InitContextInternal implements InitContext
{

    private Logger                                                 logger      = LoggerFactory.getLogger(InitContextInternal.class);

    ClasspathScanner                                               classpathScanner;

    private List<Class<?>>                                         parentTypesClassesToScan;
    private List<Class<?>>                                         ancestorTypesClassesToScan;
    private List<Class<?>>                                         typesClassesToScan;
    private List<String>                                           typesRegexToScan;
    private List<Specification<Class<?>>>                          specificationsToScan;
    private List<String>                                           resourcesRegexToScan;
    private List<String>                                           parentTypesRegexToScan;
    private List<Class<? extends Annotation>>                      annotationTypesToScan;
    private List<String>                                           annotationRegexToScan;

    private List<Class<?>>                                         parentTypesClassesToBind;
    private List<Class<?>>                                         ancestorTypesClassesToBind;
    private List<String>                                           parentTypesRegexToBind;
    private List<Specification<Class<?>>>                          specificationsToBind;
    private Map<Key, Object>                                       mapOfScopes;
    private List<Class<? extends Annotation>>                      annotationTypesToBind;
    private List<Class<? extends Annotation>>                      metaAnnotationTypesToBind;
    private List<String>                                           annotationRegexToBind;
    private List<String>                                           metaAnnotationRegexToBind;

    private List<String>                                           propertiesPrefix;

    private List<UnitModule>                                   childModules;
    private List<UnitModule>                                   childOverridingModules;
    private List<String>                                           packageRoots;
    private Set<URL>                                               additionalClasspathScan;
    private ClasspathStrategy                                      classpathStrategy;

    private Set<Class<?>>                                          classesToBind;
    private Map<Class<?>, Object>                                  classesWithScopes;

    private Collection<String>                                     propertiesFiles;

    private Map<String, String>                                    kernelParams;

    private Map<Class<?>, Collection<Class<?>>>                    mapSubTypes;
    private Map<Class<?>, Collection<Class<?>>>                    mapAncestorTypes;
    private Map<String, Collection<Class<?>>>                      mapSubTypesByName;
    private Map<String, Collection<Class<?>>>                      mapTypesByName;
    private Map<Specification, Collection<Class<?>>>               mapTypesBySpecification;
    private Map<Class<? extends Annotation>, Collection<Class<?>>> mapAnnotationTypes;
    private Map<String, Collection<Class<?>>>                      mapAnnotationTypesByName;
    private Map<String, Collection<String>>                        mapPropertiesFiles;
    private Map<String, Collection<String>>                        mapResourcesByRegex;

    private String                                                 initialPropertiesPrefix;

    private int                                                    roundNumber = 0;

    private ClasspathScanMode                                      classpathScanMode;

    /**
     * @param classpathScanMode
     * @param inPackageRoots
     */
    public InitContextInternal(String initialPropertiesPrefix, Map<String, String> kernelParams)
    {
        this(initialPropertiesPrefix, kernelParams, ClasspathScanMode.NOMINAL);
    }

    /**
     * @param classpathScanMode
     * @param inPackageRoots
     */
    public InitContextInternal(String initialPropertiesPrefix, Map<String, String> kernelParams, ClasspathScanMode classpathScanMode)
    {
        this.classpathScanMode = classpathScanMode;
        String classpathStrategyNameParam = kernelParams.get(KernelCore.NUUN_CP_STRATEGY_NAME);
        String classpathStrategyAdditionalParam = kernelParams.get(KernelCore.NUUN_CP_STRATEGY_ADD);

        classpathStrategy = new ClasspathStrategy(classpathStrategyNameParam == null
                ? ClasspathStrategy.Strategy.ALL
                : ClasspathStrategy.Strategy.valueOf(classpathStrategyNameParam.toUpperCase()), classpathStrategyAdditionalParam == null
                ? true
                : Boolean.parseBoolean(classpathStrategyAdditionalParam));

        packageRoots = new LinkedList<String>();
        this.initialPropertiesPrefix = initialPropertiesPrefix;
        this.kernelParams = kernelParams;
        childModules = new LinkedList<UnitModule>();
        childOverridingModules = new LinkedList<UnitModule>();
        classesToBind = new HashSet<Class<?>>();
        classesWithScopes = new HashMap<Class<?>, Object>();
        reset();
    }

    public void reset()
    {
        mapSubTypes = new HashMap<Class<?>, Collection<Class<?>>>();
        mapAncestorTypes = new HashMap<Class<?>, Collection<Class<?>>>();
        mapSubTypesByName = new HashMap<String, Collection<Class<?>>>();
        mapTypesByName = new HashMap<String, Collection<Class<?>>>();
        mapTypesBySpecification = new HashMap<Specification, Collection<Class<?>>>();
        mapAnnotationTypes = new HashMap<Class<? extends Annotation>, Collection<Class<?>>>();
        mapAnnotationTypesByName = new HashMap<String, Collection<Class<?>>>();
        mapPropertiesFiles = new HashMap<String, Collection<String>>();
        mapResourcesByRegex = new HashMap<String, Collection<String>>();

        annotationTypesToScan = new LinkedList<Class<? extends Annotation>>();
        parentTypesClassesToScan = new LinkedList<Class<?>>();
        ancestorTypesClassesToScan = new LinkedList<Class<?>>();
        typesClassesToScan = new LinkedList<Class<?>>();
        typesRegexToScan = new LinkedList<String>();
        specificationsToScan = new LinkedList<Specification<Class<?>>>();
        resourcesRegexToScan = new LinkedList<String>();
        parentTypesRegexToScan = new LinkedList<String>();
        annotationRegexToScan = new LinkedList<String>();

        annotationTypesToBind = new LinkedList<Class<? extends Annotation>>();
        metaAnnotationTypesToBind = new LinkedList<Class<? extends Annotation>>();
        parentTypesClassesToBind = new LinkedList<Class<?>>();
        ancestorTypesClassesToBind = new LinkedList<Class<?>>();
        parentTypesRegexToBind = new LinkedList<String>();
        specificationsToBind = new LinkedList<Specification<Class<?>>>();
        mapOfScopes = new HashMap<Key, Object>();
        annotationRegexToBind = new LinkedList<String>();
        metaAnnotationRegexToBind = new LinkedList<String>();

        propertiesPrefix = new LinkedList<String>();
        propertiesPrefix.add(initialPropertiesPrefix);
        additionalClasspathScan = new HashSet<URL>();
    }

    public void classpathScanMode(ClasspathScanMode classpathScanMode)
    {
        this.classpathScanMode = classpathScanMode;
    }

    private void initScanner()
    {
        String[] packageRootArray = new String[packageRoots.size()];
        packageRoots.toArray(packageRootArray);

        ClasspathScannerFactory classpathScannerFactory = new ClasspathScannerFactory();
        if (classpathScanMode == ClasspathScanMode.NOMINAL)
        {
            classpathScanner = classpathScannerFactory.create(classpathStrategy, additionalClasspathScan, packageRootArray);
        }
        else if (classpathScanMode == ClasspathScanMode.IN_MEMORY)
        {

            Classpath classpath = InMemoryMultiThreadClasspath.INSTANCE;
            classpathScanner = classpathScannerFactory.createInMemory(classpath, packageRootArray);

        }

    }

    class IsModuleOverriding implements Predicate<Class<? extends Module>>
    {

        @Override
        public boolean apply(Class<? extends Module> input)
        {
            KernelModule annotation = input.getAnnotation(KernelModule.class);

            return annotation.overriding();
        }

    }

    class IsModuleAbstract implements Predicate<Class<? extends Module>>
    {

        @Override
        public boolean apply(Class<? extends Module> input)
        {
            return Modifier.isAbstract(input.getModifiers());
        }

    }

    class ModuleClass2Instance implements Function<Class<? extends Module>, UnitModule>
    {

        /*
         * (non-Javadoc)
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        @Override
        public UnitModule apply(Class<? extends Module> classpathClass)
        {
            try
            {
                return new ModuleEmbedded( classpathClass.newInstance());
            }
            catch (InstantiationException e)
            {
                logger.warn("Error when instantiating module " + classpathClass, e);
            }
            catch (IllegalAccessException e)
            {
                logger.warn("Error when instantiating module " + classpathClass, e);
            }
            return null;
        }
    }

    public void executeRequests()
    {
        initScanner();

        { // bind modules
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @SuppressWarnings("unchecked")
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {

                    Collection<Class<? extends Module>> scanResult2 = (Collection) scanResult;
                    FluentIterable<UnitModule> nominals = FluentIterable.from(scanResult2).filter(and(not(new IsModuleOverriding()), not(new IsModuleAbstract())))
                            .transform(new ModuleClass2Instance());
                    FluentIterable<UnitModule> overriders = FluentIterable.from(scanResult2).filter(and(new IsModuleOverriding(), not(new IsModuleAbstract())))
                            .transform(new ModuleClass2Instance());

                    childModules.addAll(nominals.toImmutableSet());
                    childOverridingModules.addAll(overriders.toImmutableSet());
                }
            };
            classpathScanner.scanClasspathForAnnotation(KernelModule.class, callback); // OK

        }

        // CLASSES TO SCAN
        for (final Class<?> parentType : parentTypesClassesToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapSubTypes.put(parentType, scanResult);
                }
            };
            classpathScanner.scanClasspathForSubTypeClass(parentType, callback); // OK
        }

        for (final Class<?> parentType : ancestorTypesClassesToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapAncestorTypes.put(parentType, scanResult);
                }
            };
            classpathScanner.scanClasspathForSpecification(new DescendantOfSpecification(parentType), callback); // ok
        }

        // for (Class<?> type : this.typesClassesToScan)
        // {
        // scanClasspathForSubType = this.classpathScanner.scanClasspathForTypeClass(type);
        // // clässes.addAll(scanClasspathForSubType);
        // this.mapTypes.put(type, scanClasspathForSubType);
        // }

        for (final String typeName : parentTypesRegexToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapSubTypesByName.put(typeName, scanResult);
                }
            };
            classpathScanner.scanClasspathForSubTypeRegex(typeName, callback); // OK
        }

        for (final String typeName : typesRegexToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapTypesByName.put(typeName, scanResult);
                }
            };
            classpathScanner.scanClasspathForTypeRegex(typeName, callback); // OK
        }

        for (final Specification<Class<?>> spec : specificationsToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapTypesBySpecification.put(spec, scanResult);
                }
            };
            classpathScanner.scanClasspathForSpecification(spec, callback); // OK
        }

        for (final Class<? extends Annotation> annotationType : annotationTypesToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapAnnotationTypes.put(annotationType, scanResult);
                }
            };
            classpathScanner.scanClasspathForAnnotation(annotationType, callback);// ok
        }

        for (final String annotationName : annotationRegexToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapAnnotationTypesByName.put(annotationName, scanResult);
                }
            };
            classpathScanner.scanClasspathForAnnotationRegex(annotationName, callback); // ok

        }

        // CLASSES TO BIND
        // ===============
        for (final Class<?> parentType : parentTypesClassesToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.SUBTYPE_OF_BY_CLASS;
                    addScopeToClasses(scanResult, scope(requestType, parentType), classesWithScopes);

                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForSubTypeClass(parentType, callback); // OK

        }
        for (final Class<?> ancestorType : ancestorTypesClassesToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.SUBTYPE_OF_BY_TYPE_DEEP;
                    addScopeToClasses(scanResult, scope(requestType, ancestorType), classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForSpecification(new DescendantOfSpecification(ancestorType), callback); // OK
        }

        // TODO vérifier si ok parent types vs type. si ok changer de nom
        for (final String typeName : parentTypesRegexToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.SUBTYPE_OF_BY_REGEX_MATCH;
                    addScopeToClasses(scanResult, scope(requestType, typeName), classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForTypeRegex(typeName, callback); // ok
        }

        for (final Specification<Class<?>> spec : specificationsToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.VIA_SPECIFICATION;
                    addScopeToClasses(scanResult, scope(requestType, spec), classesWithScopes);

                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForSpecification(spec, callback); // ok
        }

        for (final Class<? extends Annotation> annotationType : annotationTypesToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.ANNOTATION_TYPE;
                    addScopeToClasses(scanResult, scope(requestType, annotationType), classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForAnnotation(annotationType, callback); // OK
        }

        for (final Class<? extends Annotation> metaAnnotationType : metaAnnotationTypesToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.META_ANNOTATION_TYPE;
                    addScopeToClasses(scanResult, scope(requestType, metaAnnotationType), classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForMetaAnnotation(metaAnnotationType, callback); // ok
        }

        for (final String annotationNameRegex : annotationRegexToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.ANNOTATION_REGEX_MATCH;
                    addScopeToClasses(scanResult, scope(requestType, annotationNameRegex), classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForAnnotationRegex(annotationNameRegex, callback); // ok
        }

        for (final String metaAnnotationNameRegex : metaAnnotationRegexToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    RequestType requestType = RequestType.META_ANNOTATION_REGEX_MATCH;
                    addScopeToClasses(scanResult, scope(requestType, metaAnnotationNameRegex), classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            classpathScanner.scanClasspathForMetaAnnotationRegex(metaAnnotationNameRegex, callback); // ok
        }

        // Resources to scan

        for (final String regex : resourcesRegexToScan)
        {
            CallbackResources callback = new CallbackResources()
            {
                @Override
                public void callback(Collection<String> scanResult)
                {
                    mapResourcesByRegex.put(regex, scanResult);
                }
            };
            classpathScanner.scanClasspathForResource(regex, callback); // OK
        }

        // PROPERTIES TO FETCH
        propertiesFiles = new HashSet<String>();
        for (final String prefix : propertiesPrefix)
        {
            CallbackResources callback = new CallbackResources()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<String> scanResult)
                {
                    propertiesFiles.addAll(scanResult);
                    mapPropertiesFiles.put(prefix, scanResult);
                }
            };
            classpathScanner.scanClasspathForResource(prefix + ".*\\.properties", callback); // OK
        }

        // ACTUALLY LAUNCH THE SEARCH
        classpathScanner.doClasspathScan();
    }

    private Object scope(RequestType requestType, Object spec)
    {
        Object scope = mapOfScopes.get(key(requestType, spec));
        if (null == scope)
        {
            scope = Scopes.NO_SCOPE;
        }
        return scope;
    }

    private void addScopeToClasses(Collection<Class<?>> classes, Object scope, Map<Class<?>, Object> inClassesWithScopes)
    {
        for (Class<?> klass : classes)
        {
            if (!inClassesWithScopes.containsKey(klass) && scope != null)
            {
                inClassesWithScopes.put(klass, scope);
            }
            else
            {
                Object insideScope = inClassesWithScopes.get(klass);
                if (!insideScope.equals(scope))
                {
                    String format = String.format("Class %s is already associated with scope %s but  %s", klass.getName(), insideScope, scope);
                    logger.error(format);
                    throw new KernelException(format);
                }
            }
        }
    }

    public void addClasspathsToScan(Set<URL> paths)
    {
        if (paths != null && paths.size() > 0)
        {
            additionalClasspathScan.addAll(paths);
        }
    }

    public void addClasspathToScan(URL path)
    {
        if (path != null)
        {
            additionalClasspathScan.add(path);
        }
    }

    @Override
    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass()
    {
        return Collections.unmodifiableMap(mapSubTypes);
    }

    @Override
    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass()
    {
        return Collections.unmodifiableMap(mapAncestorTypes);
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex()
    {
        return Collections.unmodifiableMap(mapSubTypesByName);
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedTypesByRegex()
    {
        return Collections.unmodifiableMap(mapTypesByName);
    }

    @Override
    public Map<Specification, Collection<Class<?>>> scannedTypesBySpecification()
    {
        return Collections.unmodifiableMap(mapTypesBySpecification);
    }

    @Override
    public Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass()
    {
        return Collections.unmodifiableMap(mapAnnotationTypes);
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex()
    {
        return Collections.unmodifiableMap(mapAnnotationTypesByName);
    }

    @Override
    public Map<String, Collection<String>> mapPropertiesFilesByPrefix()
    {
        return Collections.unmodifiableMap(mapPropertiesFiles);
    }

    @Override
    public Map<String, Collection<String>> mapResourcesByRegex()
    {
        return Collections.unmodifiableMap(mapResourcesByRegex);
    }

    public void addPropertiesPrefix(String prefix)
    {
        propertiesPrefix.add(prefix);
    }

    public void addPackageRoot(String root)
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

    public void addTypeClassToScan(Class<?> type)
    {
        typesClassesToScan.add(type);
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

    /**
     * @category bind
     * @param specification
     */
    // public void addSpecificationToBind(Specification<Class<?>> specification)
    // {
    // this.specificationsToBind.add(specification);
    // this.mapSpecificationScope.put(specification, Scopes.NO_SCOPE);
    // }

    private void updateScope(Key key, Object scope)
    {
        if (scope != null)
        {
            mapOfScopes.put(key, scope);
        }
        else
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

    public void addChildModule(Module module)
    {
        childModules.add(new ModuleEmbedded(module));
    }

    public void addChildOverridingModule(Module module)
    {
        childOverridingModules.add(new ModuleEmbedded(module));
    }

    // public void setContainerContext(Object containerContext)
    // {
    // this.containerContext = containerContext;
    // }

    // INTERFACE KERNEL PARAM USED BY PLUGIN IN INIT //

    // public Object containerContext()
    // {
    // return this.containerContext;
    // }

    @Override
    public String kernelParam(String key)
    {
        return kernelParams.get(key);
    }

    @Override
    public Collection<Class<?>> classesToBind()
    {
        return Collections.unmodifiableSet(classesToBind);
    }

    public Map<Class<?>, Object> classesWithScopes()
    {
        return Collections.unmodifiableMap(classesWithScopes);
    }

    @Override
    public List<UnitModule> moduleResults()
    {
        return Collections.unmodifiableList(childModules);
    }

    @Override
    public List<UnitModule> moduleOverridingResults()
    {
        return Collections.unmodifiableList(childOverridingModules);
    }

    @Override
    public Collection<String> propertiesFiles()
    {
        return Collections.unmodifiableCollection(propertiesFiles);
    }

    @Override
    public Collection<? extends Plugin> pluginsRequired()
    {
        return Collections.emptySet();
    }

    @Override
    public Collection<? extends Plugin> dependentPlugins()
    {
        return Collections.emptySet();
    }

    @Override
    public int roundNumber()
    {
        return roundNumber;
    }

    public void roundNumber(int roundNumber)
    {
        this.roundNumber = roundNumber;

    }

    static class Key
    {
        private final RequestType type;
        private final Object      key;

        public Key(RequestType type, Object key)
        {
            this.type = type;
            this.key = key;
        }

        @Override
        public boolean equals(Object obj)
        {
            Key key2 = (Key) obj;
            return new EqualsBuilder().append(type, key2.type).append(key, key2.key).isEquals();
        }

        @Override
        public int hashCode()
        {
            return new HashCodeBuilder().append(type).append(key).toHashCode();
        }
    }

}