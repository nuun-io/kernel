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
import io.nuun.kernel.spi.topology.Binding;
import io.nuun.kernel.spi.topology.InstanceBinding;
import io.nuun.kernel.spi.topology.InterceptorBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;
import io.nuun.kernel.spi.topology.ProviderBinding;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

public class TopologyModule extends AbstractModule
{
    private Logger              logger = LoggerFactory.getLogger(TopologyModule.class);

    private Collection<Binding> bindings;

    public TopologyModule(Collection<Binding> bindings)
    {
        this.bindings = bindings;
    }

    @Override
    protected void configure()
    {
        bindings.stream().forEach(this::configureBinding);
    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    private void configureBinding(Binding binding)
    {
        if (InstanceBinding.class.getSimpleName().equals(binding.name()))
        {
            InstanceBinding ib = (InstanceBinding) binding;
            if (ib.qualifierClass != null)
            {
                this.binder().bind(ib.key).annotatedWith(ib.qualifierClass).toInstance(ib.injected);
            }
            else if (ib.qualifierAnno != null)
            {
                this.binder().bind(ib.key).annotatedWith(ib.qualifierAnno).toInstance(ib.injected);
            }
            else if (ib.qualifierClass == null)
            {
                this.binder().bind(ib.key).toInstance(ib.injected);
            }

        }
        else if (LinkedBinding.class.getSimpleName().equals(binding.name()))
        {
            LinkedBinding lb = LinkedBinding.class.cast(binding);
            if (lb.qualifierClass != null && lb.injected.getClass().equals(Class.class))
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierClass).to((Class<?>) lb.injected);
                logger.trace("Bound {} to {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierClass.getSimpleName());
            }
            else if (lb.qualifierClass != null && !lb.injected.getClass().equals(Class.class))
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierClass).toInstance(lb.injected);
                logger.trace("Bound {} to instance {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierClass.getSimpleName());
            }
            else if (lb.qualifierAnno != null && lb.injected.getClass().equals(Class.class))
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierAnno).to((Class<?>) lb.injected);
                logger.trace("Bound {} to {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else if (lb.qualifierAnno != null && !lb.injected.getClass().equals(Class.class))
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierAnno).toInstance(lb.injected);
                logger.trace("Bound {} to instance {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else if (!lb.key.equals(lb.injected))
            {
                this.binder().bind(lb.key).to((Class<?>) lb.injected);
                logger.trace("Bound {} to {}", lb.key.getSimpleName(), lb.injected);
            }
            else
            {
                this.binder().bind(lb.key);
                logger.trace("Bound {} to itself", lb.key.getSimpleName());
            }
        }
        else if (ProviderBinding.class.getSimpleName().equals(binding.name()))
        {
            ProviderBinding pb = ProviderBinding.class.cast(binding);
            if (pb.qualifierClass != null)
            {
                this.binder().bind(pb.key).annotatedWith(pb.qualifierClass).toProvider((Class<?>) pb.injected);
                logger.trace("Bound {} to {} with {}", pb.key.getSimpleName(), pb.injected, pb.qualifierClass.getSimpleName());
            }
            else if (pb.qualifierAnno != null)
            {
                this.binder().bind(pb.key).annotatedWith(pb.qualifierAnno).toProvider((Class<?>) pb.injected);
                logger.trace("Bound {} to {} with {}", pb.key.getSimpleName(), pb.injected, pb.qualifierAnno);
            }
            else
            {
                this.binder().bind(pb.key).toProvider((Class<?>) pb.injected);
                logger.trace("Bound {} to {}", pb.key.getSimpleName(), pb.injected);
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
