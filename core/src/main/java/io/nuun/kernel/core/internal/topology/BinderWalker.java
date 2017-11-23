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
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;

import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.topology.TopologyModule.PredicateMatcherAdapter;
import io.nuun.kernel.spi.topology.binding.MultiBinding.MultiKind;

@SuppressWarnings({
        "unchecked", "rawtypes"
})
public class BinderWalker implements Walker
{
    private Logger       logger = LoggerFactory.getLogger(BinderWalker.class);

    private final Binder binder;

    public BinderWalker(Binder binder)
    {
        this.binder = binder;
    }

    Binder binder()
    {
        return this.binder;
    }

    @Override
    public void bindInstance(TypeLiteral typeLiteral, Annotation qualifierAnno, Object injected)
    {
        this.binder().bind(typeLiteral).annotatedWith(qualifierAnno).toInstance(injected);
    }

    @Override
    public void bindInstance(TypeLiteral typeLiteral, Object injected)
    {
        this.binder().bind(typeLiteral).toInstance(injected);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral, Annotation qualifierAnno, Class<?> injected)
    {
        this.binder().bind(typeLiteral).annotatedWith(qualifierAnno).to(injected);

        logger.trace("Bound {} to {} with {}", typeLiteral.getRawType().getSimpleName(), injected, qualifierAnno);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral, Annotation qualifierAnno, Object injected)
    {
        this.binder().bind(typeLiteral).annotatedWith(qualifierAnno).toInstance(injected);

        logger.trace("Bound {} to instance {} with {}", typeLiteral.getRawType().getSimpleName(), injected, qualifierAnno);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral, Class<?> injected)
    {
        this.binder().bind(typeLiteral).to(injected);

        logger.trace("Bound {} to {}", typeLiteral.getRawType().getSimpleName(), injected);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral)
    {
        this.binder().bind(typeLiteral);

        logger.trace("Bound {} to itself", typeLiteral.getRawType().getSimpleName());
    }

    @Override
    public void bindProvider(TypeLiteral typeLiteral, Annotation qualifierAnno, Class<?> injected)
    {
        this.binder().bind(typeLiteral).annotatedWith(qualifierAnno).toProvider(injected);

        logger.trace("Bound {} to {} with {}", typeLiteral.getRawType().getSimpleName(), injected, qualifierAnno);
    }

    @Override
    public void bindProvider(TypeLiteral typeLiteral, Class<?> injected)
    {
        this.binder().bind(typeLiteral).toProvider(injected);

        logger.trace("Bound {} to {}", typeLiteral.getRawType().getSimpleName(), injected);
    }

    @Override
    public void bindInterceptor(Class<?> bindingClassPredicate, Class<?> bindingMethodPredicate, Class<?> bindingMethodInterceptor)
    {
        // Class
        Optional<Predicate<Class>> optionalClassPredicate = (Optional<Predicate<Class>>) newInstance(bindingClassPredicate);
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
        Optional<Predicate<Method>> optionalMethodPredicate = (Optional<Predicate<Method>>) newInstance(bindingMethodPredicate);
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

        Optional<MethodInterceptor> optionalInterceptor = newInstance((Class<MethodInterceptor>) bindingMethodInterceptor);
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
        logger.trace("Bound {} to {} and {}", bindingMethodInterceptor.getName(), bindingClassPredicate.getName(), bindingMethodPredicate.getName());
    }

    private <T> Optional<T> newInstance(Class<T> candidate)
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

    @Override
    public void bindMulti(TypeLiteral keyTypeLiteral, TypeLiteral valueTypeLiteral, MultiKind kind, Class<? extends Function<?, ?>> keyResolver)
    {

    }

}
