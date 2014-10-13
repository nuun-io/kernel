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

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.pluginsit.dummy1.DummyPlugin;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin2;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin3;
import io.nuun.kernel.core.pluginsit.dummy4.DummyPlugin4;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class KernelMulticoreTest
{


    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error() throws InterruptedException
    {
        CountDownLatch startLatch = new CountDownLatch(1);
        for (int threadNo = 0; threadNo < 2; threadNo++) {
            Thread t = new KernelHolder(startLatch);
            t.start();
          }
          // give the threads chance to start up; we could perform
          // initialisation code here as well.
          Thread.sleep(200);
          startLatch.countDown();
    }
    
    static class KernelHolder extends Thread
    {
        public KernelHolder(CountDownLatch startLatch)
        {
        }
        
        
        @SuppressWarnings("unchecked")
        @Override
        public void run()
        {
            
//            try
            {
                System.out.println("Before");
//                startLatch.await();
                KernelCore underTest;
                DummyPlugin4 plugin4 = new DummyPlugin4();
                
                underTest = (KernelCore) createKernel(
                        //
                        newKernelConfiguration() //
                        .params (
                                DummyPlugin.ALIAS_DUMMY_PLUGIN1 , "WAZAAAA",
                                DummyPlugin.NUUNROOTALIAS       , "internal,"+KernelCoreTest.class.getPackage().getName()
                                )
                        );
                
                assertThat(underTest.name()).startsWith(Kernel.KERNEL_PREFIX_NAME);
                System.out.println(">" + underTest.name());
                
                underTest.addPlugins( DummyPlugin2.class);
                underTest.addPlugins( DummyPlugin3.class);
                underTest.addPlugins( plugin4);
                underTest.addPlugins( DummyPlugin5.class);
                underTest.init();
                
                assertThat(underTest.isInitialized()).isTrue();
                System.out.println(">" + underTest.name() + " initialized  = " + underTest.isInitialized());
                underTest.start();
                assertThat(underTest.isStarted()).isTrue();
                System.out.println(">" + underTest.name() + " started  = " + underTest.isStarted());
                underTest.stop();
            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
        }
        
        
    }


}
