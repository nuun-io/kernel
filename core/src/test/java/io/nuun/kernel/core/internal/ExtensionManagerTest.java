/*
 * Copyright (C) 2014 Kametic <pierre.thirouin@gmail.com>
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

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.pluginsit.extension.DummyExtensionPlugin;
import io.nuun.kernel.core.pluginsit.extension.MyKernelExtension;
import io.nuun.kernel.spi.KernelExtension;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 */
public class ExtensionManagerTest
{

    private ExtensionManager underTest;

    @Before
    public void setup()
    {
        List<Plugin> plugins = new ArrayList<Plugin>();
        plugins.add(new DummyExtensionPlugin());
        underTest = new ExtensionManager(plugins, Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void kernel_extension_should_be_called()
    {
        Assertions.assertThat(underTest.getExtensions()).hasSize(1);

        KernelExtension kernelExtension = underTest.getExtensions().iterator().next();
        Assertions.assertThat(kernelExtension).isInstanceOf(MyKernelExtension.class);

        MyKernelExtension myKernelExtension = (MyKernelExtension) kernelExtension;
        Assertions.assertThat(myKernelExtension.count).isEqualTo(0);

        underTest.initializing();
        underTest.initialized();
        underTest.starting();
        underTest.started();
        underTest.stopping();
        underTest.stopped();

        Assertions.assertThat(myKernelExtension.count).isEqualTo(111111);
    }
}
