package it;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.api.config.KernelOptions;
import io.nuun.kernel.core.NuunCore;
import it.fixture.scan.ClassToScan1;
import it.fixture.scan.ClassToScan2;
import it.fixture.scan.ScanningPlugin;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * This class tests the features associated with the scan done by Reflections.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class ClasspathScanningIT {

    /**
     * Tests if the plugin ScanningPlugin is able to scan classes using specification.
     * All the classes annotated or meta annotation with {@code Ignore} should be ignored.
     */
    @Test
    public void test_class_scan_and_ignored_policy() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .option(KernelOptions.SCAN_PLUGIN, false)
                .option(KernelOptions.ROOT_PACKAGES, Lists.newArrayList("it.fixture.scan"))
                .addPlugin(ScanningPlugin.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();

        Assertions.assertThat(kernel.plugins()).hasSize(1);
        Assertions.assertThat(kernel.plugins().get(ScanningPlugin.NAME)).isInstanceOf(ScanningPlugin.class);
        ScanningPlugin scanningPlugin = (ScanningPlugin) kernel.plugins().get(ScanningPlugin.NAME);
        Assertions.assertThat(scanningPlugin.getScannedClasses()).hasSize(2);
        Assertions.assertThat(scanningPlugin.getScannedClasses()).containsOnly(ClassToScan1.class, ClassToScan2.class);
    }
}
