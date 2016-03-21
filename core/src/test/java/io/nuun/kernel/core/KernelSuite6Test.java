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

import com.google.inject.Injector;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.pluginsit.dummy6.*;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

public class KernelSuite6Test
{
    private Kernel underTest;

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error()
    {
        try
        {
            underTest = createKernel(newKernelConfiguration()
                    .rootPackages("io.nuun.kernel")
                    .withoutSpiPluginsLoader()
                    .plugins(new DummyPlugin6_A(), new DummyPlugin6_B(), new DummyPlugin6_D()));
            underTest.init();
            Assertions.fail("The kernel should not be initialized");
        } catch (KernelException ke)
        {
            assertThat(ke).hasMessage("Plugin dummy-6-B misses the following dependency: " + DummyPlugin6_C.class.getCanonicalName());
        }
    }

    @Test
    public void dependee_plugins_should_start()
    {
        DummyPlugin6_A pluginA = new DummyPlugin6_A();
        DummyPlugin6_B pluginB = new DummyPlugin6_B();
        DummyPlugin6_C pluginC = new DummyPlugin6_C();
        DummyPlugin6_D pluginD = new DummyPlugin6_D();

        underTest = createKernel(newKernelConfiguration()
                .rootPackages("io.nuun.kernel")
                .withoutSpiPluginsLoader()
                .plugins(pluginA, pluginB, pluginC, pluginD));

        assertThat(pluginC.isInternal()).isFalse();
        assertThat(pluginD.isInternal()).isFalse();
        underTest.init();
        // Plugin B has initialized the C and D
        assertThat(pluginC.isInternal()).isTrue();
        assertThat(pluginD.isInternal()).isTrue();
        underTest.start();

        T2 instance = underTest.objectGraph().as(Injector.class).getInstance(T2.class);
        assertThat(instance).isNotNull();
    }

    @After
    public void stopKernel()
    {
        if (underTest.isStarted())
        {
            underTest.stop();
        }
    }
}
