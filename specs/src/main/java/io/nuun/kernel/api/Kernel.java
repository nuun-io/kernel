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

import io.nuun.kernel.api.di.GlobalModule;
import io.nuun.kernel.api.di.ObjectGraph;
import io.nuun.kernel.api.di.UnitModule;

import java.util.List;

/**
 *
 * The Kernel is the main component of the I.O.C. technical stack.
 * 
 * @author epo.jemba{@literal @}kametic.com
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
     * 
     * Tell the kernel to initialize. The kernel will load all the plugins and initialize them.
     * <p>
     * Plugins will the create an intermediate UnitModule.
     */
    public abstract void init();
    
    /**
     * Indication on whether or not the kernel is initialized.
     * 
     * @return true if the kernel is initialized.
     */
    public abstract boolean isInitialized();
    
    /**
     * After the kernel is initialized, if necessary, one can ask for the list of the plugins.
     * <p>
     * This will rarely be the case. We advise developers to be careful with plugins.
     * 
     * @return the list of plugins initialized.
     */
    public List<Plugin> plugins();
    
    /**
     * After the kernel is initialized, one can ask for the particular UnitModule created by one plugin.
     * <p>
     * This will rarely be the case. We advise developers to be careful with plugins.
     * 
     * @param plugin this is the plugin from which we want the UnitModule.
     * @return
     */
    public abstract UnitModule unitModule(Class<? extends Plugin> plugin);
    
    /**
     * After the kernel is initialized, one can ask for the particular Overriding UnitModule created by one plugin.
     * <p>
     * This will rarely be the case. We advise developers to be careful with plugins.
     * 
     * @param plugin this is the plugin from which we want the UnitModule.
     * @return
     */
    public abstract UnitModule overridingUnitModule(Class<? extends Plugin> plugin);
    
    /**
     * After the kernel is initialized, one can ask for the particular UnitModule created by one plugin.
     * <p>
     * Some times the plugin can return a native module that is not a Guice Module (the internal used D.I. engine).
     * <p>
     * Non Guice Module are handled via {@link io.nuun.kernel.spi.DependencyInjectionProvider}.
     * <p>
     * This will rarely be the case. We advise developers to be careful with plugins.
     * 
     * @param plugin this is the plugin from which we want the UnitModule.
     * @return
     */
    public abstract UnitModule nonGuiceUnitModule(Class<? extends Plugin> plugin);
    
    /**
     * After the kernel is initialized, one can ask for the particular Overriding UnitModule created by one plugin.
     * <p>
     * Some time the plugin can return a native module that is not a Guice Module (the internal used D.I. engine).
     * <p>
     * Non Guice Module are handled via {@link io.nuun.kernel.spi.DependencyInjectionProvider}.
     * <p>
     * This will rarely be the case. We advise developers to be careful with plugins.
     * 
     * @param plugin this is the plugin from which we want the UnitModule.
     * @return
     */
    public abstract UnitModule nonGuiceOverridingUnitModule(Class<? extends Plugin> plugin);

    /**
     * After the kernel is initialized, if necessary, one can ask for the global Module the result of all plugins {@link UnitModule} aggregation.
     * <p>
     * This will rarely be the case. We advise developers to be careful with plugins.
     * 
     * @return  the global binding definition provider for all the application.
     */
    public abstract GlobalModule globalModule();

    /**
     * Tell the kernel to start. Then the kernel will create the ObjectGraph of the application. The ObjectGraph will wrap the actual Guice injector.
     * <p>
     * The injector is created by combining nominal global modules and global modules to override.
     */
    public abstract void start();
    
    /**
     * Indication on whether or not the kernel is started.
     * 
     * @return true if the kernel is started.
     */
    public abstract boolean isStarted();

    /**
     * After the kernel is started, one can ask the ObjectGraph generated from the global module itself generated by all the modules the plugins create.
     * 
     * @return the ObjectGraph
     */
    public abstract ObjectGraph objectGraph();

    /**
     * This methods will stop all the plugins in the reverse order of the started plugins.
     */
    public abstract void stop();

}