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

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.util.Providers;

import io.nuun.kernel.spi.topology.binding.Binding;
import io.nuun.kernel.spi.topology.binding.NullableBinding;

public class TopologyModule extends AbstractModule
{
    private final Logger              logger = LoggerFactory.getLogger(TopologyModule.class);

    private final Collection<Binding> bindings;

    private final Collection<Key>     nullableKeys;

    private final Collection<Key>     optionalKeys;

    private Walk                      walk;

    @SuppressWarnings("rawtypes")
    public TopologyModule(Collection<Binding> bindings, Collection<Key> nullableKeys, Collection<Key> optionalKeys)
    {
        this.bindings = bindings;
        this.nullableKeys = nullableKeys;
        this.optionalKeys = optionalKeys;
    }

    @Override
    protected void configure()
    {
        this.walk = new Walk(new BinderWalker(this.binder()));
        bindings.stream().filter(this::isNotNullable).forEach(walk::walk);
        configureNullableAndOptionals();
    }

    private void configureNullableAndOptionals()
    {
        nullableKeys.stream().forEach(this::doBindNullable);
        optionalKeys.stream().forEach(this::doBindOptional);
    }

    private void doBindNullable(Key<?> k)
    {
        binder().bind(k).toProvider(Providers.of(null));
    }

    private void doBindOptional(Key<?> k)
    {
        OptionalBinder.newOptionalBinder(binder(), k);
    }

    private boolean isNotNullable(Binding binding)
    {
        return !isNullable(binding);
    }

    private boolean isNullable(Binding binding)
    {
        return (binding instanceof NullableBinding);
    }

    public static <T> Optional<T> newInstance(Class<T> candidate)
    {
        try
        {
            return Optional.of(candidate.newInstance());
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            return Optional.empty();
        }
    }

    public static class PredicateMatcherAdapter<T> extends AbstractMatcher<T>
    {
        private Predicate<T> predicate;

        public PredicateMatcherAdapter(Predicate<T> predicate)
        {
            this.predicate = predicate;
        }

        @Override
        public boolean matches(T candidate)
        {
            return this.predicate.test(candidate);
        }

    }
}
