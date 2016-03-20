/*
 * Copyright (C) 2013-2016 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.core.internal;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.api.config.KernelOptions;
import io.nuun.kernel.core.NuunCore;

public class Fixture
{
    public static KernelConfiguration config() {
        return NuunCore.newKernelConfiguration()
                .option(KernelOptions.ROOT_PACKAGES, Lists.newArrayList("io.nuun.kernel"))
                .option(KernelOptions.SCAN_PLUGIN, false);
    }

    public static KernelConfiguration configWithScan() {
        return NuunCore.newKernelConfiguration()
                .option(KernelOptions.ROOT_PACKAGES, Lists.newArrayList("io.nuun.kernel"));
    }

    public static Kernel startKernel(KernelConfiguration kernelConfiguration) {
        Kernel kernel = NuunCore.createKernel(kernelConfiguration);
        kernel.init();
        kernel.start();
        return kernel;
    }

    public static Kernel initKernel(KernelConfiguration kernelConfiguration) {
        Kernel kernel = NuunCore.createKernel(kernelConfiguration);
        kernel.init();
        return kernel;
    }
}
