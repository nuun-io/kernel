/**
 * This file is part of Nuun IO Kernel Tests.
 *
 * Nuun IO Kernel Tests is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Tests is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Tests.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.tests.it;

/**
 * @author ejemba
 *
 */
public class NuunITException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    /**
     * 
     */
    public NuunITException()
    {
    }

    /**
     * @param arg0
     */
    public NuunITException(String arg0)
    {
        super(arg0);

    }

    /**
     * @param arg0
     */
    public NuunITException(Throwable arg0)
    {
        super(arg0);

    }

    /**
     * @param arg0
     * @param arg1
     */
    public NuunITException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);

    }

}
