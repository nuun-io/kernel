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