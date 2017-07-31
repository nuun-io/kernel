package io.nuun.kernel.spi.topology;

import java.lang.annotation.Annotation;

public class NullableBinding extends MetaBinding
{

    public NullableBinding(Object key, Annotation qualifier)
    {
        super(key, qualifier);
    }

    public NullableBinding(Object key)
    {
        super(key);
    }

}
