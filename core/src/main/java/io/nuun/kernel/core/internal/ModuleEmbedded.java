package io.nuun.kernel.core.internal;

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
    public Object get()
    {
        return module;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> targetType)
    {
        if (UnitModule.class.isAssignableFrom(targetType))
        {
            return (T) Module.class.cast(module);
        }
        throw new IllegalStateException("Can not cast " + module + " to " + targetType.getName());
    }

}