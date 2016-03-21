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
