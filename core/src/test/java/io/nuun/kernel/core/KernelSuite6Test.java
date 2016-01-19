/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
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
