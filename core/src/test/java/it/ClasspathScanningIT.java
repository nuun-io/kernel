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
package it;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.api.config.KernelOptions;
import io.nuun.kernel.core.NuunCore;
import it.fixture.scan.AdditionalClasspathPlugin;
import it.fixture.scan.ClassToScan1;
import it.fixture.scan.ClassToScan2;
import it.fixture.scan.ScanningPlugin;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class tests the features associated with the scan done by Reflections.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class ClasspathScanningIT
{

    /**
     * Tests if the plugin ScanningPlugin is able to scan classes using predicates.
     * All the classes annotated or meta annotation with {@code Ignore} should be ignored.
     */
    @Test
    public void test_class_scan_and_ignored_policy()
    {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .option(KernelOptions.SCAN_PLUGIN, false)
                .option(KernelOptions.ROOT_PACKAGES, Lists.newArrayList("it.fixture.scan"))
                .addPlugin(ScanningPlugin.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        assertThat(kernel.scannedURLs()).isNotNull();
        assertThat(kernel.scannedURLs()).isEmpty();
        kernel.init();

        assertThat(kernel.plugins()).hasSize(1);
        assertThat(kernel.plugins().get(ScanningPlugin.NAME)).isInstanceOf(ScanningPlugin.class);
        ScanningPlugin scanningPlugin = (ScanningPlugin) kernel.plugins().get(ScanningPlugin.NAME);
        assertThat(scanningPlugin.getScannedClasses()).hasSize(2);
        assertThat(scanningPlugin.getScannedClasses()).containsOnly(ClassToScan1.class, ClassToScan2.class);

        assertThat(kernel.scannedURLs()).isNotEmpty();
    }

    /**
     * Tests if the plugin AdditionalClasspathPlugin is able to provide additional URLs to be scanned.
     */
    @Test
    public void test_additional_classpath() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .option(KernelOptions.SCAN_PLUGIN, false)
                .option(KernelOptions.ROOT_PACKAGES, Lists.newArrayList("it.fixture.scan"))
                .addPlugin(AdditionalClasspathPlugin.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        assertThat(kernel.scannedURLs()).isNotNull();
        assertThat(kernel.scannedURLs()).isEmpty();
        kernel.init();

        assertThat(kernel.plugins()).hasSize(1);
        assertThat(kernel.plugins().get(AdditionalClasspathPlugin.NAME)).isInstanceOf(AdditionalClasspathPlugin.class);
        assertThat(kernel.scannedURLs()).contains(AdditionalClasspathPlugin.URL);
    }
}
