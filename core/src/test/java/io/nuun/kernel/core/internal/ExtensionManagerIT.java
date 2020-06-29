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

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.pluginsit.extension.DummyExtensionPlugin;
import io.nuun.kernel.core.pluginsit.extension.MyKernelExtension;
import io.nuun.kernel.spi.KernelExtension;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pierre Thirouin
 */
public class ExtensionManagerIT
{

    private ExtensionManager underTest;

    @Before
    public void setup()
    {
        List<Plugin> plugins = new ArrayList<>();
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
        underTest.injected();
        underTest.started();
        underTest.stopping();
        underTest.stopped();

        Assertions.assertThat(myKernelExtension.count).isEqualTo(1111111);
    }
}
