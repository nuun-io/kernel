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
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.DependencyInjectionMode;
import io.nuun.kernel.api.di.GlobalModule;
import io.nuun.kernel.api.di.ObjectGraph;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.RoundInternal;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.injection.KernelGuiceModuleInternal;
import io.nuun.kernel.core.internal.injection.ModuleEmbedded;
import io.nuun.kernel.core.internal.injection.ModuleHandler;
import io.nuun.kernel.core.internal.injection.ObjectGraphEmbedded;
import io.nuun.kernel.spi.DependencyInjectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Epo Jemba
 */
public final class KernelCore implements Kernel
{
    private static AtomicInteger kernelIndex = new AtomicInteger();

    private final Logger logger;
    private final String name;
    private final KernelConfigurationInternal kernelConfig;
    private final ModuleHandler moduleHandler;
    private final RequestHandler requestHandler;
    private final PluginRegistry pluginRegistry = new PluginRegistry();

    private State state = State.NOT_INITIALIZED;
    private Injector mainInjector;
    private Module mainModule;
    private List<Plugin> orderedPlugins;
    private RoundInternal round;
    private ExtensionManager extensionManager;
    private DependencyProvider dependencyProvider;

    KernelCore(KernelConfigurationInternal kernelConfigurationInternal)
    {
        this.name = KERNEL_PREFIX_NAME + kernelIndex.getAndIncrement();
        this.logger = LoggerFactory.getLogger(KernelCore.class.getName() + ' ' + name());
        this.kernelConfig = kernelConfigurationInternal;
        this.requestHandler = new RequestHandler(kernelConfig.kernelParams().toMap(), kernelConfig.getClasspathScanMode());
        this.moduleHandler = new ModuleHandler(kernelConfig);
    }

    @Override
    public synchronized void init()
    {
        if (isInitialized())
        {
            throw new KernelException("Kernel is already initialized");
        }
        preparePlugins();
        validateMandatoryParams();
        fetchPackageRootsFromConfiguration();
        extensionManager = new ExtensionManager(pluginRegistry.getPlugins(), Thread.currentThread().getContextClassLoader());
        extensionManager.initializing();
        executeInitializationRounds();
        createMainModule();
        state = State.INITIALIZED;
        extensionManager.initialized();
    }

    public void preparePlugins()
    {
        addPluginsToTheRegistry();
        FacetRegistry facetRegistry = new FacetRegistry(pluginRegistry.getPlugins());
        dependencyProvider = new DependencyProvider(pluginRegistry, facetRegistry);

        round = new RoundInternal();
        DependenciesSpecification dependenciesSpecification = new DependenciesSpecification(facetRegistry);
        for (Plugin plugin : pluginRegistry.getPlugins())
        {
            plugin.provideRound(round);
            dependenciesSpecification.isSatisfyBy(plugin);
            addAliasesToKernelParams(plugin);
            fetchGlobalParametersFrom(plugin);
            addPackageRootsToRequestHandler(plugin.pluginPackageRoot());
        }

        sortPlugins(facetRegistry);
    }

    private void addPluginsToTheRegistry()
    {
        registerPluginsFromKernelConfiguration();
        if (kernelConfig.isPluginScanEnabled())
        {
            registerPluginsFromScan();
        }
    }

    private void registerPluginsFromKernelConfiguration()
    {
        for (Plugin plugin : kernelConfig.getPlugins())
        {
            pluginRegistry.add(plugin);
        }
        for (Class<? extends Plugin> pluginClass : kernelConfig.getPluginClasses())
        {
            pluginRegistry.add(pluginClass);
        }
    }

    private void registerPluginsFromScan()
    {
        for (Plugin plugin : ServiceLoader.load(Plugin.class, Thread.currentThread().getContextClassLoader()))
        {
            pluginRegistry.add(plugin);
        }
    }

    private void addAliasesToKernelParams(Plugin plugin)
    {
        for (Entry<String, String> entry : plugin.kernelParametersAliases().entrySet())
        {
            String alias = entry.getKey();
            String keyToAlias = entry.getValue();
            logger.info("Adding alias parameter \"{}\" to key \"{}\".", keyToAlias, alias);
            kernelConfig.kernelParams().putAlias(alias, keyToAlias);
        }
    }

    private void fetchGlobalParametersFrom(Plugin plugin)
    {
        // Constants from plugin outside rounds
        // We pass the container context object for plugin
        plugin.provideContainerContext(kernelConfig.getContainerContext());

        Set<URL> computeAdditionalClasspathScan = plugin.computeAdditionalClasspathScan();
        if (computeAdditionalClasspathScan != null && computeAdditionalClasspathScan.size() > 0)
        {
            for (URL url : computeAdditionalClasspathScan)
            {
                if (url != null)
                {
                    requestHandler.addClasspathToScan(url);
                    logger.debug("Plugin {} add classpath to scan: {}", plugin.name(), url.toExternalForm());
                }
            }
        }
        // Convert dependency manager classes to instances //
        DependencyInjectionProvider iocProvider = plugin.dependencyInjectionProvider();
        if (iocProvider != null)
        {
            moduleHandler.addDependencyInjectionProvider(iocProvider);
        }
    }

    private void sortPlugins(FacetRegistry facetRegistry)
    {
        ArrayList<Plugin> unOrderedPlugins = new ArrayList<Plugin>(pluginRegistry.getPlugins());
        logger.trace("unordered plugins: ({}) {}", unOrderedPlugins.size(), unOrderedPlugins);
        orderedPlugins = new PluginSortStrategy(facetRegistry, unOrderedPlugins).sortPlugins();
        logger.trace("ordered plugins: ({}) {}", orderedPlugins.size(), orderedPlugins);
    }

    private void fetchPackageRootsFromConfiguration()
    {
        for (String rootPackage : kernelConfig.getRootPackages())
        {
            requestHandler.addRootPackage(rootPackage);
        }

        if (kernelConfig.kernelParams().containsKey(NUUN_ROOT_PACKAGE))
        {
            String rootPackages = kernelConfig.kernelParams().get(NUUN_ROOT_PACKAGE);
            addPackageRootsToRequestHandler(rootPackages);
        }
    }

    private void addPackageRootsToRequestHandler(String pluginPackageRoots)
    {
        if (!Strings.isNullOrEmpty(pluginPackageRoots))
        {
            for (String pack : pluginPackageRoots.split(","))
            {
                logger.info("Adding {} as package root", pack);
                requestHandler.addRootPackage(pack.trim());
            }
        }
    }

    private void validateMandatoryParams() {
        MandatoryParamsSpecification mandatoryParamsSpecification = new MandatoryParamsSpecification();
        for (Plugin plugin : pluginRegistry.getPlugins()) {
            mandatoryParamsSpecification.isSatisfiedBy(plugin, kernelConfig.kernelParams());
        }
    }

    private void executeInitializationRounds()
    {
        logger.info("Initializing");
        List<Plugin> nonInitializedPlugins = orderedPlugins;
        while (allThePluginsAreNotInitialized(nonInitializedPlugins))
        {
            logger.info("Round #{}", round.number());
            requestHandler.registerRequests(nonInitializedPlugins);
            requestHandler.executeRequests();
            nonInitializedPlugins = callPluginsInitMethod(nonInitializedPlugins, round.number());
            round.next();
        }
    }

    private boolean allThePluginsAreNotInitialized(List<Plugin> pluginsInTheRound)
    {
        return !pluginsInTheRound.isEmpty() && !round.isMax();
    }

    private List<Plugin> callPluginsInitMethod(final List<Plugin> plugins, int round)
    {
        List<Plugin> nonInitializedPlugins = new ArrayList<Plugin>();
        for (Plugin plugin : plugins)
        {
            logger.info(" * {} plugin", plugin.name());
            InitContext initContext = new InitContextInternal(kernelConfig.kernelParams().toMap(), requestHandler, round, dependencyProvider, plugin.getClass());
            if (plugin.init(initContext) != InitState.INITIALIZED)
            {
                nonInitializedPlugins.add(plugin);
            }
        }
        return nonInitializedPlugins;
    }

    /**
     * This methods will create both Global ModuleProviders : nominal and overriding.
     */
    private void createMainModule()
    {
        for (Plugin plugin : orderedPlugins)
        {
            moduleHandler.handleUnitModule(requestHandler, plugin);
            moduleHandler.handleOverridingUnitModule(requestHandler, plugin);
        }
        KernelGuiceModuleInternal kernelGuiceModuleInternal = new KernelGuiceModuleInternal(requestHandler);
        KernelGuiceModuleInternal internalKernelGuiceModuleOverriding = new KernelGuiceModuleInternal(requestHandler).overriding();
        mainModule = Modules.override(kernelGuiceModuleInternal).with(internalKernelGuiceModuleOverriding);
    }

    @Override
    public synchronized void start()
    {
        if (!isInitialized())
        {
            throw new KernelException("Kernel is not initialized.");
        }
        extensionManager.starting();
        createMainInjector();
        bindAndStartPlugins();
        state = State.STARTED;
        extensionManager.started();
    }

    private void createMainInjector()
    {
        Stage stage = convertInjectionModeToGuiceStage(kernelConfig.getDependencyInjectionMode());
        mainInjector = Guice.createInjector(stage, mainModule);
    }

    private void bindAndStartPlugins()
    {
        Context context = mainInjector.getInstance(Context.class);
        for (Plugin plugin : orderedPlugins)
        {
            mainInjector.injectMembers(plugin);
            plugin.start(context);
        }
    }

    private Stage convertInjectionModeToGuiceStage(DependencyInjectionMode dependencyInjectionMode)
    {
        Stage stage;
        switch (dependencyInjectionMode)
        {
            case PRODUCTION:
                stage = Stage.PRODUCTION;
                break;
            case DEVELOPMENT:
                stage = Stage.DEVELOPMENT;
                break;
            case TOOL:
                stage = Stage.TOOL;
                break;
            default:
                stage = Stage.PRODUCTION;
                break;
        }
        return stage;
    }

    @Override
    public void stop()
    {
        if (isStarted())
        {
            extensionManager.stopping();
            stopPluginsInReverseOrder();
            extensionManager.stopped();
            state = State.STOPPED;
        } else
        {
            throw new KernelException("Kernel is not started.");
        }
    }

    private void stopPluginsInReverseOrder()
    {
        ListIterator<Plugin> li = orderedPlugins.listIterator(orderedPlugins.size());
        while (li.hasPrevious())
        {
            Plugin plugin = li.previous();
            plugin.stop();
        }
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public boolean isStarted()
    {
        return state == State.STARTED;
    }

    @Override
    public boolean isInitialized()
    {
        return state == State.INITIALIZED;
    }

    @Override
    public ObjectGraph objectGraph()
    {
        return new ObjectGraphEmbedded(mainInjector);
    }

    @Override
    public UnitModule unitModule(Class<? extends Plugin> pluginClass)
    {
        return moduleHandler.getUnitModules().get(pluginClass);
    }

    @Override
    public UnitModule overridingUnitModule(Class<? extends Plugin> pluginClass)
    {
        return moduleHandler.getOverridingUnitModules().get(pluginClass);
    }

    @Override
    public UnitModule nonGuiceUnitModule(Class<? extends Plugin> plugin)
    {
        return moduleHandler.getNonGuiceUnitModules().get(plugin);
    }

    @Override
    public UnitModule nonGuiceOverridingUnitModule(Class<? extends Plugin> plugin)
    {
        return moduleHandler.getNonGuiceOverridingUnitModules().get(plugin);
    }

    @Override
    public GlobalModule globalModule()
    {
        return GlobalModule.class.cast(new ModuleEmbedded(mainModule));
    }

    @Override
    public Map<String, Plugin> plugins()
    {
        return this.pluginRegistry.getPluginsByName();
    }
}
