/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.api.plugin;
/**
 * InitState will be returned back by plugins when when kernel will initialize them.
 * 
 * @author epo.jemba@kametic.com
 *
 */
public enum InitState
{
    /**
     * returned by the plugin to indicate that its initialization is complete.
     */
    INITIALIZED ,
    
    /**
     * returned by the plugin to indicate that its initialization is not yet complete.
     * <p>
     * In short, the kernel will program a new initialization round.
     */
    NON_INITIALIZED
}
