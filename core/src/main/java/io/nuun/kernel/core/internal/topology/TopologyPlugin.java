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
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.spi.topology.Binding;
import io.nuun.kernel.spi.topology.InstanceBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;
import io.nuun.kernel.spi.topology.TopologyDefinition;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopologyPlugin extends AbstractPlugin
{

    private static Logger      logger             = LoggerFactory.getLogger(TopologyPlugin.class);

    private TopologyDefinition topologyDefinition = new TopologyDefinitionCore();

    private List<Binding>      bindings;

    private TopologyModule     topologyModule;

    @Override
    public String name()
    {
        return "topology-plugin";
    }

    @Override
    public InitState init(InitContext initContext)
    {

        bindings = new ArrayList<Binding>();

        Map<Predicate<Class<?>>, Collection<Class<?>>> typesByPredicate = initContext.scannedTypesByPredicate();

        Collection<Class<?>> topologiesClasses = typesByPredicate.get(TopologyPredicate.INSTANCE);

        topologiesClasses.stream().forEach(c -> asList(c.getDeclaredFields()).stream().forEach(this::treatMember));

        topologiesClasses.stream().forEach(c -> asList(c.getDeclaredMethods()).stream().forEach(this::treatMember));

        return InitState.INITIALIZED;
    }

    private void treatMember(Member m)
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
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        return classpathScanRequestBuilder().predicate(TopologyPredicate.INSTANCE).build();
    }

    @Override
    public Object nativeUnitModule()
    {
        if (topologyModule == null)
        {
            topologyModule = new TopologyModule(bindings);
        }
        return topologyModule;
    }

}
