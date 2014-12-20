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
