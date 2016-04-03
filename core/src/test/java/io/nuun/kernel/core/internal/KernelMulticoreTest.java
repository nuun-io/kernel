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
package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.pluginsit.dummy1.DummyPlugin;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin2;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin3;
import io.nuun.kernel.core.pluginsit.dummy4.DummyPlugin4;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

public class KernelMulticoreTest
{
    private static final int KERNEL_NUMBER = 3;
    private static CountDownLatch startLatch = new CountDownLatch(1);
    private static CountDownLatch stopLatch = new CountDownLatch(KERNEL_NUMBER);
    private static AtomicInteger failureCount = new AtomicInteger(0);

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error() throws InterruptedException
    {
        startLatch = new CountDownLatch(1);
        for (int threadNo = 0; threadNo < KERNEL_NUMBER; threadNo++)
        {
            Thread t = new KernelHolder();
            t.start();
        }
        // give the threads chance to start up; we could perform
        // initialisation code here as well.
        Thread.sleep(200);
        startLatch.countDown();
        stopLatch.await();
        Assertions.assertThat(failureCount.get()).isEqualTo(0);
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
                                .params(
                                        DummyPlugin.ALIAS_DUMMY_PLUGIN1, "WAZAAAA",
                                        DummyPlugin.NUUN_ROOT_ALIAS, "internal," + KernelCoreIT.class.getPackage().getName()
                                )
                                .addPlugin(DummyPlugin.class)
                                .addPlugin(DummyPlugin3.class)
                                .addPlugin(DummyPlugin2.class)
                                .addPlugin(plugin4)
                                .addPlugin(DummyPlugin5.class)
                );

                assertThat(underTest.name()).startsWith(Kernel.KERNEL_PREFIX_NAME);

                underTest.init();

                assertThat(underTest.isInitialized()).isTrue();

                underTest.start();

                assertThat(underTest.isStarted()).isTrue();

                underTest.stop();
            } catch (Exception e)
            {
                failureCount.incrementAndGet();
                Assertions.fail(e.getMessage());
            } catch (AssertionError e)
            {
                failureCount.incrementAndGet();
                throw e;
            } finally
            {
                stopLatch.countDown();
            }
        }
    }
}
