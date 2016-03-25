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
package io.nuun.kernel.core;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.core.internal.KernelConfigurationInternal;
import io.nuun.kernel.core.internal.KernelCoreFactory;

/**
 * NuunCore is a class used to create the Nuun core objects, ie. the kernel and its configuration.
 *
 * @author epo.jemba{@literal @}kametic.com
 */
public class NuunCore
{

    /**
     * Creates a kernel configuration which will be used to instantiate a new kernel.
     *
     * @return a kernelConfiguration
     */
    public static KernelConfiguration newKernelConfiguration()
    {
        return new KernelConfigurationInternal();
    }

    /**
     * Creates a kernel with the given configuration.
     *
     * @param configuration the kernel configuration
     * @return the kernel
     */
    public static Kernel createKernel(KernelConfiguration configuration)
    {
        KernelCoreFactory factory = new KernelCoreFactory();
        return factory.create(configuration);
    }
}
