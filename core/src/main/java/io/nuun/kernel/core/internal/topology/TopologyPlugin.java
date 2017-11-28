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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.spi.topology.TopologyDefinition;
import io.nuun.kernel.spi.topology.binding.Binding;
import io.nuun.kernel.spi.topology.binding.InjectionBinding;
import io.nuun.kernel.spi.topology.binding.MultiBinding;
import io.nuun.kernel.spi.topology.binding.MultiBinding.MultiKind;
import io.nuun.kernel.spi.topology.binding.NullableBinding;

public class TopologyPlugin extends AbstractPlugin
{

    private static Logger            logger                    = LoggerFactory.getLogger(TopologyPlugin.class);

    private TopologyDefinition       topologyDefinition        = new TopologyDefinitionCore();

    private List<Binding>            bindings;
    private List<Binding>            overridingBindings;
    private Set<MultiBinding>        multiBindings;
    private Set<MultiBinding>        overridingMultiBindings;

    private BindingInfos             bindingInfos              = new BindingInfos();
    private List<Key<?>>             nullableKeys;
    private List<Key<?>>             keys;
    private List<Key<?>>             optionalKeys;

    private TopologyModule           topologyModule;
    private TopologyModule           overridingTopologyModule;

    private Set<Predicate<Class<?>>> multiPredicates           = new HashSet<>();
    private Set<Predicate<Class<?>>> overridingMultiPredicates = new HashSet<>();

    @Override
    public String name()
    {
        return "topology-plugin";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        if (this.round.isFirst())
        {
            logger.debug("First Round.");
            return classpathScanRequestBuilder().predicate(TopologyPredicate.INSTANCE).build();
        }
        else // second round if any
        {
            logger.debug("Second Round.");
            ClasspathScanRequestBuilder builder = classpathScanRequestBuilder();
            for (MultiBinding mb : multiBindings)
            {
                // construct classpath scan builder with a predicate
                if (mb.kind != MultiKind.NONE)
                {
                    builder = builder.predicate(predicateFromMultiBinding(mb));
                }
            }
            for (MultiBinding mb : overridingMultiBindings)
            {
                // construct classpath scan builder with a predicate
                if (mb.kind != MultiKind.NONE)
                {
                    builder = builder.predicate(predicateFromOverridingMultiBinding(mb));
                }
            }
            return builder.build();
        }
    }

    @Override
    public InitState init(InitContext initContext)
    {
        Map<Predicate<Class<?>>, Collection<Class<?>>> typesByPredicate = initContext.scannedTypesByPredicate();

        if (this.round.isFirst())
        {
            // initialization
            bindings = new ArrayList<>();
            overridingBindings = new ArrayList<>();
            multiBindings = new HashSet<>();
            overridingMultiBindings = new HashSet<>();
            nullableKeys = new ArrayList<>();
            optionalKeys = new ArrayList<>();
            keys = new ArrayList<>();

            // Get classes from kernel
            Collection<Class<?>> topologiesClasses = typesByPredicate.get(TopologyPredicate.INSTANCE);

            // Topologie analyzer
            TopologyAnalyzer analyzer = new TopologyAnalyzer(topologyDefinition, bindings);
            TopologyAnalyzer overridingAnalyzer = new TopologyAnalyzer(topologyDefinition, overridingBindings);

            // Separate Nominal Topologies from Overriding ones
            Collection<Class<?>> nominalList = topologiesClasses.stream().filter(OverridingTopologyPredicate.INSTANCE.negate()).collect(Collectors.toList());
            Collection<Class<?>> overrideList = topologiesClasses.stream().filter(OverridingTopologyPredicate.INSTANCE).collect(Collectors.toList());

            // Do the Analyze topology classes
            analyzer.analyze(nominalList);
            overridingAnalyzer.analyze(overrideList);

            // Extra Metadata
            bindings.stream().forEach(this::collectBindingsMetadata);

            // populate nullable
            nullableKeys.stream().forEach(optionalKeys::add);
            this.nullableKeys.removeAll(this.keys);

            // reaching all MultiBindings : normal and overriding
            // to check if we need a 2 round
            multiBindings.addAll(
                    bindings.stream() //
                            .filter(b -> b instanceof MultiBinding) //
                            .map(b -> MultiBinding.class.cast(b)) //
                            .collect(Collectors.toList()));

            overridingMultiBindings.addAll(
                    overridingBindings.stream() //
                            .filter(b -> b instanceof MultiBinding) //
                            .map(b -> MultiBinding.class.cast(b)) //
                            .collect(Collectors.toList()));

            if (multiBindings.size() == 0 && overridingMultiBindings.size() == 0)
            {
                return InitState.INITIALIZED;
            }
            else
            {
                return InitState.NON_INITIALIZED;
            }
        }
        else
        {
            multiPredicates.stream().forEach(p -> {
                Collection<Class<?>> classes = typesByPredicate.get(p);

                PredicateFromTypeLiteral predicateFromTypeLiteral = (PredicateFromTypeLiteral) p;

                predicateFromTypeLiteral.mb.classes = classes;

            });
            overridingMultiPredicates.stream().forEach(p -> {
                Collection<Class<?>> classes = typesByPredicate.get(p);

                PredicateFromTypeLiteral predicateFromTypeLiteral = (PredicateFromTypeLiteral) p;

                predicateFromTypeLiteral.mb.classes = classes;

            });

            return InitState.INITIALIZED;
        }
    }

    private void collectBindingsMetadata(Binding binding)
    {
        if (binding instanceof InjectionBinding)
        {
            InjectionBinding ib = (InjectionBinding) binding;
            Key<?> key = key(ib.key.value, ib.key.qualifierAnno);
            bindingInfos.put(key, BindingInfo.NOMINAL);
            keys.add(key);
        }
        else if (binding instanceof NullableBinding)
        {
            NullableBinding nullableBinding = (NullableBinding) binding;
            Key<?> key = key(nullableBinding.key, nullableBinding.qualifierAnno);
            bindingInfos.put(key, BindingInfo.NULLABLE);
            nullableKeys.add(key);
        }
    }

    private Key<?> key(Object key, Annotation qualifierAnno)
    {
        if (qualifierAnno == null)
        {
            return Key.get((TypeLiteral<?>) key);
        }
        else
        {
            return Key.get((TypeLiteral<?>) key, qualifierAnno);
        }
    }

    private Predicate<Class<?>> predicateFromMultiBinding(MultiBinding mb)
    {
        //
        Predicate<Class<?>> predicate = new PredicateFromTypeLiteral(mb);

        multiPredicates.add(predicate);

        return predicate;
    }

    private Predicate<Class<?>> predicateFromOverridingMultiBinding(MultiBinding mb)
    {
        //
        Predicate<Class<?>> predicate = new PredicateFromTypeLiteral(mb);

        overridingMultiPredicates.add(predicate);

        return predicate;
    }

    class PredicateFromTypeLiteral implements Predicate<Class<?>>
    {

        public final TypeLiteral<?> key;
        public final MultiBinding   mb;

        public PredicateFromTypeLiteral(MultiBinding mb)
        {
            this.mb = mb;
            this.key = (TypeLiteral<?>) mb.key;
        }

        @Override
        public boolean test(Class<?> t)
        {
            // key is a super class/interface of t //
            return this.key.getRawType().isAssignableFrom(t) && !this.key.getRawType().equals(t);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof PredicateFromTypeLiteral)
            {
                return this.mb.equals(((PredicateFromTypeLiteral) obj).mb);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return this.mb.hashCode();
        }
    }

    @Override
    public Object nativeUnitModule()
    {
        if (topologyModule == null)
        {
            topologyModule = new TopologyModule(bindings, nullableKeys, optionalKeys, multiBindings); // rajouter
                                                                                                      // multiBindings
        }
        return topologyModule;
    }

    @Override
    public Object nativeOverridingUnitModule()
    {
        if (overridingTopologyModule == null)
        {
            overridingTopologyModule = new TopologyModule(overridingBindings, nullableKeys, optionalKeys, overridingMultiBindings); // rajouter
                                                                                                                                    // multiBindings
        }

        return overridingTopologyModule;

    }
}
