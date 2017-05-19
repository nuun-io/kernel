package io.nuun.kernel.core.internal.topology;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

import io.nuun.kernel.spi.topology.Binding;
import io.nuun.kernel.spi.topology.InstanceBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;
import io.nuun.kernel.spi.topology.ProviderBinding;

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

    @SuppressWarnings("unchecked")
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
            if (lb.qualifierClass != null)
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierClass).to((Class<?>) lb.injected);
                logger.trace("Bound {} to {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierClass.getSimpleName());
            }
            else if (lb.qualifierAnno != null)
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierAnno).to((Class<?>) lb.injected);
                logger.trace("Bound {} to {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else
            {
                this.binder().bind(lb.key).to((Class<?>) lb.injected);
                logger.trace("Bound {} to {}", lb.key.getSimpleName(), lb.injected);
            }
        }
        else if (ProviderBinding.class.getSimpleName().equals(binding.name()))
        {
            ProviderBinding pb= ProviderBinding.class.cast(binding);
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
    }
}
