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
package io.nuun.kernel.core.internal.topology;

import static java.util.Arrays.asList;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.spi.topology.Binding;
import io.nuun.kernel.spi.topology.InstanceBinding;
import io.nuun.kernel.spi.topology.InterceptorBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;
import io.nuun.kernel.spi.topology.ProviderBinding;
import io.nuun.kernel.spi.topology.TopologyDefinition;

import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

class TopologyAnalyzer
{

    private List<Binding>      bindings;
    private TopologyDefinition topologyDefinition;

    public TopologyAnalyzer(TopologyDefinition topologyDefinition, List<Binding> bindings)
    {
        this.topologyDefinition = topologyDefinition;
        this.bindings = bindings;
    }

    public void analyze(Collection<Class<?>> topologiesClasses)
    {

        topologiesClasses.stream().peek(this::treatUnit).forEach(c -> asList(c.getDeclaredFields()).stream().forEach(this::treatMember));

        topologiesClasses.stream().forEach(c -> asList(c.getDeclaredMethods()).stream().forEach(this::treatMember));

    }

    void treatUnit(Class<?> unit)
    {
        if (!unit.isInterface())
        {
            throw new KernelException("Topology : %s must be an interface to be a valid topology.", unit.getName());
        }
    }

    void treatMember(Member m)
    {
        // Instance Binding
        Optional<InstanceBinding> instanceBinding = topologyDefinition.instanceBinding(m);
        if (instanceBinding.isPresent())
        {
            bindings.add(instanceBinding.get());
        }

        // Linked Binding
        Optional<LinkedBinding> linkedBinding = topologyDefinition.linkedBinding(m);
        if (linkedBinding.isPresent())
        {
            bindings.add(linkedBinding.get());
        }

        // Provider Binding
        Optional<ProviderBinding> providerBinding = topologyDefinition.providerBinding(m);
        if (providerBinding.isPresent())
        {
            bindings.add(providerBinding.get());
        }

        // Interceptor Binding
        Optional<InterceptorBinding> interceptorBinding = topologyDefinition.interceptorBinding(m);
        if (interceptorBinding.isPresent())
        {
            bindings.add(interceptorBinding.get());
        }
    }

}
