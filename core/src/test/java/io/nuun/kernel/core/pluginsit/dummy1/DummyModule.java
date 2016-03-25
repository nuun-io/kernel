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
/**
 * 
 */
package io.nuun.kernel.core.pluginsit.dummy1;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.util.Providers;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author Epo Jemba
 */
public class DummyModule extends AbstractModule
{
    private final Collection<Class<?>> classes;

    public DummyModule(Collection<Class<?>> classes)
    {
        this.classes = classes;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void configure()
    {
        bind(DummyService.class).to(DummyServiceInternal.class);
        bind(DummyService.class).annotatedWith(MarkerSample3.class).to(DummyServiceInternal2.class);
        Provider ofNull = Providers.of(null);
        for (Class<?> klass : classes)
        {
            if (klass.getAnnotation(Nullable.class)== null )
            {
                bind(klass);
            }
            else
            {
                bind(klass).toProvider( ofNull);
            }
        }
    }
}
