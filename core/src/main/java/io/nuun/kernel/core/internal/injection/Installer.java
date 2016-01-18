package io.nuun.kernel.core.internal.injection;

import com.google.inject.Binder;
import com.google.inject.matcher.Matchers;
import io.nuun.kernel.spi.Concern;

import java.lang.annotation.Annotation;

public abstract class Installer implements Comparable<Installer>
{
    @Override
    public int compareTo(Installer other)
    {
        return order().compareTo(other.order());
    }

    public Long order()
    {
        for (Annotation annotation : getOriginalClass().getAnnotations())
        {
            if (Matchers.annotatedWith(Concern.class).matches(annotation.annotationType()))
            {
                Concern concern = annotation.annotationType().getAnnotation(Concern.class);
                return concern.priority().value() + concern.order();
            }
        }
        return 0L;
    }

    protected abstract void install(Binder binder);

    protected abstract Class<?> getOriginalClass();

}
