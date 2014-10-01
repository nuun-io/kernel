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

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.config.DependencyInjectionMode;
import io.nuun.kernel.api.config.KernelConfiguration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author epo.jemba@kametic.com
 */
public class KernelConfigurationInternal implements KernelConfiguration, KernelCoreMutator
{

    private final Logger              logger;
    List<String>                      parameters = new ArrayList<String>();
    private Object                    containerContext;
    @SuppressWarnings("unchecked")
    private Class<? extends Plugin>[] pluginsClass = new Class[0];
    private Plugin[]                  plugins = new Plugin[0];
    private boolean                   useSpi     = true;
    private DependencyInjectionMode   dependencyInjectionMode;
    private ClasspathScanMode         classpathScanMode = ClasspathScanMode.NOMINAL;

    public KernelConfigurationInternal()
    {
        logger                 = LoggerFactory.getLogger( KernelConfiguration.class);
    }
    
    
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

        for (String entry : paramEntries)
        {
            parameters.add(entry);
        }
        return this;
    }

    @Override
    public KernelConfiguration containerContext(Object containerContext)
    {
        this.containerContext = containerContext;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public KernelConfiguration plugins(Class<? extends Plugin>... pluginsClass)
    {
        this.pluginsClass = concat(this.pluginsClass, pluginsClass);
        return this;
    }

    @Override
    public KernelConfiguration plugins(Plugin... plugins)
    {
        this.plugins = this.<Plugin>concat(this.plugins, plugins);
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

    }

    private <T> T[] concat(T[] A, T[] B)
    {
        int aLen = A.length;
        int bLen = B.length;
                
        T[] C =  (T[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen) ;
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

}
