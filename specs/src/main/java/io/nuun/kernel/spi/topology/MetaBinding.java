package io.nuun.kernel.spi.topology;

import java.lang.annotation.Annotation;

public abstract class MetaBinding extends Binding
{
    public final Object     key;
    public final Annotation qualifierAnno;

    public MetaBinding(Object key, Annotation qualifier)
    {
        this.key = key;
        this.qualifierAnno = qualifier;
    }

    public MetaBinding(Object key)
    {
        this(key, (Annotation) null);
    }

}
