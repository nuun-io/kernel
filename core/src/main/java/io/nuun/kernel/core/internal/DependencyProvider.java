/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.KernelException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class allows to retrieve a plugin's required or dependent dependencies.
 * A dependency can be a {@link io.nuun.kernel.api.annotations.Facet} interface or a
 * {@link io.nuun.kernel.api.Plugin} class.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
class DependencyProvider {

    private final PluginRegistry pluginRegistry;
    private final FacetRegistry facetRegistry;

    private enum DependencyType {
        REQUIRED, DEPENDENT, ALL
    }

    public DependencyProvider(PluginRegistry pluginRegistry, FacetRegistry facetRegistry) {
        this.pluginRegistry = pluginRegistry;
        this.facetRegistry = facetRegistry;
    }

    public List<Plugin> getDependenciesOf(Class<? extends Plugin> pluginClass) {
        return getDependenciesOf(pluginClass, DependencyType.ALL);
    }

    public List<Plugin> getRequiredPluginsOf(Class<? extends Plugin> pluginClass) {
        return getDependenciesOf(pluginClass, DependencyType.REQUIRED);
    }

    public List<Plugin> getDependentPluginsOf(Class<? extends Plugin> pluginClass) {
        return getDependenciesOf(pluginClass, DependencyType.DEPENDENT);
    }

    private List<Plugin> getDependenciesOf(Class<? extends Plugin> pluginClass, DependencyType dependencyType) {
        Plugin plugin = pluginRegistry.get(pluginClass);
        List<Plugin> dependencies = new ArrayList<Plugin>();
        if (plugin != null) {
            for (Class<?> requiredClass : getDependencyClasses(plugin, dependencyType)) {
                //noinspection unchecked
                dependencies.addAll((Collection<? extends Plugin>) facetRegistry.getFacets(requiredClass));
            }
        }
        return dependencies;
    }

    private Collection<Class<?>> getDependencyClasses(Plugin plugin, DependencyType dependencyType) {
        Collection<Class<?>> requiredClasses = new ArrayList<Class<?>>();
        if (dependencyType == DependencyType.REQUIRED) {
            requiredClasses = plugin.requiredPlugins();
        } else if (dependencyType == DependencyType.DEPENDENT) {
            requiredClasses = plugin.dependentPlugins();
        } else if (dependencyType == DependencyType.ALL) {
            requiredClasses = plugin.requiredPlugins();
            requiredClasses.addAll(plugin.dependentPlugins());
        }
        return requiredClasses;
    }

    public <T> List<T> getFacets(Class<? extends Plugin> pluginClass, Class<T> facet) {
        assertExplicitDependency(pluginClass, facet);
        return facetRegistry.getFacets(facet);
    }

    public <T> T getFacet(Class<? extends Plugin> pluginClass, Class<T> facet) {
        assertExplicitDependency(pluginClass, facet);
        return facetRegistry.getFacet(facet);
    }

    private <T> void assertExplicitDependency(Class<? extends Plugin> pluginClass, Class<T> facet) {
        Plugin plugin = pluginRegistry.get(pluginClass);
        if (!plugin.requiredPlugins().contains(facet) && !plugin.dependentPlugins().contains(facet)) {
            throw new KernelException("The plugin " + pluginClass.getCanonicalName() + " doesn't specify a dependency with " + facet.getCanonicalName());
        }
    }
}
