package io.nuun.kernel.core.internal.topology;

import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.topology.TopologyModule.PredicateMatcherAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Predicate;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;

@SuppressWarnings({"unchecked" , "rawtypes"})
public class BinderWalker implements BindingWalker
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

}
