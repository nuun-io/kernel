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

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.internal.topology.TopologyPlugin;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class KernelSuite10Test
{

    private Kernel underTest;

    @Test
    public void initkernel_should_failed()
    {
        underTest = createKernel(

        newKernelConfiguration().rootPackages("io.nuun.kernel.core.error1_test_topo") //
                .withoutSpiPluginsLoader().plugins(new TopologyPlugin()));

        try
        {
            underTest.init();
            Assertions.failBecauseExceptionWasNotThrown(KernelException.class);
        }
        catch (KernelException e)
        {
            Assertions.assertThat(e.getMessage()).contains("Predicate");
        }

    }

}
