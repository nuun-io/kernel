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
package io.nuun.kernel.core.internal;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.config.DependencyInjectionMode;
import io.nuun.kernel.api.di.GlobalModule;
import io.nuun.kernel.api.di.ModuleValidation;
import io.nuun.kernel.api.di.ObjectGraph;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.RoundEnvironmentInternal;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequestType;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.context.InitContextInternal;
import io.nuun.kernel.core.internal.graph.Graph;
import io.nuun.kernel.spi.DependencyInjectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Epo Jemba
 */
public final class KernelCore implements Kernel
{

    private final int                                      MAXIMAL_ROUND_NUMBER           = 50;
    private final Logger                                   logger;
    private static ConcurrentHashMap<String, Kernel>       kernels                        = new ConcurrentHashMap<String, Kernel>();
    private final String                                   name;

    private final String                                   NUUN_PROPERTIES_PREFIX         = "nuun-";

    private ServiceLoader<Plugin>                          pluginLoader;
    private boolean                                        spiPluginEnabled               = true;
    private Map<String, Plugin>                            plugins                        = Collections.synchronizedMap(new HashMap<String, Plugin>());
    private Map<String, Plugin>                            pluginsToAdd                   = Collections.synchronizedMap(new HashMap<String, Plugin>());

    private final InitContextInternal                      initContext;
    private Injector                                       mainInjector;
    private final AliasMap                                 kernelParamsAndAlias           = new AliasMap();

    private boolean                                        started                        = false;
    private boolean                                        initialized                    = false;
    private Context                                        context;
    private Collection<DependencyInjectionProvider>        dependencyInjectionProviders;
    private Object                                         containerContext;
    private ArrayList<Plugin>                              orderedPlugins;

    private Collection<DependencyInjectionProvider>        globalDependencyInjectionProviders;
    private List<Iterator<Plugin>>                         pluginIterators;
    private List<Plugin>                                   fetchedPlugins;
    private Set<URL>                                       globalAdditionalClasspath;
    private RoundEnvironmentInternal roundEnv;
    private DependencyInjectionMode                        dependencyInjectionMode;
    private ClasspathScanMode                              classpathScanMode              = ClasspathScanMode.NOMINAL;
    private final List<ModuleValidation>                   globalModuleValidations        = Collections.synchronizedList(new ArrayList<ModuleValidation>());
    private final Map<Class<? extends Plugin>, UnitModule> unitModules                    = Maps.newConcurrentMap();
    private final Map<Class<? extends Plugin>, UnitModule> overridingUnitModules          = Maps.newConcurrentMap();
    private final Map<Class<? extends Plugin>, UnitModule> nonGuiceUnitModules           = Maps.newConcurrentMap();
    private final Map<Class<? extends Plugin>, UnitModule> nonGuiceOverridingUnitModules = Maps.newConcurrentMap();
    private Module                                         mainFinalModule;

    KernelCore(KernelConfigurationInternal kernelConfigurationInternal)
    {
        name = KERNEL_PREFIX_NAME + kernels.size();
        logger = LoggerFactory.getLogger(KernelCore.class.getPackage().getName() + '.' + name());
        initContext = new InitContextInternal(NUUN_PROPERTIES_PREFIX, kernelParamsAndAlias, classpathScanMode);

        if (!kernels.contains(name()))
        {
            kernels.put(name(), this);
        }
        else
        {
            throw new KernelException(String.format("A kernel named %s already exists", name()));
        }

        kernelConfigurationInternal.apply(this);
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public boolean isStarted()
    {
        return started;
    }

    @Override
    public boolean isInitialized()
    {
        return initialized;
    }

    @Override
    public synchronized void init()
    {
        if (!initialized)
        {
            fetchPlugins();
            computeAliases();
            initRoundEnvironment();
            checkPlugins();
            fetchGlobalParametersFromPlugins();
            initPlugins();
            computeGlobalModuleProviders();
            initialized = true;
        }
        else
        {
            throw new KernelException("Kernel is already initialized");
        }
    }

    private void initRoundEnvironment()
    {
        // we initialize plugins
        roundEnv = new RoundEnvironmentInternal();

        for (Plugin plugin : fetchedPlugins)
        {
            // we pass the roundEnvironment
            plugin.provideRoundEnvironment(roundEnv);

        }

    }

    private void fetchGlobalParametersFromPlugins()
    {
        globalDependencyInjectionProviders = new HashSet<DependencyInjectionProvider>();
        globalAdditionalClasspath = Sets.newHashSet();

        // Constants from plugin outside rounds
        // We pass the container context object for plugin
        for (Plugin plugin : plugins.values())
        {
            plugin.provideContainerContext(containerContext);

            String name = plugin.name();
            logger.info("Get additional classpath to scan from Plugin {}.", name);

            Set<URL> computeAdditionalClasspathScan = plugin.computeAdditionalClasspathScan();
            if (computeAdditionalClasspathScan != null && computeAdditionalClasspathScan.size() > 0)
            {
                logger.info("Adding from Plugin {} start.", name);
                for (URL url : computeAdditionalClasspathScan)
                {
                    if (url != null)
                    {
                        globalAdditionalClasspath.add(url);
                        logger.debug(url.toExternalForm());
                    }
                }
                logger.info("Adding from Plugin {} end. {} elements.", name, "" + computeAdditionalClasspathScan.size());
            }
            // Convert dependency manager classes to instances //
            DependencyInjectionProvider iocProvider = plugin.dependencyInjectionProvider();
            if (iocProvider != null)
            {
                globalDependencyInjectionProviders.add(iocProvider);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fetchPlugins()
    {
        // plugin from kernel call api
        Iterator<Plugin> iterator1 = pluginsToAdd.values().iterator();

        // TODO add unit and test integration test for this
        if (spiPluginEnabled)
        {
            pluginLoader = ServiceLoader.load(Plugin.class, Thread.currentThread().getContextClassLoader());

            // plugin from service loader
            Iterator<Plugin> iterator2 = pluginLoader.iterator();
            pluginIterators = Arrays.asList(iterator2, iterator1);
        }
        else
        {
            pluginIterators = Arrays.asList(iterator1);
        }

        fetchedPlugins = new LinkedList<Plugin>();

        for (Iterator<Plugin> iterator : pluginIterators)
        {
            Plugin plugin;
            while (iterator.hasNext())
            {
                plugin = iterator.next();
                fetchedPlugins.add(plugin);
            }

        }
    }

    private void computeAliases()
    {
        // Compute alias
        for (Plugin plugin : fetchedPlugins)
        {
            Map<String, String> pluginKernelParametersAliases = plugin.kernelParametersAliases();
            for (Entry<String, String> entry : pluginKernelParametersAliases.entrySet())
            {
                // entry.getValue() is the alias to give
                // entry.getKey() is the key to alias

                logger.info("Adding alias parameter \"{}\" to key \"{}\".", entry.getKey(), entry.getValue());
                kernelParamsAndAlias.putAlias(entry.getKey(), entry.getValue());
            }
        }

        //
        if (kernelParamsAndAlias.containsKey(NUUN_ROOT_PACKAGE))
        {
            String tmp = kernelParamsAndAlias.get(NUUN_ROOT_PACKAGE);

            fillPackagesRoot(tmp);
        }
    }


    /**
     * 
     */
    private void checkPlugins()
    {
        logger.info("Plugins initialisation ");
        plugins.clear();

        List<Class<? extends Plugin>> pluginClasses = new ArrayList<Class<? extends Plugin>>();

        for (Plugin plugin : fetchedPlugins)
        {

            String pluginName = plugin.name();
            logger.info("checking Plugin {}.", pluginName);
            if (!Strings.isNullOrEmpty(pluginName))
            {
                Object ok = plugins.put(pluginName, plugin);
                if (ok == null)
                {
                    // Check for required parameter
                    // ============================
                    Collection<KernelParamsRequest> kernelParamsRequests = plugin.kernelParamsRequests();
                    Collection<String> computedMandatoryParams = new HashSet<String>();
                    for (KernelParamsRequest kernelParamsRequest : kernelParamsRequests)
                    {
                        if (kernelParamsRequest.requestType == KernelParamsRequestType.MANDATORY)
                        {
                            computedMandatoryParams.add(kernelParamsRequest.keyRequested);
                        }
                    }

                    if (kernelParamsAndAlias.containsAllKeys(computedMandatoryParams))
                    // if (kernelParams.keySet().containsAll(computedMandatoryParams))
                    {
                        pluginClasses.add(plugin.getClass());
                    }
                    else
                    {
                        logger.error("plugin {} miss parameter/s : {}", pluginName, kernelParamsRequests.toString());
                        throw new KernelException("plugin " + pluginName + " miss parameter/s : " + kernelParamsRequests.toString());
                    }

                }
                else
                {
                    logger.error("Can not have 2 Plugin {} of the same type {}. please fix this before the kernel can start.", pluginName, plugin.getClass()
                            .getName());
                    throw new KernelException("Can not have 2 Plugin %s of the same type %s. please fix this before the kernel can start.", pluginName, plugin
                            .getClass().getName());
                }
            }
            else
            {
                logger.warn("Plugin {} has no correct name it won't be installed.", plugin.getClass());
                throw new KernelException("Plugin %s has no correct name it won't be installed.", pluginName);
            }

        }

        // Check for required and dependent plugins
        for (Plugin plugin : plugins.values())
        {
            {
                Collection<Class<? extends Plugin>> pluginDependenciesRequired = plugin.requiredPlugins();

                if (pluginDependenciesRequired != null && !pluginDependenciesRequired.isEmpty() && !pluginClasses.containsAll(pluginDependenciesRequired))
                {
                    logger.error("plugin {} misses the following plugin/s as dependency/ies {}", plugin.name(), pluginDependenciesRequired.toString());
                    throw new KernelException(
                            "plugin %s misses the following plugin/s as dependency/ies %s", plugin.name(), pluginDependenciesRequired.toString());
                }
            }

            {
                Collection<Class<? extends Plugin>> dependentPlugin = plugin.dependentPlugins();

                if (dependentPlugin != null && !dependentPlugin.isEmpty() && !pluginClasses.containsAll(dependentPlugin))
                {
                    logger.error("plugin {} misses the following plugin/s as dependee/s {}", plugin.name(), dependentPlugin.toString());
                    throw new KernelException("plugin %s misses the following plugin/s as dependee/s %s", plugin.name(), dependentPlugin.toString());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see io.nuun.kernel.core.internal.Kernel#start()
     */
    @Override
    public synchronized void start()
    {
        if (initialized)
        {

            // Compute Guice Stage
            Stage stage = Stage.PRODUCTION;
            if (dependencyInjectionMode == DependencyInjectionMode.PRODUCTION)
            {
                stage = Stage.PRODUCTION;
            }
            else if (dependencyInjectionMode == DependencyInjectionMode.DEVELOPMENT)
            {
                stage = Stage.DEVELOPMENT;
            }
            else if (dependencyInjectionMode == DependencyInjectionMode.TOOL)
            {
                stage = Stage.TOOL;
            }

            mainInjector = Guice.createInjector(stage, mainFinalModule);

            // Here we can pass the mainInjector to the non guice modules

            context = mainInjector.getInstance(Context.class);

            // 1) inject plugins via injector
            // 2) inject context via injector
            // 2) start them

            for (Plugin plugin : orderedPlugins)
            {
                mainInjector.injectMembers(plugin);

                plugin.start(context);
            }

            started = true;
        }
        else
        {
            throw new KernelException("Kernel is not initialized.");
        }
    }

    @Override
    public ObjectGraph objectGraph()
    {
        return new ObjectGraphEmbedded(mainInjector);
    }

    @Override
    public UnitModule unitModule(Class<? extends Plugin> pluginClass)
    {
        return unitModules.get(pluginClass);
    }

    @Override
    public UnitModule overridingUnitModule(Class<? extends Plugin> pluginClass)
    {
        return overridingUnitModules.get(pluginClass);
    }

    @Override
    public UnitModule nonGuiceUnitModule(Class<? extends Plugin> plugin)
    {
        return nonGuiceUnitModules.get(plugin);
    }

    @Override
    public UnitModule nonGuiceOverridingUnitModule(Class<? extends Plugin> plugin)
    {
        return nonGuiceOverridingUnitModules.get(plugin);
    }

    @Override
    public GlobalModule globalModule()
    {
        return GlobalModule.class.cast(new ModuleEmbedded(mainFinalModule));
    }

    @Override
    public List<Plugin> plugins()
    {
        return null;
    }

    @Override
    public void stop()
    {
        if (started)
        {
            ListIterator<Plugin> li = orderedPlugins.listIterator(orderedPlugins.size());

            // Iterate in reverse.
            while (li.hasPrevious())
            {
                Plugin plugin = li.previous();
                plugin.stop();
            }
        }
        else
        {
            throw new KernelException("Kernel is not started.");
        }
    }

    @SuppressWarnings("unchecked")
    private void initPlugins()
    {

        // We reset the resettable element of initcontext
        initContext.reset();

        // Get plugins requests
        // ====================
        Collection<Plugin> globalPlugins = plugins.values(); // first round all plugins

        // We sort them
        ArrayList<Plugin> unOrderedPlugins = new ArrayList<Plugin>(globalPlugins);
        logger.info("unordered plugins: (" + unOrderedPlugins.size() + ") " + unOrderedPlugins);
        orderedPlugins = sortPlugins(unOrderedPlugins);
        logger.info("ordered plugins: (" + orderedPlugins.size() + ") " + orderedPlugins);
        Map<String, InitState> states = new HashMap<String, InitState>();

        ArrayList<Plugin> roundOrderedPlugins = new ArrayList<Plugin>(orderedPlugins);

        do
        { // ROUND ITERATIONS

            // we update the number of initialization round.
            initContext.roundNumber(roundEnv.roundNumber());

            logger.info("ROUND " + roundEnv.roundNumber() + " of the kernel initialisation.");

            for (Plugin plugin : roundOrderedPlugins)
            {

                // Configure properties prefixes
                String pluginPropertiesPrefix = plugin.pluginPropertiesPrefix();
                if (!Strings.isNullOrEmpty(pluginPropertiesPrefix))
                {
                    initContext.addPropertiesPrefix(pluginPropertiesPrefix);
                }

                // Configure package root
                String pluginPackageRoot = plugin.pluginPackageRoot();
                fillPackagesRoot(pluginPackageRoot);

                Collection<ClasspathScanRequest> classpathScanRequests = plugin.classpathScanRequests();
                if (classpathScanRequests != null && classpathScanRequests.size() > 0)
                {
                    for (ClasspathScanRequest request : classpathScanRequests)
                    {
                        switch (request.requestType)
                        {
                            case ANNOTATION_TYPE:
                                initContext.addAnnotationTypesToScan((Class<? extends Annotation>) request.objectRequested);
                                break;
                            case ANNOTATION_REGEX_MATCH:
                                initContext.addAnnotationRegexesToScan((String) request.objectRequested);
                                break;
                            // case META_ANNOTATION_TYPE:
                            // this.initContext.addAnnotationTypesToScan((Class<? extends Annotation>)
                            // request.objectRequested);
                            // break;
                            // case META_ANNOTATION_REGEX_MATCH:
                            // this.initContext.addAnnotationRegexesToScan((String) request.objectRequested);
                            // break;
                            case SUBTYPE_OF_BY_CLASS:
                                initContext.addParentTypeClassToScan((Class<?>) request.objectRequested);
                                break;
                            case SUBTYPE_OF_BY_TYPE_DEEP:
                                initContext.addAncestorTypeClassToScan((Class<?>) request.objectRequested);
                                break;
                            case SUBTYPE_OF_BY_REGEX_MATCH:
                                initContext.addParentTypeRegexesToScan((String) request.objectRequested);
                                break;
                            case RESOURCES_REGEX_MATCH:
                                initContext.addResourcesRegexToScan((String) request.objectRequested);
                                // this.initContext.addTypeClassToScan((Class<?>) request.objectRequested);
                                break;
                            case TYPE_OF_BY_REGEX_MATCH:
                                initContext.addTypeRegexesToScan((String) request.objectRequested);
                                break;
                            case VIA_SPECIFICATION:
                                initContext.addSpecificationToScan(request.specification);
                                break;
                            default:
                                logger.warn("{} is not a ClasspathScanRequestType a o_O", request.requestType);
                                break;
                        }
                    }
                }

                Collection<BindingRequest> bindingRequests = plugin.bindingRequests();
                if (bindingRequests != null && bindingRequests.size() > 0)
                {
                    for (BindingRequest request : bindingRequests)
                    {
                        switch (request.requestType)
                        {
                            case ANNOTATION_TYPE:
                                initContext.addAnnotationTypesToBind((Class<? extends Annotation>) request.requestedObject, request.requestedScope);
                                break;
                            case ANNOTATION_REGEX_MATCH:
                                initContext.addAnnotationRegexesToBind((String) request.requestedObject, request.requestedScope);
                                break;
                            case META_ANNOTATION_TYPE:
                                initContext.addMetaAnnotationTypesToBind((Class<? extends Annotation>) request.requestedObject, request.requestedScope);
                                break;
                            case META_ANNOTATION_REGEX_MATCH:
                                initContext.addMetaAnnotationRegexesToBind((String) request.requestedObject, request.requestedScope);
                                break;
                            case SUBTYPE_OF_BY_CLASS:
                                initContext.addParentTypeClassToBind((Class<?>) request.requestedObject, request.requestedScope);
                                break;
                            case SUBTYPE_OF_BY_TYPE_DEEP:
                                initContext.addAncestorTypeClassToBind((Class<?>) request.requestedObject, request.requestedScope);
                                break;
                            case SUBTYPE_OF_BY_REGEX_MATCH:
                                initContext.addTypeRegexesToBind((String) request.requestedObject, request.requestedScope);
                                break;
                            case VIA_SPECIFICATION:
                                initContext.addSpecificationToBind(request.specification, request.requestedScope);
                                break;
                            default:
                                logger.warn("{} is not a BindingRequestType o_O !", request.requestType);
                                break;
                        }
                    }
                }
            } // end plugin request

            for (URL url : globalAdditionalClasspath)
            {
                initContext.addClasspathToScan(url);
            }

            // We launch classpath scan and store results of requests
            initContext.executeRequests();

            // INITIALISATION

            for (Plugin plugin : roundOrderedPlugins)
            {
                InitContext actualInitContext = initContext;

                // TODO : we compute dependencies only in round 0 for first version , no other plugin will be
                // given
                Collection<Class<? extends Plugin>> requiredPluginsClasses = plugin.requiredPlugins();
                Collection<Class<? extends Plugin>> dependentPluginsClasses = plugin.dependentPlugins();
                if (roundEnv.roundNumber() == 0 && requiredPluginsClasses != null && !requiredPluginsClasses.isEmpty() || dependentPluginsClasses != null
                        && !dependentPluginsClasses.isEmpty())
                {
                    Collection<Plugin> requiredPlugins = filterPlugins(globalPlugins, requiredPluginsClasses);
                    Collection<Plugin> dependentPlugins = filterPlugins(globalPlugins, dependentPluginsClasses);
                    actualInitContext = proxyfy(initContext, requiredPlugins, dependentPlugins);
                }

                String name = plugin.name();
                logger.info("initializing Plugin {}.", name);
                InitState state = plugin.init(actualInitContext);
                states.put(name, state);
            }

            // After been initialized plugin can give they module
            // Configure module //
            ArrayList<Plugin> nextRoundOrderedPlugins = new ArrayList<Plugin>();

            for (Plugin plugin : roundOrderedPlugins)
            {
                String pluginName = plugin.name();
                InitState state = states.get(pluginName);

                if (state == InitState.INITIALIZED)
                {
                    // Main // =====================================================================
                    UnitModule unitModule = plugin.unitModule();
                    {
                        boolean override = false;

                        handleUnitModule(plugin, pluginName, unitModule, override);
                    }

                    // Overriding definition
                    UnitModule overridingUnitModule = plugin.overridingUnitModule();
                    {
                        boolean override = true;
                        
                        handleUnitModule(plugin, pluginName, overridingUnitModule, override);
                        
                    }
                    
                    if (unitModule == null && overridingUnitModule == null)
                    {
                        logger.info("For information Plugin {} does not provide any UnitModule via unitModule() nor overridingUnitModule().",  pluginName);
                    }

                }
                else
                { // the plugin is not initialized we add it for a new round
                    logger.info("Plugin " + pluginName + " is not initialized. We set it for a new round");
                    nextRoundOrderedPlugins.add(plugin);
                }
            }
            roundOrderedPlugins = nextRoundOrderedPlugins;
            // increment round number
            roundEnv.incrementRoundNumber();
        }
        while (!roundOrderedPlugins.isEmpty() && roundEnv.roundNumber() < MAXIMAL_ROUND_NUMBER);

        // When all round are done.

    }

    private void handleUnitModule(Plugin plugin, String pluginName, UnitModule unitModule, boolean override)
    {
        if (unitModule != null && unitModule.nativeModule() != null)
        {
            // Conversion of native module if needed
            if ( ! Module.class.isAssignableFrom ( unitModule.nativeModule().getClass()) )
            {
                // we also keep a trace on non guice unit module in case.
                if (! override)
                {
                    nonGuiceUnitModules.put(plugin.getClass(), unitModule);
                }
                else
                {
                    nonGuiceOverridingUnitModules.put(plugin.getClass(), unitModule);
                }
                // we then convert the non guice native module into a guice module as this is the internal DI engine.
                unitModule = convertNativeModule(pluginName, unitModule.nativeModule(), override);
            }
            
            validateUnitModule(unitModule);
            
            if (! override)
            // we feed the matching unitModules map
            {
                initContext.addChildModule(Module.class.cast(unitModule.nativeModule()));
                unitModules.put(plugin.getClass(), unitModule);
            }
            else
            {
                initContext.addChildOverridingModule(Module.class.cast(unitModule.nativeModule()));
                overridingUnitModules.put(plugin.getClass(), unitModule);
            }
        } //
    }

    /**
     * This methods will create both Global ModuleProviders : nominal and overriding.
     */
    private void computeGlobalModuleProviders()
    {
        KernelGuiceModuleInternal kernelGuiceModuleInternal = new KernelGuiceModuleInternal(initContext);
        KernelGuiceModuleInternal internalKernelGuiceModuleOverriding = new KernelGuiceModuleInternal(initContext).overriding();

        mainFinalModule = Modules.override(kernelGuiceModuleInternal).with(internalKernelGuiceModuleOverriding);
    }

    private void validateUnitModule(UnitModule unitModule)
    {
        for (ModuleValidation validation : globalModuleValidations)
        {
            if (validation.canHandle(unitModule.nativeModule().getClass()))
            {
                try
                {
                    validation.validate(unitModule);
                }
                catch (Exception validationException)
                {
                    throw new KernelException("Error when validating di definition " + unitModule, validationException);
                }
            }
        }
    }

    private void fillPackagesRoot(String pluginPackageRoot)
    {
        if (!Strings.isNullOrEmpty(pluginPackageRoot))
        {
            String[] packages = null;

            packages = pluginPackageRoot.split(",");

            for (String pack : packages)
            {
                logger.info("Adding {} as package root", pack);
                initContext.addPackageRoot(pack.trim());
            }
        }
    }

    private UnitModule convertNativeModule(String pluginName, Object nativeUnitModule, boolean override)
    {
        DependencyInjectionProvider provider = findDependencyInjectionProvider(nativeUnitModule);
        if (provider != null)
        {
            UnitModule unitModule = provider.convert(nativeUnitModule);

            return unitModule;
        }
        else
        {
            logger.error("Kernel did not recognize module {} of plugin {}", nativeUnitModule, pluginName);
            throw new KernelException(
                    "Kernel did not recognize module %s of plugin %s. Please provide a DependencyInjectionProvider.", nativeUnitModule.toString(),
                    pluginName);
        }
    }

    private DependencyInjectionProvider findDependencyInjectionProvider(Object pluginDependencyInjectionDef)
    {
        DependencyInjectionProvider provider = null;
        providerLoop: for (DependencyInjectionProvider providerIt : globalDependencyInjectionProviders)
        {
            if (providerIt.canHandle(pluginDependencyInjectionDef.getClass()))
            {
                provider = providerIt;
                break providerLoop;
            }
        }
        return provider;
    }

    private ArrayList<Plugin> sortPlugins(ArrayList<Plugin> unsortedPlugins)
    {
        Graph graph = new Graph(unsortedPlugins.size());
        ArrayList<Plugin> sorted = new ArrayList<Plugin>();
        Map<Integer, Plugin> idxPlug = new HashMap<Integer, Plugin>();
        Map<Character, Plugin> charPlug = new HashMap<Character, Plugin>();
        Map<Plugin, Integer> plugIdx = new HashMap<Plugin, Integer>();
        Map<Class<? extends Plugin>, Integer> classPlugIdx = new HashMap<Class<? extends Plugin>, Integer>();

        // Add vertices
        for (short i = 0; i < unsortedPlugins.size(); i++)
        {

            char c = (char) i;
            Plugin unsortedPlugin = unsortedPlugins.get(i);
            Integer pluginIndex = graph.addVertex(c);

            charPlug.put(c, unsortedPlugin);
            idxPlug.put(pluginIndex, unsortedPlugin);
            plugIdx.put(unsortedPlugin, pluginIndex);
            classPlugIdx.put(unsortedPlugin.getClass(), pluginIndex);
        }

        // add edges
        for (Entry<Integer, Plugin> entry : idxPlug.entrySet())
        {
            Plugin source = entry.getValue();
            // based on required plugins
            for (Class<? extends Plugin> dependencyClass : source.requiredPlugins())
            {
                int start = classPlugIdx.get(dependencyClass);
                int end = plugIdx.get(source);
                graph.addEdge(start, end);
            }
            // based on dependent plugins
            for (Class<? extends Plugin> dependencyClass : source.dependentPlugins())
            {
                int start = plugIdx.get(source); // we inversed
                int end = classPlugIdx.get(dependencyClass);
                graph.addEdge(start, end);
            }
        }

        // launch the algo
        char[] topologicalSort = graph.topologicalSort();

        if (topologicalSort != null)
        {
            for (Character c : topologicalSort)
            {
                sorted.add(charPlug.get(c));
            }
        }
        else
        {
            throw new KernelException("Error when sorting plugins : either a Cycle in dependencies or another cause.");
        }

        return sorted;
    }

    private Collection<Plugin> filterPlugins(Collection<Plugin> collection, Collection<Class<? extends Plugin>> pluginDependenciesRequired)
    {
        Set<Plugin> filteredSet = new HashSet<Plugin>();

        for (Plugin plugin : collection)
        {
            if (pluginDependenciesRequired.contains(plugin.getClass()))
            {
                filteredSet.add(plugin);
            }
        }

        return filteredSet;
    }

    private InitContext proxyfy(final InitContext initContext, final Collection<Plugin> requiredPlugins, final Collection<Plugin> dependentPlugins)
    {
        return (InitContext) Proxy.newProxyInstance( //
                initContext.getClass().getClassLoader(), //
                new Class[] {
                    InitContext.class
                }, //
                new InvocationHandler()
                {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                    {
                        if (method.getName().equals("pluginsRequired"))
                        {
                            return requiredPlugins;
                        }
                        else if (method.getName().equals("dependentPlugins"))
                        {
                            return dependentPlugins;
                        }
                        else
                        {
                            return method.invoke(initContext, args);
                        }

                    }
                });
    }

    void createDependencyInjectionProvidersMap(Collection<Class<?>> dependencyInjectionProvidersClasses)
    {

        dependencyInjectionProviders = new HashSet<DependencyInjectionProvider>();

        for (Class<?> dependencyInjectionProviderClass : dependencyInjectionProvidersClasses)
        {
            DependencyInjectionProvider injectionDependencyProvider = newInstance(dependencyInjectionProviderClass);
            if (injectionDependencyProvider != null)
            {
                dependencyInjectionProviders.add(injectionDependencyProvider);
            }
            else
            {
                throw new KernelException("DependencyInjectionProvider %s can not be instanciated", (Object) dependencyInjectionProviderClass);
            }
        }
    }

    /**
     * You have only one chance to get the current kernel.
     * 
     * @param keyValues
     * @return
     */
    @Deprecated
    public synchronized static KernelBuilderWithPluginAndContext createKernel(String... keyValues)
    {
        return new KernelBuilderImpl(keyValues);
    }

    public static interface KernelBuilder
    {

        Kernel build();
    }

    public static interface KernelBuilderWithPluginAndContext extends KernelBuilderWithContainerContext, KernelBuilderWithPlugins, KernelModeContext
    {
    }

    public static interface KernelModeContext extends KernelBuilder
    {

        KernelBuilderWithPluginAndContext withDependencyInjectionMode(DependencyInjectionMode dependencyInjectionMode);

        KernelBuilderWithPluginAndContext withClasspathScanMode(ClasspathScanMode classpathScanMode, Object scanConfigurationObject);
    }

    public static interface KernelBuilderWithContainerContext extends KernelBuilder
    {

        KernelBuilderWithPlugins withContainerContext(Object containerContext);
    }

    public static interface KernelBuilderWithPlugins extends KernelBuilder
    {
        KernelBuilderWithPluginAndContext withPlugins(Class<? extends Plugin>... klass);

        KernelBuilderWithPluginAndContext withPlugins(Plugin... plugins);

        KernelBuilderWithPluginAndContext withoutSpiPluginsLoader();

    }

    private class ObjectGraphEmbedded implements ObjectGraph
    {

        private Object injector;

        public ObjectGraphEmbedded(Object injector)
        {
            this.injector = injector;
        }

        @Override
        public Object get()
        {
            return injector;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T as(Class<T> targetType)
        {
            if (targetType.equals(Injector.class))
            {
                return (T) Injector.class.cast(injector);
            }
            throw new IllegalStateException("Can not cast " + injector + " to " + targetType.getName());
        }

    }

    private static class KernelBuilderImpl implements KernelBuilderWithPluginAndContext
    {

        private KernelCore kernel;

        /**
         * 
         */
        public KernelBuilderImpl(String... keyValues)
        {

        }

        @Override
        public Kernel build()
        {
            return null;
        }

        @Override
        public KernelBuilderWithPlugins withContainerContext(Object containerContext)
        {

            kernel.addContainerContext(containerContext);
            return this;

        }

        @Override
        public KernelBuilderWithPluginAndContext withPlugins(java.lang.Class<? extends Plugin>... klass)
        {
            kernel.addPlugins(klass);
            return this;
        }

        @Override
        public KernelBuilderWithPluginAndContext withPlugins(Plugin... plugin)
        {
            kernel.addPlugins(plugin);
            return this;
        }

        @Override
        public KernelBuilderWithPluginAndContext withoutSpiPluginsLoader()
        {
            kernel.spiPluginDisabled();
            return this;
        }

        @Override
        public KernelBuilderWithPluginAndContext withDependencyInjectionMode(DependencyInjectionMode dependencyInjectionMode)
        {
            kernel.dependencyInjectionMode(dependencyInjectionMode);
            return this;
        }

        @Override
        public KernelBuilderWithPluginAndContext withClasspathScanMode(ClasspathScanMode classpathScanMode, Object scanConfigurationObject)
        {
            kernel.classpathScanMode(classpathScanMode);
            return this;
        }

    }

    @SuppressWarnings("unchecked")
    private <T> T newInstance(Class<?> klass)
    {
        T instance = null;

        try
        {
            instance = (T) klass.newInstance();
        }
        catch (InstantiationException e)
        {
            logger.error("Error when instantiating class " + klass, e);
        }
        catch (IllegalAccessException e)
        {
            logger.error("Error when instantiating class " + klass, e);
        }

        return instance;
    }

    void addContainerContext(Object containerContext)
    {
        this.containerContext = containerContext;

    }

    void spiPluginEnabled()
    {
        spiPluginEnabled = true;
    }

    void spiPluginDisabled()
    {
        spiPluginEnabled = false;
    }

    void dependencyInjectionMode(DependencyInjectionMode dependencyInjectionMode)
    {
        this.dependencyInjectionMode = dependencyInjectionMode;
    }

    void classpathScanMode(ClasspathScanMode classpathScanMode)
    {
        this.classpathScanMode = classpathScanMode;

        initContext.classpathScanMode(classpathScanMode);
    }

    /**
     * @param klass
     */
    void addPlugins(Class<? extends Plugin>... klass)
    {
        for (Class<? extends Plugin> class1 : klass)
        {

            Plugin plugin = newInstance(class1);
            if (plugin == null)
            {
                throw new KernelException("Plugin %s can not be instanciated", (Object) klass);
            }
            else
            {
                addPlugin(plugin);
            }
        }
    }

    void addPlugins(Plugin... plugins)
    {
        for (Plugin plugin : plugins)
        {
            addPlugin(plugin);
        }
    }

    void addPlugin(Plugin plugin)
    {
        if (!started)
        {
            pluginsToAdd.put(plugin.name(), plugin);
        }
        else
        {
            throw new KernelException("Plugin %s can not be added. Kernel already is started", plugin.name());
        }
    }

    AliasMap paramsAndAlias()
    {
        return kernelParamsAndAlias;
    }

    void provideGlobalDiDefValidation(ModuleValidation validation)
    {
        globalModuleValidations.add(validation);
    }

}
