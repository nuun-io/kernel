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

import io.nuun.kernel.api.config.KernelOptions;
import io.nuun.kernel.core.internal.concerns.ConcernTest;
import io.nuun.kernel.core.internal.concerns.sample.BugPlugin;
import io.nuun.kernel.core.internal.concerns.sample.CachePlugin;
import io.nuun.kernel.core.internal.concerns.sample.LogPlugin;
import io.nuun.kernel.core.internal.concerns.sample.SecurityPlugin;
import io.nuun.kernel.core.internal.injection.ClassInstaller;
import io.nuun.kernel.core.internal.injection.KernelGuiceModuleInternal;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalKernelModuleTest
{
    private KernelGuiceModuleInternal underTest;

    @Before
    public void init()
    {
        RequestHandler requestHandler = new RequestHandler(new HashMap<>(), new KernelOptions());
        underTest = new KernelGuiceModuleInternal(requestHandler);
    }

    @Test
    public void computeOrder_should_works()
    {
        assertThat(new ClassInstaller(SecurityPlugin.Module.class).order()).isEqualTo(12884901888L);
        assertThat(new ClassInstaller(LogPlugin.Module.class).order()).isEqualTo(-4294967296L);
        assertThat(new ClassInstaller(CachePlugin.Module.class).order()).isEqualTo(12884901886L);
        assertThat(new ClassInstaller(ConcernTest.Module.class).order()).isEqualTo(0);
        assertThat(new ClassInstaller(BugPlugin.Module.class).order()).isEqualTo(15032385535L);
    }
}
