/*
 * Copyright (C) 2014 Kametic <pierre.thirouin@gmail.com>
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
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 */
@SuppressWarnings("unchecked")
public class ExtensionManager
{

    private final List<Plugin>                         fetchedPlugins;
    private final ClassLoader                          contextClassLoader;
    private final Multimap<KernelExtension<?>, Plugin> kernelExtensions = ArrayListMultimap.create();

    /**
     * Constructor.
     *
     * @param fetchedPlugins the plugin list
     */
    public ExtensionManager(List<Plugin> fetchedPlugins, ClassLoader contextClassLoader)
    {
        this.fetchedPlugins = fetchedPlugins;
        this.contextClassLoader = contextClassLoader;

        fetchExtensions();
    }

    private void fetchExtensions()
    {

        ServiceLoader<KernelExtension> kernelExtensionLoader = ServiceLoader.load(KernelExtension.class, contextClassLoader);
        Map<Class<?>, KernelExtension<?>> extensionMapping = new HashMap<Class<?>, KernelExtension<?>>();
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
