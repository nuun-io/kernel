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
package io.nuun.kernel.core.internal.injection;

import com.google.inject.Injector;
import io.nuun.kernel.api.di.ObjectGraph;

public class ObjectGraphEmbedded implements ObjectGraph
{

    private Object injector;

    public ObjectGraphEmbedded(Object injector)
    {
        this.injector = injector;
    }

    @Override
    public Object get()
    {
        return injector;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> targetType)
    {
        if (targetType.equals(Injector.class))
        {
            return (T) Injector.class.cast(injector);
        }
        throw new IllegalStateException("Can not cast " + injector + " to " + targetType.getName());
    }

}
