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
package io.nuun.kernel.api;

import io.nuun.kernel.api.di.ModuleProvider;

import java.util.List;

import com.google.inject.Injector;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public interface Kernel
{

    public static final String NUUN_ROOT_PACKAGE     = "nuun.root.package";
    public static final String NUUN_NUM_CP_PATH      = "nuun.num.classpath.path";
    public static final String NUUN_CP_PATH_PREFIX   = "nuun.classpath.path.prefix-";
    public static final String NUUN_CP_STRATEGY_NAME = "nuun.classpath.strategy.name";
    public static final String NUUN_CP_STRATEGY_ADD  = "nuun.classpath.strategy.additional";
    public static final String KERNEL_PREFIX_NAME    = "Kernel-";

    /**
     * The name of the kernel is determined with its
     * 
     * @return the name of the Kernel.
     */
    public abstract String name();
    
    /**
     * Indication on whether or not the kernel is started.
     * 
     * @return true if the kernel is started.
     */
    public abstract boolean isStarted();

    /**
     * Indication on whether or not the kernel is initialized.
     * 
     * @return true if the kernel is initialized.
     */
    public abstract boolean isInitialized();

    /**
     * 
     * Tell the kernel to initialize. The kernel will load all the plugins and initialize them.
     */
    public abstract void init();
    
    /**
     * After the kernel is initialized, one can ask for the list of the plugins. 
     * 
     * @return the list of plugins initialized.
     */
    public List<Plugin> plugins();
    
    /**
     * After the kernel is initialized, one can ask for the particular ModuleProvider created by one plugin.
     * 
     * @param plugin 
     * @return 
     */
    public abstract ModuleProvider getModuleProvider(Class<? extends Plugin> plugin);

    /**
     * After the kernel is initialized, one can ask for the global ModuleProvider the result of all plugins. 
     * 
     * @return  the global binding definition provider for all the application.
     */
    public abstract ModuleProvider getGlobalModuleProvider();
    
    /**
     * Tell the kernel to start.
     */
    public abstract void start();

    /**
     * After the kernel is started, ones can ask the ObjectGraphProvider.  
     * 
     * @return the ObjectGraphProvider
     */
    public abstract Injector getMainInjector();

    /**
     * This methods will stop all the plugin in the reverse order of the started plugins.
     */
    public abstract void stop();

}