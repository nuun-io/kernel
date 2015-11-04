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
class FacetRegistry {

    private final ListMultimap<Class<?>, Plugin> facets = ArrayListMultimap.create();
    private final Map<Class<?>, Plugin> plugins = new HashMap<Class<?>, Plugin>();

    /**
     * Constructs a facet registry.
     *
     * @param plugins the list of plugin
     */
    FacetRegistry(final Collection<Plugin> plugins) {
        if (plugins != null) {
            for (Plugin plugin : plugins) {
                this.plugins.put(plugin.getClass(), plugin);

                for (Class<?> anInterface : plugin.getClass().getInterfaces()) {
                    if (isFacet(anInterface)) {
                        facets.put(anInterface, plugin);
                    }
                }
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
    <T> T getFacet(@Nullable Class<T> facet) throws IllegalStateException {
        T plugin = null;
        if (facet != null) {
            List<T> plugins = getFacets(facet);
            if (plugins.size() > 1) {
                throw new IllegalStateException("One implementation was expected for the " + facet.getSimpleName() + " facet, but found: " + plugins.toString());
            } else if (plugins.size() == 1) {
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
    <T> List<T> getFacets(@Nullable Class<T> facet) {
        if (facet != null && Plugin.class.isAssignableFrom(facet)) {
            List<T> ts = new ArrayList<T>();
            Plugin plugin = plugins.get(facet);
            if (plugin != null) {
                //noinspection unchecked
                ts.add((T) plugin);
            }
            return ts;
        }
        //noinspection unchecked
        return (List<T>) facets.get(facet);
    }

    private boolean isFacet(Class<?> aClass) {
        return aClass.isAnnotationPresent(Facet.class);
    }
}
