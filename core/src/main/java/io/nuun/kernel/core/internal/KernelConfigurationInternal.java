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

import com.google.common.collect.ObjectArrays;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.*;
import io.nuun.kernel.api.di.ModuleValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class KernelConfigurationInternal implements KernelConfiguration
{

    private final Logger logger = LoggerFactory.getLogger(KernelConfiguration.class);
    private final AliasMap kernelParamsAndAlias = new AliasMap();

    private List<Class<? extends Plugin>> pluginsClass = new ArrayList<Class<? extends Plugin>>();
    private Plugin[] plugins = new Plugin[0];
    private List<ModuleValidation> validations = new ArrayList<ModuleValidation>();
    private Object containerContext;

    private KernelOptions options = new KernelOptions();

    @Override
    public <T> KernelConfiguration option(KernelOption<T> option, T value)
    {
        options.set(option, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public KernelOptions options()
    {
        return options;
    }

    @Override
    public KernelConfiguration rootPackages(String... rootPackages)
    {
        this.options.get(KernelOptions.ROOT_PACKAGES).addAll(Arrays.asList(rootPackages));
        return this;
    }

    @Override
    public KernelConfiguration param(String key, String value)
    {
        kernelParamsAndAlias.put(key, value);
        if (key.equals("nuun.root.package")) {
            options.get(KernelOptions.ROOT_PACKAGES).add(value);
        }
        return this;
    }

    @Override
    public KernelConfiguration params(String... paramEntries)
    {
        if (isNotEvenNumber(paramEntries.length))
        {
            throw new IllegalArgumentException("An even number of parameters was expected but found: "
                    + Arrays.toString(paramEntries));
        }

        Iterator<String> it = Arrays.asList(paramEntries).iterator();
        while (it.hasNext())
        {
            addParamKeyValue(it);
        }
        return this;
    }

    public boolean isNotEvenNumber(int number)
    {
        return number % 2 != 0;
    }

    private void addParamKeyValue(Iterator<String> it)
    {
        String key = it.next();
        String value = it.next();
        logger.debug("Adding {} = {} as param to kernel", key, value);
        param(key, value);
    }

    public AliasMap kernelParams()
    {
        return kernelParamsAndAlias;
    }

    @Override
    public KernelConfiguration containerContext(Object containerContext)
    {
        this.containerContext = containerContext;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public KernelConfiguration addPlugin(Class<? extends Plugin> pluginsClass)
    {
        Collections.addAll(this.pluginsClass, pluginsClass);
        return this;
    }

    @Override
    public KernelConfiguration plugins(Class<? extends Plugin>... pluginsClasses)
    {
        Collections.addAll(this.pluginsClass, pluginsClasses);
        return this;
    }

    @Override
    public KernelConfiguration addPlugin(Plugin plugin)
    {
        this.plugins = ObjectArrays.concat(plugin, this.plugins);
        return this;
    }

    @Override
    public KernelConfiguration plugins(Plugin... plugins)
    {
        this.plugins = ObjectArrays.concat(this.plugins, plugins, Plugin.class);
        return this;
    }

    @Override
    public KernelConfiguration withoutSpiPluginsLoader()
    {
        this.options.set(KernelOptions.SCAN_PLUGIN, false);
        return this;
    }

    @Override
    public KernelConfiguration withSpiPluginsLoader()
    {
        this.options.set(KernelOptions.SCAN_PLUGIN, true);
        return this;
    }

    @Override
    public KernelConfiguration dependencyInjectionMode(DependencyInjectionMode dependencyInjectionMode)
    {
        this.options.set(KernelOptions.DEPENDENCY_INJECTION_MODE, dependencyInjectionMode);
        return this;
    }

    @Override
    public KernelConfiguration classpathScanMode(ClasspathScanMode classpathScanMode)
    {
        this.options.set(KernelOptions.CLASSPATH_SCAN_MODE, classpathScanMode);
        return this;
    }

    @Override
    public KernelConfiguration moduleValidation(ModuleValidation validation)
    {
        if (validation != null)
        {
            validations.add(validation);
        }
        return this;
    }

    public Object getContainerContext()
    {
        return containerContext;
    }

    public List<Class<? extends Plugin>> getPluginClasses()
    {
        return pluginsClass;
    }

    public Plugin[] getPlugins()
    {
        return plugins;
    }

    public List<ModuleValidation> getValidations()
    {
        return validations;
    }
}
