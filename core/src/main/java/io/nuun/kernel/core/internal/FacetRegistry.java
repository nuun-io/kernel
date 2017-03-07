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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.annotations.Facet;

import javax.annotation.Nullable;
import java.util.*;

/**
 * This registry takes a list of plugins and extracts all its facets.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
class FacetRegistry
{
    private final ListMultimap<Class<?>, Plugin> pluginsByFacet = ArrayListMultimap.create();
    private final Map<Class<?>, Plugin> pluginsByClass = new HashMap<>();

    /**
     * Constructs a facet registry.
     *
     * @param plugins the list of plugin
     */
    FacetRegistry(final Collection<Plugin> plugins)
    {
        for (Plugin plugin : plugins)
        {
            this.pluginsByClass.put(plugin.getClass(), plugin);
            collectFacets(plugin);
        }
    }

    private void collectFacets(Plugin plugin)
    {
        for (Class<?> anInterface : plugin.getClass().getInterfaces())
        {
            if (isFacet(anInterface))
            {
                pluginsByFacet.put(anInterface, plugin);
            }
        }
    }

    /**
     * Returns the plugin implementing the given facet.
     * <p>
     * In order to provide more flexibility, the given class can also
     * be a Plugin class. In this case, its implementation will be directly
     * returned if present.
     * </p>
     *
     * @param facet the facet class
     * @param <T>   the facet type
     * @return the facet implementation or null if not present
     * @throws java.lang.IllegalStateException if more than one implementation exists for the facet
     */
    <T> T getFacet(@Nullable Class<T> facet) throws IllegalStateException
    {
        T plugin = null;
        if (facet != null)
        {
            List<T> plugins = getFacets(facet);
            if (plugins.size() > 1)
            {
                throw new IllegalStateException(String.format("One implementation was expected for the %s facet, but found: %s", facet.getSimpleName(), plugins.toString()));
            } else if (plugins.size() == 1)
            {
                plugin = plugins.get(0);
            }
        }
        return plugin;
    }

    /**
     * Returns the list of plugins implementing the given facet.
     * <p>
     * In order to provide more flexibility, the given class can also
     * be a Plugin class. In this case, its implementation will be directly
     * returned if present.
     * </p>
     *
     * @param facet the facet class
     * @param <T>   the facet type
     * @return the list of facet implementations, or an empty list if not present
     */
    @SuppressWarnings("unchecked")
    <T> List<T> getFacets(@Nullable Class<T> facet)
    {
        if (facet != null && Plugin.class.isAssignableFrom(facet))
        {
            List<T> ts = new ArrayList<>();
            Plugin plugin = pluginsByClass.get(facet);
            if (plugin != null)
            {
                ts.add((T) plugin);
            }
            return ts;
        }
        return (List<T>) pluginsByFacet.get(facet);
    }

    private boolean isFacet(Class<?> aClass)
    {
        return aClass.isAnnotationPresent(Facet.class);
    }
}
