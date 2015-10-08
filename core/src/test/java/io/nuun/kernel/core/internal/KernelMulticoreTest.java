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
package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.pluginsit.dummy1.DummyPlugin;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin2;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin3;
import io.nuun.kernel.core.pluginsit.dummy4.DummyPlugin4;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class KernelMulticoreTest
{
    private static CountDownLatch startLatch;

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error() throws InterruptedException
    {
        startLatch = new CountDownLatch(1);
        for (int threadNo = 0; threadNo < 3; threadNo++) {
            Thread t = new KernelHolder();
            t.start();
          }
          // give the threads chance to start up; we could perform
          // initialisation code here as well.
          Thread.sleep(200);
          startLatch.countDown();
    }
    
    static class KernelHolder extends Thread
    {

        @SuppressWarnings("unchecked")
        @Override
        public void run()
        {
            try
            {
                startLatch.await();
                KernelCore underTest;
                DummyPlugin4 plugin4 = new DummyPlugin4();
                
                underTest = (KernelCore) createKernel(
                        newKernelConfiguration()
                        .params (
                                DummyPlugin.ALIAS_DUMMY_PLUGIN1 , "WAZAAAA",
                                DummyPlugin.NUUN_ROOT_ALIAS, "internal,"+KernelCoreTest.class.getPackage().getName()
                                )
                        );
                
                assertThat(underTest.name()).startsWith(Kernel.KERNEL_PREFIX_NAME);

                underTest.addPlugin(DummyPlugin3.class);
                underTest.addPlugin(DummyPlugin2.class);
                underTest.addPlugin(plugin4);
                underTest.addPlugin(DummyPlugin5.class);

                underTest.init();
                
                assertThat(underTest.isInitialized()).isTrue();

                underTest.start();

                assertThat(underTest.isStarted()).isTrue();

                underTest.stop();
            }
            catch (InterruptedException e)
            {
                fail(e.getMessage());
            }
        }
    }
}
