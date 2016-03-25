/**
 * This file is part of Nuun IO Kernel Specs.
 *
 * Nuun IO Kernel Specs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Specs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Specs.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.api.plugin;

/**
 * InitState is returned by plugins when the kernel initializes them.
 * 
 * @author epo.jemba{@literal @}kametic.com
 */
public enum InitState
{
    /**
     * Returned by the plugin to indicate that its initialization is complete.
     */
    INITIALIZED,
    
    /**
     * Returned by the plugin to indicate that its initialization is not yet complete.
     * <p>
     * In short, the kernel will program a new initialization round.
     */
    NON_INITIALIZED
}
