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
package io.nuun.kernel.core.pluginsit.dummy23;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Epo Jemba
 */
public class DummyPlugin2 extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "dummyPlugin2";
    }

    @Override
    public Collection<Class<?>> requiredPlugins()
    {
        return Lists.newArrayList(DummyPlugin3.class);
    }

    @Override
    public InitState init(InitContext initContext)
    {
        assertThat(initContext.pluginsRequired()).isNotNull();
        assertThat(initContext.pluginsRequired()).hasSize(1);
        assertThat(initContext.pluginsRequired().iterator().next().getClass()).isEqualTo(DummyPlugin3.class);
        return InitState.INITIALIZED;
    }
}
