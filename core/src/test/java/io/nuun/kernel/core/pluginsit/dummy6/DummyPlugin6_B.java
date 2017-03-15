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
package io.nuun.kernel.core.pluginsit.dummy6;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * A 2 rounds plugin
 *
 * @author ejemba
 */
public class DummyPlugin6_B extends AbstractPlugin
{

    public DummyPlugin6_B()
    {
    }

    @Override
    public String name()
    {
        return "dummy-6-B";
    }

    @Override
    public Collection<Class<?>> dependentPlugins()
    {
        if (round.isFirst())
        {
            return Lists.newArrayList(DummyPlugin6_D.class, DummyPlugin6_C.class);
        } else
        {
            return super.dependentPlugins();
        }
    }

    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        if (round.number() == 3)
        {
            return bindingRequestsBuilder().annotationType(Marker66.class).build();
        }
        return super.bindingRequests();
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        if (round.number() == 4)
        {
            return classpathScanRequestBuilder().annotationType(Marker6.class).build();
        }

        return super.classpathScanRequests();
    }

    @Override
    public InitState init(InitContext initContext)
    {
        if (round.number() != 5)
        {
            Collection<?> dependentPlugins = initContext.dependentPlugins();
            assertThat(dependentPlugins).isNotNull();
            if (round.isFirst())
            {
                assertThat(dependentPlugins).hasSize(2);
            } else
            {
                assertThat(dependentPlugins).hasSize(0);
            }

            if (round.number() == 4)
            {
                Collection<Class<?>> collection = initContext.scannedClassesByAnnotationClass().get(Marker6.class);
                assertThat(collection).hasSize(2);
            } else
            {
                Collection<Class<?>> collection = initContext.scannedClassesByAnnotationClass().get(Marker6.class);
                assertThat(collection).isNullOrEmpty();
            }

            for (Object plugin : dependentPlugins)
            {
                if (DummyPlugin6_D.class.isAssignableFrom(plugin.getClass()))
                {
                    DummyPlugin6_D.class.cast(plugin).setInternal(true);
                }
                if (DummyPlugin6_C.class.isAssignableFrom(plugin.getClass()))
                {
                    DummyPlugin6_C.class.cast(plugin).setInternal(true);
                }
            }

            return InitState.NON_INITIALIZED;
        } else
        {
            return InitState.INITIALIZED;
        }
    }

    @Override
    public String toString()
    {
        return "B";
    }

}
