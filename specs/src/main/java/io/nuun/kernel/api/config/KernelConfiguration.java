/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
 * <p/>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nuun.kernel.api.config;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.di.ModuleValidation;

/**
 * Kernel configuration is used as an helper object to instantiate a Nuun kernel.
 */
public interface KernelConfiguration
{
    <T> KernelConfiguration option(KernelOption<T> option, T value);

    /**
     * Sets the root packages. By default if no root package is set the scanner will scan all the classpath.
     *
     * @param rootPackages the list of packages to scan
     * @return itself
     */
    KernelConfiguration rootPackages(String... rootPackages);

    /**
     * Sets a key/value parameter.
     *
     * @param key the key
     * @param value the value
     * @return itself
     */
    KernelConfiguration param(String key, String value);

    /**
     * Sets parameters entries as a sequence of key value pairs.
     *
     * @param paramEntries parameters
     * @return itself
     */
    KernelConfiguration params(String... paramEntries);

    /**
     * Sets the container context which will be passed to plugins.
     *
     * @param containerContext the container context
     * @return itself
     */
    KernelConfiguration containerContext(Object containerContext);

    /**
     * Specifies a plugin to be used by the Nuun kernel.
     *
     * @param pluginsClass the plugin class
     * @return itself
     */
    KernelConfiguration addPlugin(Class<? extends Plugin> pluginsClass);

    /**
     * Specifies the plugins to be used by the Nuun kernel.
     *
     * @param pluginsClasses the plugin classes
     * @return itself
     */
    KernelConfiguration plugins(Class<? extends Plugin>... pluginsClasses);

    /**
     * Specifies a plugin to be used by the Nuun kernel.
     *
     * @param plugins the plugin instance
     * @return itself
     */
    KernelConfiguration addPlugin(Plugin plugins);

    /**
     * Specifies the plugins to be used by the Nuun kernel.
     *
     * @param plugins the plugin instances
     * @return itself
     */
    KernelConfiguration plugins(Plugin... plugins);

    /**
     * Disables the plugin scan via the service loader.
     * <p>
     * This allows to only use the plugins explicitly specified in the kernel configuration.
     * </p>
     * @return itself
     */
    KernelConfiguration withoutSpiPluginsLoader();

    /**
     * Enables the plugin scan via the service loader.
     * <p>
     * All the plugin declared in {@code META-INF/services} will use by the Nuun kernel.
     * </p>
     * @return itself
     */
    KernelConfiguration withSpiPluginsLoader();

    /**
     * Sets the dependency injection mode.
     *
     * @param mode the mode to use
     * @see io.nuun.kernel.api.config.DependencyInjectionMode
     * @return itself
     */
    KernelConfiguration dependencyInjectionMode(DependencyInjectionMode mode);

    /**
     * Sets the classpath scan mode.
     *
     * @param mode the mode to use
     * @see io.nuun.kernel.api.config.ClasspathScanMode
     * @return itself
     */
    KernelConfiguration classpathScanMode(ClasspathScanMode mode);

    /**
     * Sets a {@link io.nuun.kernel.api.di.ModuleValidation} which will be used
     * to assert {@link io.nuun.kernel.api.di.UnitModule} given by the plugins.
     *
     * @param validation the module validation
     * @return itself
     */
    KernelConfiguration moduleValidation(ModuleValidation validation);

}
