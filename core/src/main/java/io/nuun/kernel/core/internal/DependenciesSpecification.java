package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.KernelException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class DependenciesSpecification
{

    private final FacetRegistry facetRegistry;

    DependenciesSpecification(FacetRegistry facetRegistry)
    {
        this.facetRegistry = facetRegistry;
    }

    /**
     * Verifies that the plugin dependencies are present in the plugin list.
     * The list should contains both dependent and required plugins.
     *
     * @param plugin the plugin to check
     */
    void isSatisfyBy(final Plugin plugin)
    {
        Collection<Class<?>> expectedDependencies = new ArrayList<Class<?>>();

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