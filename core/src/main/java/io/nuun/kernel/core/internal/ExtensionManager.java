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
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.spi.KernelExtension;

import java.util.*;

/**
 * The ExtensionManager handles the kernel extensions.
 *
 * @author Pierre Thirouin {@literal<pierre.thirouin@ext.mpsa.com>}
 */
@SuppressWarnings("unchecked")
public class ExtensionManager
{

    private final Collection<Plugin>                         fetchedPlugins;
    private final ClassLoader                          contextClassLoader;
    private final Multimap<KernelExtension<?>, Plugin> kernelExtensions = ArrayListMultimap.create();

    /**
     * Constructor.
     *
     * @param fetchedPlugins the plugin list
     * @param contextClassLoader the context classloader
     */
    public ExtensionManager(Collection<Plugin> fetchedPlugins, ClassLoader contextClassLoader)
    {
        this.fetchedPlugins = fetchedPlugins;
        this.contextClassLoader = contextClassLoader;

        fetchExtensions();
    }

    private void fetchExtensions()
    {

        ServiceLoader<KernelExtension> kernelExtensionLoader = ServiceLoader.load(KernelExtension.class, contextClassLoader);
        Map<Class<?>, KernelExtension<?>> extensionMapping = new HashMap<>();
        for (KernelExtension<?> kernelExtension : kernelExtensionLoader)
        {
            Class<?> extensionInterface = TypeToken.of(kernelExtension.getClass()).resolveType(KernelExtension.class.getTypeParameters()[0]).getRawType();
            extensionMapping.put(extensionInterface, kernelExtension);
        }

        for (Plugin plugin : fetchedPlugins)
        {
            for (Class<?> anInterface : plugin.getClass().getInterfaces())
            {
                if(extensionMapping.containsKey(anInterface))
                {
                    KernelExtension<?> kernelExtension = extensionMapping.get(anInterface);

                    kernelExtensions.put(kernelExtension, plugin);
                }
            }
        }
    }

    /**
     * Notifies the extensions that the kernel is initializing
     */
    public void initializing()
    {
        for (KernelExtension kernelExtension : kernelExtensions.keySet())
        {
            Collection<Plugin> plugins = kernelExtensions.get(kernelExtension);
            kernelExtension.initializing(plugins);
        }
    }

    /**
     * Notifies the extensions that the kernel is initialized
     */
    public void initialized()
    {
        for (KernelExtension kernelExtension : kernelExtensions.keySet())
        {
            kernelExtension.initialized(kernelExtensions.get(kernelExtension));
        }
    }

    /**
     * Notifies the extensions that the kernel is starting
     */
    public void starting()
    {
        for (KernelExtension kernelExtension : kernelExtensions.keySet())
        {
            kernelExtension.starting(kernelExtensions.get(kernelExtension));
        }
    }

    /**
     * Notifies the extensions that the kernel is started
     */
    public void started()
    {
        for (KernelExtension kernelExtension : kernelExtensions.keySet())
        {
            kernelExtension.started(kernelExtensions.get(kernelExtension));
        }
    }

    /**
     * Notifies the extensions that the kernel is stopping
     */
    public void stopping()
    {
        for (KernelExtension kernelExtension : kernelExtensions.keySet())
        {
            kernelExtension.stopping(kernelExtensions.get(kernelExtension));
        }
    }

    /**
     * Notifies the extensions that the kernel is stopped
     */
    public void stopped()
    {
        for (KernelExtension kernelExtension : kernelExtensions.keySet())
        {
            kernelExtension.stopped(kernelExtensions.get(kernelExtension));
        }
    }

    /**
     * Gets enabled extensions.
     *
     * @return set of kernel extensions
     */
    public Set<KernelExtension<?>> getExtensions()
    {
        return kernelExtensions.keySet();
    }
}
