package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequestType;
import io.nuun.kernel.core.KernelException;

import java.util.*;

class PluginSpecification {

    private final FacetRegistry facetRegistry;

    PluginSpecification(FacetRegistry facetRegistry) {
        this.facetRegistry = facetRegistry;
    }

    void isSatisfyBy(final Plugin plugin, final AliasMap kernelParams) {
        // Check mandatory params
        checkMandatoryParams(plugin, kernelParams);

        // Check for required and dependent plugins
        assertDependenciesArePresent(plugin);
    }

    private void checkMandatoryParams(Plugin plugin, AliasMap kernelParams) {
        Collection<KernelParamsRequest> kernelParamsRequests = plugin.kernelParamsRequests();
        Collection<String> computedMandatoryParams = new HashSet<String>();

        for (KernelParamsRequest kernelParamsRequest : kernelParamsRequests) {
            if (kernelParamsRequest.requestType == KernelParamsRequestType.MANDATORY) {
                computedMandatoryParams.add(kernelParamsRequest.keyRequested);
            }
        }

        if (!kernelParams.containsAllKeys(computedMandatoryParams)) {
            throw new KernelException("Plugin " + plugin.name() + " misses parameter/s : " + kernelParamsRequests.toString());
        }
    }

    /**
     * Verifies that the plugin dependencies are present in the plugin list.
     * The list should contains both dependent and required plugins.
     *
     * @param plugin the plugin to check
     */
    private void assertDependenciesArePresent(Plugin plugin) {
        Collection<Class<?>> expectedDependency = new ArrayList<Class<?>>();

        Collection<Class<?>> requiredPlugins = plugin.requiredPlugins();
        if (requiredPlugins != null) {
            expectedDependency.addAll(requiredPlugins);
        }

        Collection<Class<?>> dependentPlugins = plugin.dependentPlugins();
        if (dependentPlugins != null) {
            expectedDependency.addAll(dependentPlugins);
        }

        for (Class<?> dependencyClass : expectedDependency) {
            List<?> facets = facetRegistry.getFacets(dependencyClass);

            if (facets.isEmpty()) {
                throw new KernelException("Plugin %s misses the following dependency: %s", plugin.name(), dependencyClass.getCanonicalName());
            }
        }
    }
}