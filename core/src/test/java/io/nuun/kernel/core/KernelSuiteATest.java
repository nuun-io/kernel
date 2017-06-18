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

import static org.assertj.core.api.Assertions.assertThat;
import io.nuun.kernel.api.annotations.Entrypoint;
import io.nuun.kernel.core.entrypoint1.EntrypointPathLess;
import io.nuun.kernel.core.test_topo.MyService3;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;

public class KernelSuiteATest
{

    @Entrypoint(packageScan = "io.nuun.kernel.core.test_topo")
    static class App implements Runnable
    {
        @Inject
        @Named("key1")
        String     key1;

        @Inject
        MyService3 ms3;

        @Override
        public void run()
        {
            System.out.println("-> " + key1);
            assertThat(ms3.one()).isEqualTo("one");
            assertThat(ms3.one_aop()).isEqualTo("(one)");
            assertThat(App.class.getPackage().getName()).isEqualTo("io.nuun.kernel.core");
        }
    }

    @Test
    public void testEntrypoint()
    {
        NuunRunner.entrypoint(App.class).execute("--option1", "value1");
    }

    @Test
    public void testPathLessEntrypoint()
    {
        NuunRunner.entrypoint(EntrypointPathLess.class).execute();
    }

}
