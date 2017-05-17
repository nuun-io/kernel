package io.nuun.kernel.core.internal.topology;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

import io.nuun.kernel.spi.topology.Binding;
import io.nuun.kernel.spi.topology.InstanceBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;

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
            if (ib.qualifierClass == null)
            {
                this.binder().bind(ib.key).toInstance(ib.injected);
            }
            else
            {
                this.binder().bind(ib.key).annotatedWith(ib.qualifierClass).toInstance(ib.injected);
            }
        }
        else if (LinkedBinding.class.getSimpleName().equals(binding.name()))
        {
            LinkedBinding lb = LinkedBinding.class.cast(binding);
            if (lb.qualifierClass != null)
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierClass).to((Class<?>) lb.injected);
                logger.info("Bound {} to {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierClass.getSimpleName());
            }
            else if (lb.qualifierAnno != null)
            {
                this.binder().bind(lb.key).annotatedWith(lb.qualifierAnno).to((Class<?>) lb.injected);
                logger.info("Bound {} to {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifierAnno);
            }
            else
            {
                this.binder().bind(lb.key).to((Class<?>) lb.injected);
                logger.info("Bound {} to {}", lb.key.getSimpleName(), lb.injected);
            }
        }
    }
}
