package io.nuun.kernel.core.internal.topology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;

import io.nuun.kernel.spi.topology.binding.Binding;
import io.nuun.kernel.spi.topology.binding.InstanceBinding;
import io.nuun.kernel.spi.topology.binding.InterceptorBinding;
import io.nuun.kernel.spi.topology.binding.LinkedBinding;
import io.nuun.kernel.spi.topology.binding.ProviderBinding;

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
                walker.bindInstance(typeLiteral(ib.key), ib.qualifierAnno, ib.injected);
            }
            else
            {
                walker.bindInstance(typeLiteral(ib.key), ib.injected);
            }
        }
        else if (LinkedBinding.class.getSimpleName().equals(binding.name()))
        {
            LinkedBinding lb = LinkedBinding.class.cast(binding);

            if (lb.qualifierAnno != null && lb.injected.getClass().equals(Class.class))
            {
                walker.bindLink(typeLiteral(lb.key), lb.qualifierAnno, (Class<?>) lb.injected);
            }
            else if (lb.qualifierAnno != null && !lb.injected.getClass().equals(Class.class))
            {
                walker.bindLink(typeLiteral(lb.key), lb.qualifierAnno, lb.injected);
            }
            else if (!typeLiteral(lb.key).getRawType().equals(lb.injected))
            {
                walker.bindLink(typeLiteral(lb.key), (Class<?>) lb.injected);
            }
            else
            {
                walker.bindLink(typeLiteral(lb.key));
            }
        }
        else if (ProviderBinding.class.getSimpleName().equals(binding.name()))
        {
            ProviderBinding pb = ProviderBinding.class.cast(binding);

            if (pb.qualifierAnno != null)
            {
                walker.bindProvider(typeLiteral(pb.key), pb.qualifierAnno, (Class<?>) pb.injected);
            }
            else
            {
                walker.bindProvider(typeLiteral(pb.key), (Class<?>) pb.injected);
            }
        }
        else if (InterceptorBinding.class.getSimpleName().equals(binding.name()))
        {
            InterceptorBinding pb = InterceptorBinding.class.cast(binding);
            
            walker.bindInterceptor(pb.classPredicate, pb.methodPredicate, pb.methodInterceptor);
        }
    }

    private TypeLiteral typeLiteral(Object key)
    {
        return TypeLiteral.class.cast(key);
    }

}
