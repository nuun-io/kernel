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
package io.nuun.kernel.core.internal;

import io.nuun.kernel.core.KernelException;

import java.util.*;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class AliasMap
{
    private Map<String, String> aliases = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();

    /**
     * @param key   the key to alias.
     * @param alias the alias to give to the key.
     * @return the previous alias corresponding the key
     */
    public String putAlias(String alias, String key)
    {
        if (aliases.containsKey(key))
        {
            throw new IllegalArgumentException("The key \"" + key + "\" to alias is already present in the kernel parameters.");
        }
        return aliases.put(key, alias);
    }

    public String get(String key)
    {
        List<String> cache = new ArrayList<String>();
        return getWithAlias(key, cache);
    }

    private String getWithAlias(String key, List<String> cache)
    {
        if (cache.contains(key))
        {
            throw new KernelException("Cycle detected in kernel parameter aliases.");
        }
        cache.add(key);
        String alias = aliases.get(key);
        if (alias == null)
        {
            return params.get(key);
        } else
        {
            return getWithAlias(alias, cache);
        }
    }

    public String put(String key, String value)
    {
        return params.put(key, value);
    }

    public Map<String, String> toMap()
    {
        Map<String, String> map = new HashMap<String, String>(params);
        for (Map.Entry<String, String> entry : aliases.entrySet())
        {
            String alias = entry.getKey();
            String paramKey = entry.getValue();
            map.put(alias, get(paramKey));
        }
        return Collections.unmodifiableMap(map);
    }

    public boolean containsAllKeys(Collection<String> keys)
    {
        for (String key : keys)
        {
            if (!containsKey(key))
            {
                return false;
            }
        }
        return true;
    }

    public boolean containsKey(String key)
    {
        return aliases.containsKey(key) || params.containsKey(key);
    }

}