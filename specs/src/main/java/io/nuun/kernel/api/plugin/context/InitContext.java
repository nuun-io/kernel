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
package io.nuun.kernel.api.plugin.context;

import io.nuun.kernel.api.di.UnitModule;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The holder class containing all the data available at the {@code init} step.
 *
 * @author Epo Jemba
 */
public interface InitContext
{
    Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass();

    Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex();

    Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass();

    Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex();

    Map<String, Collection<String>> mapPropertiesFilesByPrefix();

    Map<String, String> kernelParams();

    String kernelParam(String key);

    Collection<Class<?>> classesToBind();

    List<UnitModule> moduleResults();

    List<UnitModule> moduleOverridingResults();

    Collection<String> propertiesFiles();

    Map<String, Collection<Class<?>>> scannedTypesByRegex();

    Map<String, Collection<String>> mapResourcesByRegex();

    Map<Predicate<Class<?>>, Collection<Class<?>>> scannedTypesByPredicate();

    /**
     * Returns plugin instances required by the current plugin.
     * The plugin's init phase will be executed after theirs.
     *
     * @return the instances of the plugin declared required by the method Plugin.pluginDependenciesRequired()
     */
    @Deprecated
    Collection<?> pluginsRequired();

    /**
     * Returns instances of the plugins that become dependent on this plugin.
     * The plugin's init phase will be executed before theirs.
     *
     * @return dependent plugins
     */
    @Deprecated
    Collection<?> dependentPlugins();

    List<?> dependencies();

    <T> List<T> dependencies(Class<T> dependencyClass);

    <T> T dependency(Class<T> dependencyClass);

    /**
     * @return the current round number
     */
    int roundNumber();
}
