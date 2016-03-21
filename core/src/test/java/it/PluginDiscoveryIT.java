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
