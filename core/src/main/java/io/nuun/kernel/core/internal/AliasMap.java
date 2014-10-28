package io.nuun.kernel.core.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class AliasMap extends HashMap<String, String>
{
    private static final long serialVersionUID = 1L;
    Map<String, String>       aliases          = new HashMap<String, String>();

    /**
     * @param key
     *            the key to alias.
     * @param alias
     *            the alias to give to the key.
     * @return
     */
    public String putAlias(String key, String alias)
    {
        if (super.containsKey(alias))
        {
            throw new IllegalArgumentException("alias " + alias + " already exists in map.");
        }
        return aliases.put(alias, key);
    }

    @Override
    public String get(Object key)
    {
        String keyAlias = aliases.get(key);
        if (keyAlias == null)
        {
            return super.get(key);
        }
        else
        {
            return super.get(keyAlias);
        }
    }

    public boolean containsAllKeys(Collection<String> computedMandatoryParams)
    {
        HashSet<String> allKeys = new HashSet<String>();
        allKeys.addAll(keySet());
        allKeys.addAll(aliases.values());

        Collection<String> trans = new HashSet<String>();
        for (String s : computedMandatoryParams)
        {
            String string = aliases.get(s);
            if (string != null)
            {
                trans.add(string);
            }
        }

        return allKeys.containsAll(trans);
    }

    @Override
    public boolean containsKey(Object key)
    {
        return aliases.containsKey(key) ? true : super.containsKey(key);
    }

}