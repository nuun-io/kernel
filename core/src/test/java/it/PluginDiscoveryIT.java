package it;

import it.fixture.NoOpPlugin1;
import it.fixture.NoOpPlugin2;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.NuunCore;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * This class tests the SPI allowing to register Plugin using the classLoader.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class PluginDiscoveryIT {

    /**
     * Tests if the plugins defined in META-INF/services were registered.
     */
    @Test
    public void test_plugin_discovery() {
        Kernel kernel = NuunCore.createKernel(NuunCore.newKernelConfiguration());
        kernel.init();
        Assertions.assertThat(kernel.plugins()).hasSize(2);
        Assertions.assertThat(kernel.plugins().get(NoOpPlugin1.NAME)).isInstanceOf(NoOpPlugin1.class);
        Assertions.assertThat(kernel.plugins().get(NoOpPlugin2.NAME)).isInstanceOf(NoOpPlugin2.class);
    }
}
