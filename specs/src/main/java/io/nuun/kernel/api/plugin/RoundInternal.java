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
 * Default implementation of {@link Round}.
 */
public class RoundInternal implements Round
{

    private static final int MAXIMAL_ROUND_NUMBER = 50;

    private int roundIndex = 0;

    @Override
    public int number()
    {
        return this.roundIndex;
    }

    /**
     * Increments the round number.
     */
    public void next()
    {
        if (isMax())
            throw new IndexOutOfBoundsException("Exceeded the maximal round number");
        this.roundIndex++;
    }

    @Override
    public boolean isFirst()
    {
        return this.roundIndex == 0;
    }

    @Override
    public boolean isMax() {
        return roundIndex == MAXIMAL_ROUND_NUMBER;
    }

}
