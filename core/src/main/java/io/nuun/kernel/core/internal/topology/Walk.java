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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;

import io.nuun.kernel.spi.topology.binding.Binding;
import io.nuun.kernel.spi.topology.binding.BindingKey;
import io.nuun.kernel.spi.topology.binding.InstanceBinding;
import io.nuun.kernel.spi.topology.binding.InterceptorBinding;
import io.nuun.kernel.spi.topology.binding.LinkedBinding;
import io.nuun.kernel.spi.topology.binding.MultiBinding;
import io.nuun.kernel.spi.topology.binding.ProviderBinding;

public class Walk
{

    private Logger logger = LoggerFactory.getLogger(Walk.class);

    private Walker walker;

    public Walk(Walker walker)
    {
        this.walker = walker;
    }

    public void walk(Binding binding)
    {
        if (InstanceBinding.class.getSimpleName().equals(binding.name()))
        {
            InstanceBinding ib = (InstanceBinding) binding;
            if (ib.key.qualifierAnno != null)
            {
                walker.bindInstance(typeLiteral(ib.key.value), ib.key.qualifierAnno, ib.injected);
            }
            else
            {
                walker.bindInstance(typeLiteral(ib.key.value), ib.injected);
            }
        }
        else if (LinkedBinding.class.getSimpleName().equals(binding.name()))
        {
            LinkedBinding lb = LinkedBinding.class.cast(binding);

            if (lb.key.qualifierAnno != null && lb.injected.getClass().equals(Class.class))
            {
                walker.bindLink(typeLiteral(lb.key.value), lb.key.qualifierAnno, (Class<?>) lb.injected);
            }
            else if (lb.key.qualifierAnno != null && !lb.injected.getClass().equals(Class.class))
            {
                walker.bindLink(typeLiteral(lb.key.value), lb.key.qualifierAnno, lb.injected);
            }
            else if (!typeLiteral(lb.key.value).getRawType().equals(lb.injected))
            {
                walker.bindLink(typeLiteral(lb.key.value), (Class<?>) lb.injected);
            }
            else
            {
                walker.bindLink(typeLiteral(lb.key.value));
            }
        }
        else if (ProviderBinding.class.getSimpleName().equals(binding.name()))
        {
            ProviderBinding pb = ProviderBinding.class.cast(binding);

            if (pb.key.qualifierAnno != null)
            {
                walker.bindProvider(typeLiteral(pb.key.value), pb.key.qualifierAnno, (Class<?>) pb.injected);
            }
            else
            {
                walker.bindProvider(typeLiteral(pb.key.value), (Class<?>) pb.injected);
            }
        }
        else if (InterceptorBinding.class.getSimpleName().equals(binding.name()))
        {
            InterceptorBinding pb = InterceptorBinding.class.cast(binding);

            walker.bindInterceptor(pb.classPredicate, pb.methodPredicate, pb.methodInterceptor);
        }
        else if (MultiBinding.class.getSimpleName().equals(binding.name()))
        {
            MultiBinding mb = MultiBinding.class.cast(binding);
            walker.bindMulti(typeLiteral(mb.key), typeLiteral(mb.value), mb.kind, mb.keyResolver);
        }

    }

    private TypeLiteral typeLiteral(Object key)
    {
        if (key instanceof BindingKey)
            throw new IllegalStateException("error in refactoring");

        return TypeLiteral.class.cast(key);
    }

}
