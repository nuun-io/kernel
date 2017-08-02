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

import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.spi.topology.binding.Binding;
import io.nuun.kernel.spi.topology.binding.InstanceBinding;
import io.nuun.kernel.spi.topology.binding.InterceptorBinding;
import io.nuun.kernel.spi.topology.binding.LinkedBinding;
import io.nuun.kernel.spi.topology.binding.NullableBinding;
import io.nuun.kernel.spi.topology.binding.ProviderBinding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.util.Providers;

public class TopologyModule extends AbstractModule
{
    private Logger              logger = LoggerFactory.getLogger(TopologyModule.class);

    private Collection<Binding> bindings;

    private BindingInfos        bindingInfos;

    public TopologyModule(Collection<Binding> bindings)
    {
        this.bindings = bindings;
        this.bindingInfos = new BindingInfos();
    }

    @Override
    protected void configure()
    {
        bindings.stream().forEach(this::collectBindingsMetadata);
        bindings.stream().filter(this::isNotNullable).forEach(this::configureBinding);
        configureNullableAndOptionals();
    }

    private void configureNullableAndOptionals()
    {
        List<Key> nullableKeys = bindingInfos.keys(BindingInfo.NULLABLE).stream().collect(Collectors.toList());
        // TODO : implémenter la requête sur les autre modules des autres
        // For each nullable key we bind it to
        nullableKeys.stream().forEach(this::doBindNullableAndOptional);

    }

    private void doBindNullableAndOptional(Key k)
    {
        // bind to null
        binder().bind(k).toProvider(Providers.of(null));
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

    private void collectBindingsMetadata(Binding binding)
    {
        if (binding instanceof NullableBinding)
        {
            NullableBinding nullableBinding = (NullableBinding) binding;
            bindingInfos.put(key(nullableBinding.key, nullableBinding.qualifierAnno), BindingInfo.NULLABLE);
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

    private void updateBindingInfo(TypeLiteral typeLiteral, Annotation qualifierAnno)
    {
        bindingInfos.put(Key.get(typeLiteral, qualifierAnno), BindingInfo.IS_BOUND);
    }

    private void updateBindingInfo(TypeLiteral typeLiteral)
    {
        bindingInfos.put(Key.get(typeLiteral), BindingInfo.IS_BOUND);
    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    private void configureBinding(Binding binding)
    {
        if (InstanceBinding.class.getSimpleName().equals(binding.name()))
        {
            InstanceBinding ib = (InstanceBinding) binding;
            if (ib.qualifierAnno != null)
            {
                // key anno injected
                this.binder().bind(typeLiteral(ib.key)).annotatedWith(ib.qualifierAnno).toInstance(ib.injected);
                updateBindingInfo(typeLiteral(ib.key), ib.qualifierAnno);
            }
            else
            {
                this.binder().bind(typeLiteral(ib.key)).toInstance(ib.injected);
                updateBindingInfo(typeLiteral(ib.key));
            }
        }
        else if (LinkedBinding.class.getSimpleName().equals(binding.name()))
        {
            LinkedBinding lb = LinkedBinding.class.cast(binding);

            if (lb.qualifierAnno != null && lb.injected.getClass().equals(Class.class))
            {
                this.binder().bind(typeLiteral(lb.key)).annotatedWith(lb.qualifierAnno).to((Class<?>) lb.injected);
                updateBindingInfo(typeLiteral(lb.key), lb.qualifierAnno);
                logger.trace("Bound {} to {} with {}", typeLiteral(lb.key).getRawType().getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else if (lb.qualifierAnno != null && !lb.injected.getClass().equals(Class.class))
            {
                this.binder().bind(typeLiteral(lb.key)).annotatedWith(lb.qualifierAnno).toInstance(lb.injected);
                updateBindingInfo(typeLiteral(lb.key), lb.qualifierAnno);
                logger.trace("Bound {} to instance {} with {}", typeLiteral(lb.key).getRawType().getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else if (!typeLiteral(lb.key).getRawType().equals(lb.injected))
            {
                this.binder().bind(typeLiteral(lb.key)).to((Class<?>) lb.injected);
                updateBindingInfo(typeLiteral(lb.key));
                logger.trace("Bound {} to {}", typeLiteral(lb.key).getRawType().getSimpleName(), lb.injected);
            }
            else
            {
                this.binder().bind(typeLiteral(lb.key));
                updateBindingInfo(typeLiteral(lb.key));
                logger.trace("Bound {} to itself", typeLiteral(lb.key).getRawType().getSimpleName());
            }
        }
        else if (ProviderBinding.class.getSimpleName().equals(binding.name()))
        {
            ProviderBinding pb = ProviderBinding.class.cast(binding);

            if (pb.qualifierAnno != null)
            {
                this.binder().bind(typeLiteral(pb.key)).annotatedWith(pb.qualifierAnno).toProvider((Class<?>) pb.injected);
                updateBindingInfo(typeLiteral(pb.key), pb.qualifierAnno);
                logger.trace("Bound {} to {} with {}", typeLiteral(pb.key).getRawType().getSimpleName(), pb.injected, pb.qualifierAnno);
            }
            else
            {
                this.binder().bind(typeLiteral(pb.key)).toProvider((Class<?>) pb.injected);
                updateBindingInfo(typeLiteral(pb.key));
                logger.trace("Bound {} to {}", typeLiteral(pb.key).getRawType().getSimpleName(), pb.injected);
            }
        }
        else if (InterceptorBinding.class.getSimpleName().equals(binding.name()))
        {
            InterceptorBinding pb = InterceptorBinding.class.cast(binding);
            // Class
            Optional<Predicate<Class>> optionalClassPredicate = (Optional<Predicate<Class>>) newInstance(pb.classPredicate);
            Predicate<Class> classPredicate = null;
            if (optionalClassPredicate.isPresent())
            {
                classPredicate = optionalClassPredicate.get();
            }
            else
            {
                throw new KernelException("InterceptorBinding : classPredicate is null.");
            }
            Matcher<? super Class<?>> classMatcher = new PredicateMatcherAdapter<>(classPredicate);

            // Method
            Optional<Predicate<Method>> optionalMethodPredicate = (Optional<Predicate<Method>>) newInstance(pb.methodPredicate);
            Predicate<Method> methodPredicate = null;
            if (optionalMethodPredicate.isPresent())
            {
                methodPredicate = optionalMethodPredicate.get();
            }
            else
            {
                throw new KernelException("InterceptorBinding : methodPredicate is null.");
            }
            Matcher<Method> methodMatcher = new PredicateMatcherAdapter<>(methodPredicate);

            Optional<MethodInterceptor> optionalInterceptor = newInstance((Class<MethodInterceptor>) pb.methodInterceptor);
            MethodInterceptor methodInterceptor = null;
            if (optionalInterceptor.isPresent())
            {
                methodInterceptor = optionalInterceptor.get();

            }
            else
            {
                throw new KernelException("InterceptorBinding : methodInterceptor is null.");
            }
            this.binder().bindInterceptor(classMatcher, methodMatcher, new MethodInterceptor[] {
                methodInterceptor
            });
            logger.trace("Bound {} to {} and {}", pb.methodInterceptor.getName(), pb.classPredicate.getName(), pb.methodPredicate.getName());
        }
    }

    private TypeLiteral typeLiteral(Object key)
    {
        return TypeLiteral.class.cast(key);
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
