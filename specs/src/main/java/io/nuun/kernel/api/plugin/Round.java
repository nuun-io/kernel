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
 * Plugins are initialized by the kernel in a loop. This loop computes
 * plugin requests then execute the plugin {@code init()} method until
 * the plugin is initialized.
 * <p>
 * This class represents the current loop number.
 * </p>
 *
 * @author epo.jemba{@literal @}kametic.com
 */
public interface Round
{

    /**
     * The current round number.
     *
     * @return the round number
     */
    public int number();

    /**
     * Indicates if this is the first round.
     *
     * @return true if it's the first round, false otherwise.
     */
    public boolean isFirst();

    boolean isMax();
}
