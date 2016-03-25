package it;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.internal.Fixture;
import it.fixture.NoOpPlugin1;
import it.fixture.NoOpPlugin2;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        Kernel kernel = Fixture.initKernel(Fixture.configWithScan());

        assertThat(kernel.plugins()).hasSize(2);
        assertThat(kernel.plugins().get(NoOpPlugin1.NAME)).isInstanceOf(NoOpPlugin1.class);
        assertThat(kernel.plugins().get(NoOpPlugin2.NAME)).isInstanceOf(NoOpPlugin2.class);
    }
}
