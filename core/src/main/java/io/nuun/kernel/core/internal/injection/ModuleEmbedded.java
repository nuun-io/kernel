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

import io.nuun.kernel.api.di.GlobalModule;
import io.nuun.kernel.api.di.UnitModule;

import com.google.inject.Module;

public class ModuleEmbedded implements UnitModule, GlobalModule
{
    private Object module;

    public ModuleEmbedded(Object module)
    {
        this.module = module;
    }

    @Override
    public Object nativeModule()
    {
        return module;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> targetType)
    {
        if (targetType.isAssignableFrom(module.getClass()))
        {
            return (T) Module.class.cast(module);
        }
        throw new IllegalStateException("Can not cast " + module + " to " + targetType.getName());
    }
    
    public static ModuleEmbedded wrap(Object module)
    {
        return new ModuleEmbedded(module);
    }

}