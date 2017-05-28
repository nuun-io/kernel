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
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.internal.topology.TopologyPlugin;
import io.nuun.kernel.core.test_topo.sample.MyService;
import io.nuun.kernel.core.test_topo.sample.MyService2;
import io.nuun.kernel.core.test_topo.sample.MyServiceImpl;
import io.nuun.kernel.core.test_topo.sample.MyServiceImpl2;
import io.nuun.kernel.core.test_topo.sample.MyServiceImpl2Bis;
import io.nuun.kernel.core.test_topo.sample.Server;
import io.nuun.kernel.core.test_topo.sample.Serveur;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class KernelSuite9Test
{

    private Kernel underTest;

    @Before
    public void initkernel()
    {
        underTest = createKernel(

        newKernelConfiguration().rootPackages("io.nuun.kernel.core.test_topo") //
                .withoutSpiPluginsLoader().plugins(new TopologyPlugin()));

        underTest.init();
        underTest.start();
    }

    @Test
    public void topology_should_handle_aop_via_method_interceptor()
    {
        Injector injector = underTest.objectGraph().as(Injector.class);

    }

    @Test
    public void topoly_should_work_with_provider_binding()
    {
        Injector injector = underTest.objectGraph().as(Injector.class);

        Object instance = injector.getInstance(Key.get(MyService2.class));
        assertThat(instance).isNotNull();
        assertThat(instance).isInstanceOf(MyServiceImpl2.class);

        instance = injector.getInstance(Key.get(MyService2.class, Server.class));
        assertThat(instance).isNotNull();
        assertThat(instance).isInstanceOf(MyServiceImpl2Bis.class);
    }

    @Test
    public void topoly_should_work_with_instance_binding()
    {
        Injector injector = underTest.objectGraph().as(Injector.class);

        //
        Integer theAnswer = injector.getInstance(Key.get(Integer.class));

        assertThat(theAnswer).isNotNull();
        assertThat(theAnswer).isEqualTo(42);

        //
        Long port = injector.getInstance(Key.get(Long.class, Server.class));
        assertThat(port).isNotNull();
        assertThat(port).isEqualTo(8080l);

        //
        String url = injector.getInstance(Key.get(String.class, Serveur.class));
        assertThat(url).isNotNull();
        assertThat(url).isEqualTo("http://localhost.local");

        //
        String context = injector.getInstance(Key.get(String.class, Names.named("main")));
        assertThat(context).isNotNull();
        assertThat(context).isEqualTo("cli");
    }

    @Test
    public void topoly_should_work_with_linked_binding()
    {
        Injector injector = underTest.objectGraph().as(Injector.class);
        // MyServiceImpl injects(MyService key);
        Object myService = injector.getInstance(Key.get(MyService.class));

        assertThat(myService).isNotNull();
        assertThat(myService).isInstanceOf(MyServiceImpl.class);

        // MyServiceImpl2 injectsTwo(@Named("two") MyService key);
        Object myService2 = injector.getInstance(Key.get(MyService.class, Names.named("two")));
        assertThat(myService2).isNotNull();
        assertThat(myService2).isInstanceOf(MyServiceImpl2.class);
    }

    @After
    public void stopKernel()
    {
        underTest.stop();
    }

}
