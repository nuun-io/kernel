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

class DependenciesAsserter
{

    private final FacetRegistry facetRegistry;

    DependenciesAsserter(FacetRegistry facetRegistry)
    {
        this.facetRegistry = facetRegistry;
    }

    /**
     * Verifies that the plugin dependencies are present in the plugin list.
     * The list should contains both dependent and required plugins.
     *
     * @param plugin the plugin to check
     */
    void assertDependencies(final Plugin plugin)
    {
        Collection<Class<?>> expectedDependencies = new ArrayList<>();

        Collection<Class<?>> requiredPlugins = plugin.requiredPlugins();
        if (requiredPlugins != null)
        {
            expectedDependencies.addAll(requiredPlugins);
        }

        Collection<Class<?>> dependentPlugins = plugin.dependentPlugins();
        if (dependentPlugins != null)
        {
            expectedDependencies.addAll(dependentPlugins);
        }

        for (Class<?> dependencyClass : expectedDependencies)
        {
            List<?> facets = facetRegistry.getFacets(dependencyClass);

            if (facets.isEmpty())
            {
                throw new KernelException("Plugin %s misses the following dependency: %s", plugin.name(), dependencyClass.getCanonicalName());
            }
        }
    }
}