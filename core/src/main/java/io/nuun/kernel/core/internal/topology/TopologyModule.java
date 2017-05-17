package io.nuun.kernel.core.internal.topology;

import io.nuun.kernel.spi.topology.Binding;
import io.nuun.kernel.spi.topology.InstanceBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

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
            if (ib.qualifier == null)
            {
                this.binder().bind(ib.key).toInstance(ib.injected);
            }
            else
            {
                this.binder().bind(ib.key).annotatedWith(ib.qualifier).toInstance(ib.injected);
            }
        }
        else if (LinkedBinding.class.getSimpleName().equals(binding.name()))
        {
            LinkedBinding lb = LinkedBinding.class.cast(binding);
            if (lb.qualifier == null)
            {
                this.binder().bind(lb.key).to((Class<?>) lb.injected);
                logger.info("Bound {} to {}", lb.key.getSimpleName(), lb.injected);
            }
            else
            {
                this.binder().bind(lb.key).annotatedWith(Names.named("two") /* lb.qualifier */).to((Class<?>) lb.injected);
                logger.info("Bound {} to {} with {}", lb.key.getSimpleName(), lb.injected, lb.qualifier.getSimpleName());
            }
        }
    }

}
