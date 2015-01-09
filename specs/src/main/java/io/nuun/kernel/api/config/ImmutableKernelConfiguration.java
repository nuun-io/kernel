/**
 * Copyright (C) 2014 Kametic <epo.jemba{@literal @}kametic.com>
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
package io.nuun.kernel.api.config;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.di.ModuleValidation;

import java.util.Map;
import java.util.Set;

/**
 * Read only version of the Kernel Configuration.
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public interface ImmutableKernelConfiguration
{
    /**
     * 
     * @return the map containing all kernel params.
     */
    Map<String,String> params ();
    
    /**
     * 
     * @return the container context associated with the
     */
    Object containerContext();
    
    /**
     * return the plugins classes expected by the configuration. Not the plugins loaded by the kernel via service loader mechanism.
     * 
     * @return the plugins classes expected by the configuration.
     */
    Set<Class<? extends Plugin>> pluginsClasses ();
    
    
    /**
     * return the plugins instances expected by the configuration. Not the plugins loaded by the kernel via service loader mechanism.
     * 
     * @return the plugins instances expected by the configuration.
     */
    Set<Plugin> plugins ();
    
    /**
     * 
     * @return true if configuration is using spi plugin loader.
     */
    boolean useSpiPluginsLoader ();
    
    /**
     * 
     * @return the dependency injection mode.
     */
    DependencyInjectionMode dependencyInjectionMode();
    
    /**
     * 
     * @return the classpath scan mode.
     */
    ClasspathScanMode classpathScanMode();
    
    /**
     * 
     * @return the {@link ModuleValidation} associated with the configuration.
     */
    ModuleValidation moduleValidation();
    
    
    
}
