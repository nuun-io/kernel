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
package io.nuun.kernel.core.pluginsit.dummy7;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.nuun.kernel.core.AbstractPlugin;

public class DummyPlugin7_A extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "dummy-plugin-7-A";
    }

    @Override
    public String toString()
    {
        return "A";
    }

    @Override
    public Object nativeUnitModule()
    {
        return new AbstractModule()
        {

            @Override
            protected void configure()
            {
                bind(String.class).annotatedWith(Names.named("dep7a")).toInstance("dep7a");
                bind(String.class).annotatedWith(Names.named("dep7b")).toInstance("dep7b");
            }
        };
    }

}
