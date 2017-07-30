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
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.core.entrypoint2.NullableService;
import io.nuun.kernel.core.entrypoint2.TopologyNullable01;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;

import org.junit.Before;
import org.junit.Test;

public class KernelSuite11Test
{

    @Before
    public void init()
    {
        new ClasspathBuilder()
        {
            @Override
            public void configure()
            {
                addJar("test.jar");
                addClass(TopologyNullable01.class);
                addClass(NullableService.class);
                addDirectory("META-INF");
                addResource(base, name);
                addClass(candidate);
            }
        }.configure();
    }

    @Test
    public void dependee_plugins()
    {

        Kernel underTest = createKernel(

        newKernelConfiguration().rootPackages("io.nuun.kernel").classpathScanMode(ClasspathScanMode.IN_MEMORY).withoutSpiPluginsLoader()
                .plugins(new DummyPlugin5()));

        underTest.init();
        underTest.start();
    }

}
