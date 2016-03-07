package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequestType;
import io.nuun.kernel.core.KernelException;

import java.util.Collection;
import java.util.HashSet;

public class MandatoryParamsSpecification
{
    public void isSatisfiedBy(Plugin plugin, AliasMap kernelParams) {
        Collection<KernelParamsRequest> requestedParams = plugin.kernelParamsRequests();
        Collection<String> mandatoryParams = filterMandatoryParams(requestedParams);

        if (!kernelParams.containsAllKeys(mandatoryParams)) {
            throw new KernelException("Plugin " + plugin.name() + " misses parameter/s : " + requestedParams.toString());
        }
    }

    private Collection<String> filterMandatoryParams(Collection<KernelParamsRequest> kernelParamsRequests)
    {
        Collection<String> computedMandatoryParams = new HashSet<String>();
        for (KernelParamsRequest kernelParamsRequest : kernelParamsRequests) {
            if (kernelParamsRequest.requestType == KernelParamsRequestType.MANDATORY) {
                computedMandatoryParams.add(kernelParamsRequest.keyRequested);
            }
        }
        return computedMandatoryParams;
    }
}
