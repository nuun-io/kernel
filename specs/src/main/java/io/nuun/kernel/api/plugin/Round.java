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
 * Plugins are initialized by the kernel in a loop. This loop computes
 * plugin requests then execute the plugin {@code init()} method until
 * the plugin is initialized.
 * <p>
 * This class represents the current loop index.
 * </p>
 *
 * @author epo.jemba{@literal @}kametic.com
 */
public interface Round
{

    /**
     * The current round number.
     *
     * @return the round index
     */
    public int index();

    /**
     * Indicates if this is the first round.
     *
     * @return true if it's the first round, false otherwise.
     */
    public boolean isFirst();
    
}
