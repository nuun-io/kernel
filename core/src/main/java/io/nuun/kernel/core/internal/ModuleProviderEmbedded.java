package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.di.ModuleProvider;

import com.google.inject.Module;

public class ModuleProviderEmbedded implements ModuleProvider
{
    private Object module;

    public ModuleProviderEmbedded(Object module)
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
        if (ModuleProvider.class.isAssignableFrom(targetType))
        {
            return (T) Module.class.cast(module);
        }
        throw new IllegalStateException("Can not cast " + module + " to " + targetType.getName());
    }

}