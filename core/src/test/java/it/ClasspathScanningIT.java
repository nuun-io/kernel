package it;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
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
@SuppressWarnings("unchecked")
public class ClasspathScanningIT {

    /**
     * Tests if the plugin ScanningPlugin is able to scan classes using specification.
     * All the classes annotated or meta annotation with {@code Ignore} should be ignored.
     */
    @Test
    public void test_scanned_classes_with_ignored_classes() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .param(Kernel.NUUN_ROOT_PACKAGE, "it.fixture.scan")
                .plugins(ScanningPlugin.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();

        Assertions.assertThat(kernel.plugins()).hasSize(1);
        Assertions.assertThat(kernel.plugins().get(ScanningPlugin.NAME)).isInstanceOf(ScanningPlugin.class);
        ScanningPlugin scanningPlugin = (ScanningPlugin) kernel.plugins().get(ScanningPlugin.NAME);
        Assertions.assertThat(scanningPlugin.getScannedClasses()).hasSize(2);
        Assertions.assertThat(scanningPlugin.getScannedClasses()).containsOnly(ClassToScan1.class, ClassToScan2.class);
    }
}
