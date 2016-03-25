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
