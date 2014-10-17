/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nuun.kernel.tests.ut.fixtures;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.inject.spi.Element;


/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class Store implements Multimap<Class<? extends Element>, Object>
{
    
    private final Multimap<Class<? extends Element>, Object> store;
    
    public Store()
    {
        store = HashMultimap.create();
    }
    

    @Override
    public int size()
    {
        return store.size();
    }

    @Override
    public boolean isEmpty()
    {
        return store.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return store.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return store.containsValue(value);
    }

    @Override
    public boolean containsEntry(Object key, Object value)
    {
        return store.containsEntry(key, value);
    }

    @Override
    public boolean put(Class<? extends Element> key, Object value)
    {
        return store.put(key, value);
    }

    @Override
    public boolean remove(Object key, Object value)
    {
        return store.remove(key, value);
    }

    @Override
    public boolean putAll(Class<? extends Element> key, Iterable<? extends Object> values)
    {
        return store.putAll(key, values);
    }

    @Override
    public boolean putAll(Multimap<? extends Class<? extends Element>, ? extends Object> multimap)
    {
        return store.putAll(multimap);
    }

    @Override
    public Collection<Object> replaceValues(Class<? extends Element> key, Iterable<? extends Object> values)
    {
        return store.replaceValues(key, values);
    }

    @Override
    public Collection<Object> removeAll(Object key)
    {
        return store.removeAll(key);
    }

    @Override
    public void clear()
    {
        store.clear();
    }

    @Override
    public Collection<Object> get(Class<? extends Element> key)
    {
        return store.get(key);
    }

    @Override
    public Set<Class<? extends Element>> keySet()
    {
        return store.keySet();
    }

    @Override
    public Multiset<Class<? extends Element>> keys()
    {
        return store.keys();
    }

    @Override
    public Collection<Object> values()
    {
        return store.values();
    }

    @Override
    public Collection<Entry<Class<? extends Element>, Object>> entries()
    {
        return store.entries();
    }

    @Override
    public Map<Class<? extends Element>, Collection<Object>> asMap()
    {
        return store.asMap();
    }

    @Override
    public boolean equals(Object obj)
    {
        return store.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return store.hashCode();
    }

}
