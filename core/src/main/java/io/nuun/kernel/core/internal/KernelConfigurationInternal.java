/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
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

import com.google.common.collect.ObjectArrays;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.config.DependencyInjectionMode;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.api.di.ModuleValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class KernelConfigurationInternal implements KernelConfiguration, KernelCoreMutator
{

    private final Logger              logger = LoggerFactory.getLogger(KernelConfiguration.class);
    List<String>                      parameters = new ArrayList<String>();
    private Object                    containerContext;
    private List<Class<? extends Plugin>> pluginsClass = new ArrayList<Class<? extends Plugin>>();
    private Plugin[]                  plugins = new Plugin[0];
    private boolean                   useSpi     = true;
    private DependencyInjectionMode   dependencyInjectionMode;
    private ClasspathScanMode         classpathScanMode = ClasspathScanMode.NOMINAL;
    private List<ModuleValidation> validations = new ArrayList<ModuleValidation>();
    
    @Override
    public KernelConfiguration param(String key, String value)
    {
        cleanParamsSize();

        parameters.add(key);
        parameters.add(value);
        
        return this;
    }

    private void cleanParamsSize()
    {
        if (parameters.size() % 2 != 0)
        {
            parameters.add("");
        }
    }

    @Override
    public KernelConfiguration params(String... paramEntries)
    {
        cleanParamsSize();

        Collections.addAll(parameters, paramEntries);
        return this;
    }

    @Override
    public KernelConfiguration containerContext(Object containerContext)
    {
        this.containerContext = containerContext;
        return this;
    }

    @Override
    public KernelConfiguration addPlugin(Class<? extends Plugin> pluginsClass) {
        Collections.addAll(this.pluginsClass, pluginsClass);
        return this;
    }

    @Override
    public KernelConfiguration plugins(Class<? extends Plugin>... pluginsClasses) {
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
        useSpi = false;
        return this;
    }

    @Override
    public KernelConfiguration withSpiPluginsLoader()
    {
        useSpi = true;
        return this;
    }

    @Override
    public KernelConfiguration dependencyInjectionMode(DependencyInjectionMode dependencyInjectionMode)
    {
        this.dependencyInjectionMode = dependencyInjectionMode;
        return this;
    }

    @Override
    public KernelConfiguration classpathScanMode(ClasspathScanMode classpathScanMode)
    {
        this.classpathScanMode = classpathScanMode;
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

    @Override
    public void apply(KernelCore kernelCore)
    {

        // Update parameters
        AliasMap kernelParamsAndAlias = kernelCore.paramsAndAlias();

        Iterator<String> it = parameters.iterator();
        while (it.hasNext())
        {
            String key = it.next();
            String value = "";
            if (it.hasNext())
            {
                value = it.next();
            }
            logger.info("Adding {} = {} as param to kernel", key, value);
            kernelParamsAndAlias.put(key, value);
        }
        
        kernelCore.addContainerContext(containerContext);
        kernelCore.addPlugins(pluginsClass);
        kernelCore.addPlugins(plugins);
        if (useSpi)
        {
            kernelCore.spiPluginEnabled();
        }
        else
        {
            kernelCore.spiPluginDisabled();
        }
        
        kernelCore.dependencyInjectionMode(dependencyInjectionMode);
        
        kernelCore.classpathScanMode(classpathScanMode);
        
        for ( ModuleValidation validation : validations)
        {
            kernelCore.provideGlobalDiDefValidation(validation);
        }

    }

}
