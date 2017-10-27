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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.entrypoint3.NonNullImplementation;
import io.nuun.kernel.core.entrypoint3.NullableService;
import io.nuun.kernel.core.internal.topology.TopologyPlugin;

public class KernelSuite12Test
{

    private Kernel   underTest;

    private Injector injector;

    @Before
    public void initkernel()
    {
        underTest = createKernel(

                newKernelConfiguration().rootPackages("io.nuun.kernel.core.entrypoint3") //
                .withoutSpiPluginsLoader().plugins(new TopologyPlugin()));

        underTest.init();
        underTest.start();
        injector = underTest.objectGraph().as(Injector.class);
    }

    @Test
    public void dependee_plugins()
    {

        NullableService nullService = injector.getInstance(NullableService.class);
        assertThat(nullService).isNotNull();
        assertThat(nullService).isInstanceOf(NonNullImplementation.class);

        Optional<NullableService> optinalNullService = injector.getInstance(Key.get(new TypeLiteral<Optional<NullableService>>()
        {
        }));

        assertThat(optinalNullService).isNotNull();
        assertThat(optinalNullService.isPresent()).isTrue();
        assertThat(optinalNullService.get()).isInstanceOf(NonNullImplementation.class);
    }
}
