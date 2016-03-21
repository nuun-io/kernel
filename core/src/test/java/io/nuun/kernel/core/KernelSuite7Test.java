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
import com.google.inject.Key;
import com.google.inject.name.Names;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.pluginsit.dummy7.DummyPlugin7_A;
import io.nuun.kernel.core.pluginsit.dummy7.DummyPlugin7_B;
import org.junit.After;
import org.junit.Test;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

public class KernelSuite7Test
{

    private Kernel underTest;

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error()
    {

        underTest = createKernel(

                newKernelConfiguration()
                        .rootPackages("io.nuun.kernel")
                        .withoutSpiPluginsLoader()
                        .plugins(
                                new DummyPlugin7_A(),
                                new DummyPlugin7_B()
                        )
        );

        underTest.init();
        underTest.start();

        String resa = underTest.objectGraph().as(Injector.class).getInstance(Key.get(String.class, Names.named("dep7a")));
        assertThat(resa).isNotNull();
        assertThat(resa).isEqualTo("dep7aOVER");
    }

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error_()
    {
        underTest = createKernel(

                newKernelConfiguration()
                        .rootPackages("io.nuun.kernel")
                        .withoutSpiPluginsLoader()
                        .plugins(
                                new DummyPlugin7_A(),
                                new DummyPlugin7_B()
                        )
        );
        underTest.init();
        underTest.start();

        String resa = underTest.objectGraph().as(Injector.class).getInstance(Key.get(String.class, Names.named("dep7b")));
        assertThat(resa).isNotNull();
        assertThat(resa).isEqualTo("dep7bOVER");
    }

    @After
    public void stopKernel()
    {
        underTest.stop();
    }

}
