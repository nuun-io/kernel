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

import com.google.common.collect.Lists;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.config.DependencyInjectionMode;
import io.nuun.kernel.api.config.KernelOptions;
import io.nuun.kernel.core.NuunCore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Pierre THIROUIN (pierre.thirouin@ext.inetpsa.com)
 */
public class KernelConfigurationTest
{

    private KernelConfigurationInternal underTest = new KernelConfigurationInternal();

    @Test
    public void testConfigurationOptions() throws Exception
    {
        KernelConfigurationInternal config = (KernelConfigurationInternal) NuunCore.newKernelConfiguration()
                .option(KernelOptions.ENABLE_REFLECTION_LOGGER, true)
                .option(KernelOptions.ROOT_PACKAGES, Lists.newArrayList("io.nuun", "org.seedstack"))
                .option(KernelOptions.CLASSPATH_SCAN_MODE, ClasspathScanMode.IN_MEMORY);

        KernelOptions options = config.options();
        assertThat(options.get(KernelOptions.ENABLE_REFLECTION_LOGGER)).isTrue();
        assertThat(options.get(KernelOptions.ROOT_PACKAGES)).containsOnly("io.nuun", "org.seedstack");
        assertThat(options.get(KernelOptions.CLASSPATH_SCAN_MODE)).isEqualTo(ClasspathScanMode.IN_MEMORY);
        assertThat(options.get(KernelOptions.DEPENDENCY_INJECTION_MODE)).isEqualTo(DependencyInjectionMode.PRODUCTION);
    }

    @Test
    public void testEmptyParamsNotNull() throws Exception
    {
        AliasMap params = underTest.kernelParams();
        assertThat(params).isNotNull();
    }

    @Test
    public void testNullParams() throws Exception
    {
        underTest.param("key1", null);
        underTest.param("key2", null);
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isNull();
        assertThat(params.get("key2")).isNull();
    }

    @Test
    public void testMissingParamValue() throws Exception
    {
        try
        {
            underTest.params("key1");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e)
        {
            assertThat(e).hasMessage("An even number of parameters was expected but found: " + Lists.newArrayList("key1").toString());
        }
    }

    @Test
    public void testParams() throws Exception
    {
        underTest.params("key1", "val1", "key2", "val2");
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");
        assertThat(params.get("key2")).isEqualTo("val2");
    }

    @Test
    public void testParam() throws Exception
    {
        underTest.param("key1", "val1");
        underTest.param("key2", "val2");
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");
        assertThat(params.get("key2")).isEqualTo("val2");
    }

    @Test
    public void testParamAreNotReset() throws Exception
    {
        underTest.param("key1", "val1");
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");

        underTest.param("key2", "val2");
        underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");
        assertThat(params.get("key2")).isEqualTo("val2");
    }
}
