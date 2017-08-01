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
