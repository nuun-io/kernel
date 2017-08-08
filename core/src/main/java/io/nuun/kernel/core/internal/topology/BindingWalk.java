package io.nuun.kernel.core.internal.topology;

import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.topology.TopologyModule.PredicateMatcherAdapter;
import io.nuun.kernel.spi.topology.binding.Binding;
import io.nuun.kernel.spi.topology.binding.InstanceBinding;
import io.nuun.kernel.spi.topology.binding.InterceptorBinding;
import io.nuun.kernel.spi.topology.binding.LinkedBinding;
import io.nuun.kernel.spi.topology.binding.ProviderBinding;

import java.lang.reflect.Method;
import java.util.Optional;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;

public class BindingWalk
{

    private Logger        logger = LoggerFactory.getLogger(BindingWalk.class);

    private BindingWalker walker;

    public BindingWalk(BindingWalker walker)
    {
        this.walker = walker;
    }

    public void walk(Binding binding)
    {
        if (InstanceBinding.class.getSimpleName().equals(binding.name()))
        {
            InstanceBinding ib = (InstanceBinding) binding;
            if (ib.qualifierAnno != null)
            {
                // key anno injected
                walker.bindInstance(typeLiteral(ib.key), ib.qualifierAnno, ib.injected);
                // this.binder().bind(typeLiteral(ib.key)).annotatedWith(ib.qualifierAnno).toInstance(ib.injected);
                // updateBindingInfo(typeLiteral(ib.key), ib.qualifierAnno);
            }
            else
            {
                walker.bindInstance(typeLiteral(ib.key), ib.injected);
                // this.binder().bind(typeLiteral(ib.key)).toInstance(ib.injected);
                // updateBindingInfo(typeLiteral(ib.key));
            }
        }
        else if (LinkedBinding.class.getSimpleName().equals(binding.name()))
        {
            LinkedBinding lb = LinkedBinding.class.cast(binding);

            if (lb.qualifierAnno != null && lb.injected.getClass().equals(Class.class))
            {
                walker.bindLink(typeLiteral(lb.key), lb.qualifierAnno, (Class<?>) lb.injected);
                // this.binder().bind(typeLiteral(lb.key)).annotatedWith(lb.qualifierAnno).to((Class<?>)
                // lb.injected);
                // updateBindingInfo(typeLiteral(lb.key), lb.qualifierAnno);
                logger.trace("Bound {} to {} with {}", typeLiteral(lb.key).getRawType().getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else if (lb.qualifierAnno != null && !lb.injected.getClass().equals(Class.class))
            {
                walker.bindLink(typeLiteral(lb.key), lb.qualifierAnno, lb.injected);
                // this.binder().bind(typeLiteral(lb.key)).annotatedWith(lb.qualifierAnno).toInstance(lb.injected);
                // updateBindingInfo(typeLiteral(lb.key), lb.qualifierAnno);
                logger.trace("Bound {} to instance {} with {}", typeLiteral(lb.key).getRawType().getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else if (!typeLiteral(lb.key).getRawType().equals(lb.injected))
            {
                walker.bindLink(typeLiteral(lb.key), (Class<?>) lb.injected);
                // this.binder().bind(typeLiteral(lb.key)).to((Class<?>) lb.injected);
                // updateBindingInfo(typeLiteral(lb.key));
                logger.trace("Bound {} to {}", typeLiteral(lb.key).getRawType().getSimpleName(), lb.injected);
            }
            else
            {
                walker.bindLink(typeLiteral(lb.key));
                // this.binder().bind(typeLiteral(lb.key));
                // updateBindingInfo(typeLiteral(lb.key));
                logger.trace("Bound {} to itself", typeLiteral(lb.key).getRawType().getSimpleName());
            }
        }
        else if (ProviderBinding.class.getSimpleName().equals(binding.name()))
        {
            ProviderBinding pb = ProviderBinding.class.cast(binding);

            if (pb.qualifierAnno != null)
            {
                walker.bindProvider(typeLiteral(pb.key), pb.qualifierAnno, (Class<?>) pb.injected);
                // this.binder().bind(typeLiteral(pb.key)).annotatedWith(pb.qualifierAnno).toProvider((Class<?>)
                // pb.injected);
                // updateBindingInfo(typeLiteral(pb.key), pb.qualifierAnno);
                logger.trace("Bound {} to {} with {}", typeLiteral(pb.key).getRawType().getSimpleName(), pb.injected, pb.qualifierAnno);
            }
            else
            {
                walker.bindProvider(typeLiteral(pb.key), (Class<?>) pb.injected);
                // this.binder().bind(typeLiteral(pb.key)).toProvider((Class<?>) pb.injected);
                // updateBindingInfo(typeLiteral(pb.key));
                logger.trace("Bound {} to {}", typeLiteral(pb.key).getRawType().getSimpleName(), pb.injected);
            }
        }
        else if (InterceptorBinding.class.getSimpleName().equals(binding.name()))
        {
            InterceptorBinding pb = InterceptorBinding.class.cast(binding);
            walker.bindInterceptor(pb.classPredicate, pb.methodPredicate, pb.methodInterceptor);
        /*
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
            */
            logger.trace("Bound {} to {} and {}", pb.methodInterceptor.getName(), pb.classPredicate.getName(), pb.methodPredicate.getName());
        }
    }

    private TypeLiteral typeLiteral(Object key)
    {
        return TypeLiteral.class.cast(key);
    }

}
