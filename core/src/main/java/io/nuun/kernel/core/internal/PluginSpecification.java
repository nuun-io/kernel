package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequestType;
import io.nuun.kernel.core.KernelException;

import java.util.*;

class PluginSpecification {

    PluginRegistry isSatisfyBy(final PluginRegistry pluginRegistry, final AliasMap kernelParams) {
        // Check mandatory params
        for (Plugin plugin : pluginRegistry.getPlugins()) {
            checkMandatoryParams(plugin, kernelParams);
        }

        // Check for required and dependent plugins
        for (Plugin plugin : pluginRegistry.getPlugins()) {
            assertDependenciesArePresent(plugin, pluginRegistry.getPluginClasses());
        }
        return pluginRegistry;
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
     * @param plugin           the plugin to check
     * @param availablePlugins the list of available plugins
     */
    private void assertDependenciesArePresent(Plugin plugin, Collection<Class<? extends Plugin>> availablePlugins) {
        Collection<Class<?>> requiredPlugins = plugin.requiredPlugins();

        if (requiredPlugins != null && !requiredPlugins.isEmpty() && !availablePlugins.containsAll(requiredPlugins)) {
            throw new KernelException("Plugin %s misses the following plugin/s as dependency/ies %s", plugin.name(), requiredPlugins.toString());
        }

        Collection<Class<?>> dependentPlugins = plugin.dependentPlugins();

        if (dependentPlugins != null && !dependentPlugins.isEmpty() && !availablePlugins.containsAll(dependentPlugins)) {
            throw new KernelException("Plugin %s misses the following plugin/s as dependee/s %s", plugin.name(), dependentPlugins.toString());
        }
    }
}