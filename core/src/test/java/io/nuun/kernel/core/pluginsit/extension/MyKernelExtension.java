/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.core.pluginsit.extension;

import io.nuun.kernel.spi.KernelExtension;

import java.util.Collection;

/**
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 *         05/01/2015
 */
public class MyKernelExtension implements KernelExtension<MyExtensionInterface>
{

    public int count = 0;

    @Override
    public void initializing(Collection<MyExtensionInterface> extensions)
    {
        count += 1;
    }

    @Override
    public void initialized(Collection<MyExtensionInterface> extensions)
    {
        count += 10;
    }

    @Override
    public void starting(Collection<MyExtensionInterface> extensions)
    {
        count += 100;
    }

    @Override
    public void started(Collection<MyExtensionInterface> extensions)
    {
        count += 1000;
    }

    @Override
    public void stopping(Collection<MyExtensionInterface> extensions)
    {
        count += 10000;
    }

    @Override
    public void stopped(Collection<MyExtensionInterface> extensions)
    {
        count += 100000;
    }
}
