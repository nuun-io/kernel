package io.nuun.kernel.core.internal.topology;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return multimap.containsEntry(key, info);
        // return get(key).stream().anyMatch(i -> i.equals(info));
    }

    public List<Key> keys(BindingInfo bindingInfo)
    {
        return multimap.entries().stream().filter(e -> e.getValue().equals(bindingInfo)).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
