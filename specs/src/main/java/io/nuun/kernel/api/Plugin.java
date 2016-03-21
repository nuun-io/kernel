/**
 * This file is part of Nuun IO Kernel Specs.
 *
 * Nuun IO Kernel Specs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Specs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Specs.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.api;

import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.Round;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.spi.DependencyInjectionProvider;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Epo Jemba
 */
public interface Plugin
{
    /**
     * Lifecycle method: init()
     */
    InitState init(InitContext initContext);

    /**
     * Lifecycle method: start()
     */
    void start(Context context);

    /**
     * Lifecycle method: stop()
     */
    void stop();

    /**
     * The name of the plugin. Plugin won't be installed, if there is no a name. And if this name is not
     * unique. Mandatory.
     * 
     * @return the plugin name.
     */
    String name();

    /**
     * The description of the plugin.
     * 
     * @return the description
     */
    String description();

    /* ============================= PLUGIN info and requests ============================= * */

    /**
     * list of kernel params needed by this plugins, required by this plugin.
     * 
     * @return the kernel params requests
     */
    Collection<KernelParamsRequest> kernelParamsRequests();

    /**
     * List of classpath request needed by this plugin.
     * 
     * @return the classpath scan requests
     */
    Collection<ClasspathScanRequest> classpathScanRequests();

    /**
     * List of bind request.
     * 
     * @return the binding requests
     */
    Collection<BindingRequest> bindingRequests();

    /**
     * List of plugins dependencies required by this plugin.
     * The plugin's init phase will be executed after these dependencies.
     * 
     * @return the required plugin classes
     */
    Collection<Class<?>> requiredPlugins();

    /**
     * List of plugins that become dependent on "this" plugin.
     * The plugin's init phase will be executed after these dependencies.
     * 
     * @return the dependent plugin classes
     */
    Collection<Class<?>> dependentPlugins();

    /**
     * The prefix for all the properties for this plugin.
     * 
     * @return the properties prefix
     */
    String pluginPropertiesPrefix();

    /**
     * The package root(s) from where the nuun core will scan for annotation. It is possible
     * to specify multiple package roots by separating them by a comma.
     * 
     * @return the package roots
     */
    @Deprecated
    String pluginPackageRoot();

    /**
     * The root package(s) use to filter the classpath scanning. It is possible
     * to specify multiple root packages by separating them by a comma.
     *
     * @return the package roots
     */
    String rootPackages();

    /**
     * Return an object that will contains the dependency injection definitions. Mostly a Guice module but
     * it can be other dependency injection object from other DI frameworks : Spring, Tapestry, Jodd, etc.
     * The kernel must have a {@link DependencyInjectionProvider} that handle it.
     * 
     * @return the unit module
     */
    UnitModule unitModule();

    /**
     * This object will contains bindings definition that will override the main dependency ones.
     * It can serve as test bindings replacement, environment definition.
     * Those can be also handled by a {@link DependencyInjectionProvider} if keys match.
     * 
     * @return the unit module
     */
    UnitModule overridingUnitModule();

    /**
     * Practical method to retrieve the container context as it is passed as argument.
     * @param containerContext the context of the container
     */
    void provideContainerContext(Object containerContext);
    
    /**
     * The kernel allows the plugin to compute additional classpath to scan. Method can use the containerContext - that
     * may be a ServletContext for servlet environment, BundleContext for OSGI environment or something
     * else - given to the plugin via method provideContainerContext.
     * 
     * @return urls to scan
     */
    Set<URL> computeAdditionalClasspathScan();

    /**
     * Returns Dependency injection provider to the kernel.
     * 
     * @return a DependencyInjectionProvider
     */
    DependencyInjectionProvider dependencyInjectionProvider();

    /**
     * Returns a map which contains:
     * <pre>
     * - key :the alias to create.
     * - value :  kernel parameter to alias.
     * </pre>
     */
    Map<String, String> kernelParametersAliases();
    
    /**
     * The {@link io.nuun.kernel.api.plugin.Round} provides information regarding the current round to the plugin.
     * The method is called by the kernel.
     * 
     * @param round the round
     */
    void provideRound(Round round);

}
