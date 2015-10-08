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
    Collection<Class<? extends Plugin>> requiredPlugins();

    /**
     * List of plugins that become dependent on "this" plugin.
     * The plugin's init phase will be executed after these dependencies.
     * 
     * @return the dependent plugin classes
     */
    Collection<Class<? extends Plugin>> dependentPlugins();

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
    String pluginPackageRoot();

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
     * Round Environment provides information regarding the current round to the plugin.
     * The kernel pass this object to the plugin before all
     * 
     * @param round the round
     */
    void provideRoundEnvironment(Round round);

}
