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

    public abstract boolean isStarted();

    public abstract boolean isInitialized();

    /**
     * 
     * 
     */
    public abstract void init();

    public abstract void start();

    public abstract Injector getMainInjector();

    /**
     * This methods will stop all the plugin in the reverse order of the sorted plugins.
     * 
     */
    public abstract void stop();

}