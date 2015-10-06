package io.nuun.kernel.core.internal;

import com.google.inject.Injector;
import io.nuun.kernel.api.di.ObjectGraph;

class ObjectGraphEmbedded implements ObjectGraph
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
