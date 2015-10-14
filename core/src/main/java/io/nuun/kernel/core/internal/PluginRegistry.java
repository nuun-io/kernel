package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.KernelException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This registry holds all the plugin contained by the kernel.
 * It validates the added plugins and provides accessor methods.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
class PluginRegistry {

    public static final String NAME_UNIQUENESS_ERROR = "The kernel contains two plugins with the same name (%s): %s, %s";
    public static final String TYPE_UNIQUENESS_ERROR = "The kernel contains two plugins of type ";
    public static final String NAME_VALIDATION_ERROR = "The plugin %s doesn't have a correct name. It should not be null or empty.";

    private final Map<Class<? extends Plugin>, Plugin> pluginsByClass = new HashMap<Class<? extends Plugin>, Plugin>();
    private final Map<String, Plugin> pluginsByName = new HashMap<String, Plugin>();

    /**
     * Adds a plugin to the registry. It checks if the plugin name is valid
     * and if the plugin name and type are unique.
     *
     * @param plugin the plugin to add
     */
    void add(Plugin plugin) {
        assertNameNotBlank(plugin);

        Plugin previous = this.pluginsByClass.put(plugin.getClass(), plugin);
        if (previous != null) {
            throw new KernelException(TYPE_UNIQUENESS_ERROR + plugin.getClass().getCanonicalName());
        }

        Plugin previousPlugin = this.pluginsByName.put(plugin.name(), plugin);
        if (previousPlugin != null) {
            throw new KernelException(String.format(NAME_UNIQUENESS_ERROR, plugin.name(),
                    plugin.getClass().getCanonicalName(), previousPlugin.getClass().getCanonicalName()));
        }
    }

    private void assertNameNotBlank(Plugin plugin) {
        String name = plugin.name();
        if (name == null || name.equals("")) {
            throw new KernelException(NAME_VALIDATION_ERROR, plugin.getClass().getCanonicalName());
        }
    }

    /**
     * Returns the plugin instance corresponding to the given class.
     *
     * @param pluginClass the plugin class
     * @return the plugin instance
     */
    Plugin get(Class<? extends Plugin> pluginClass) {
        return pluginsByClass.get(pluginClass);
    }

    /**
     * Returns the plugin instance corresponding to the given name.
     *
     * @param name the plugin name
     * @return the plugin instance
     */
    Plugin get(String name) {
        return pluginsByName.get(name);
    }

    /**
     * Returns all the plugin instances.
     *
     * @return the plugin instances
     */
    Collection<Plugin> getPlugins() {
        return pluginsByClass.values();
    }

    /**
     * Returns all the plugin classes.
     *
     * @return the plugin classes
     */
    Collection<Class<? extends Plugin>> getPluginClasses() {
        return pluginsByClass.keySet();
    }

    public Map<String, Plugin> getPluginsByName() {
        return pluginsByName;
    }
}
