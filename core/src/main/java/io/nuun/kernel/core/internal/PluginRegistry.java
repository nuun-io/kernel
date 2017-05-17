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
class PluginRegistry
{
    private static final String NAME_UNIQUENESS_ERROR = "The kernel contains two plugins with the same name (%s): %s, %s";
    private static final String TYPE_UNIQUENESS_ERROR = "The kernel contains two plugins of type ";
    private static final String NAME_VALIDATION_ERROR = "The plugin %s doesn't have a correct name. It should not be null or empty.";

    private final Map<Class<? extends Plugin>, Plugin> pluginsByClass = new HashMap<>();
    private final Map<String, Plugin> pluginsByName = new HashMap<>();

    void add(Class<? extends Plugin> pluginClass)
    {
        try
        {
            add(pluginClass.newInstance());
        } catch (InstantiationException e)
        {
            throw new KernelException("Plugin %s can not be instantiated", pluginClass);
        } catch (IllegalAccessException e)
        {
            throw new KernelException("Plugin %s can not be instantiated", pluginClass);
        }
    }

    /**
     * Adds a plugin to the registry. It checks if the plugin name is valid
     * and if the plugin name and type are unique.
     *
     * @param plugin the plugin to add
     */
    void add(Plugin plugin)
    {
        assertNameNotBlank(plugin);
        indexPluginByClass(plugin);
        indexPluginByName(plugin);
    }

    private void assertNameNotBlank(Plugin plugin)
    {
        String name = plugin.name();
        if (name == null || name.equals(""))
        {
            throw new KernelException(NAME_VALIDATION_ERROR, plugin.getClass().getCanonicalName());
        }
    }

    private void indexPluginByName(Plugin plugin)
    {
        Plugin alreadyExistingPlugin = this.pluginsByName.put(plugin.name(), plugin);
        if (alreadyExistingPlugin != null)
        {
            throw new KernelException(String.format(NAME_UNIQUENESS_ERROR, plugin.name(),
                    plugin.getClass().getCanonicalName(), alreadyExistingPlugin.getClass().getCanonicalName()));
        }
    }

    private void indexPluginByClass(Plugin plugin)
    {
        Plugin alreadyExistingPlugin = this.pluginsByClass.put(plugin.getClass(), plugin);
        if (alreadyExistingPlugin != null)
        {
            throw new KernelException(TYPE_UNIQUENESS_ERROR + plugin.getClass().getCanonicalName());
        }
    }

    /**
     * Returns the plugin injected corresponding to the given class.
     *
     * @param pluginClass the plugin class
     * @return the plugin injected
     */
    Plugin get(Class<? extends Plugin> pluginClass)
    {
        return pluginsByClass.get(pluginClass);
    }

    /**
     * Returns the plugin injected corresponding to the given name.
     *
     * @param name the plugin name
     * @return the plugin injected
     */
    Plugin get(String name)
    {
        return pluginsByName.get(name);
    }

    Collection<Plugin> getPlugins()
    {
        return pluginsByClass.values();
    }

    Collection<Class<? extends Plugin>> getPluginClasses()
    {
        return pluginsByClass.keySet();
    }

    public Map<String, Plugin> getPluginsByName()
    {
        return pluginsByName;
    }
}
