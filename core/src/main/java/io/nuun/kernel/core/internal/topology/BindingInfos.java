package io.nuun.kernel.core.internal.topology;

import java.util.Collection;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.Key;

public class BindingInfos
{

    Multimap<Key<?>, BindingInfo> multimap;

    public BindingInfos()
    {
        multimap = MultimapBuilder

        .hashKeys().enumSetValues(BindingInfo.class).build();
    }

    public void put(Key<?> key, BindingInfo info)
    {
        multimap.put(key, info);
    }

    public Collection<BindingInfo> get(Key<?> key)
    {
        return multimap.get(key);
    }

    public boolean contains(Key<?> key, BindingInfo info)
    {
        return get(key).stream().anyMatch(i -> i.equals(info));
    }

}
