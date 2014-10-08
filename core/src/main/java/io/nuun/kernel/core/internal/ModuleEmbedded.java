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