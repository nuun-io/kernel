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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.spi.topology.TopologyDefinition;
import io.nuun.kernel.spi.topology.binding.Binding;

public class TopologyPlugin extends AbstractPlugin
{

    private static Logger      logger             = LoggerFactory.getLogger(TopologyPlugin.class);

    private TopologyDefinition topologyDefinition = new TopologyDefinitionCore();

    private List<Binding>      bindings;
    private List<Binding>      overridingBindings;

    private TopologyModule     topologyModule;
    private TopologyModule     overridingTopologyModule;

    @Override
    public String name()
    {
        return "topology-plugin";
    }

    @Override
    public InitState init(InitContext initContext)
    {

        bindings = new ArrayList<>();
        overridingBindings = new ArrayList<>();

        Map<Predicate<Class<?>>, Collection<Class<?>>> typesByPredicate = initContext.scannedTypesByPredicate();

        Collection<Class<?>> topologiesClasses = typesByPredicate.get(TopologyPredicate.INSTANCE);

        TopologyAnalyzer analyzer = new TopologyAnalyzer(topologyDefinition, bindings);
        TopologyAnalyzer overridingAnalyzer = new TopologyAnalyzer(topologyDefinition, overridingBindings);

        Collection<Class<?>> nominal = topologiesClasses.stream().filter(OverridingTopologyPredicate.INSTANCE.negate()).collect(Collectors.toList());
        Collection<Class<?>> overrideList = topologiesClasses.stream().filter(OverridingTopologyPredicate.INSTANCE).collect(Collectors.toList());

        analyzer.analyze(nominal);
        overridingAnalyzer.analyze(overrideList);

        return InitState.INITIALIZED;
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

    @Override
    public Object nativeOverridingUnitModule()
    {
        if (overridingTopologyModule == null)
        {
            overridingTopologyModule = new TopologyModule(overridingBindings);
        }

        return overridingTopologyModule;

    }
}
