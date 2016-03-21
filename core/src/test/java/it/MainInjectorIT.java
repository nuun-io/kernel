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

import com.google.inject.Injector;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.internal.Fixture;
import it.fixture.injection.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the main injector built by the kernel.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class MainInjectorIT {

    /**
     * Tests that the interface bound by the two plugins are really bound.
     */
    @Test
    public void test_main_injector() {
        Kernel kernel = Fixture.startKernel(Fixture.config()
                .addPlugin(InjectionPlugin1.class)
                .addPlugin(InjectionPlugin2.class));

        Injector injector = kernel.objectGraph().as(Injector.class);

        InjectableInterface1 injectableInterface1 = injector.getInstance(InjectableInterface1.class);
        assertThat(injectableInterface1).isNotNull();
        assertThat(injectableInterface1).isInstanceOf(InjecteeImplementation1.class);

        InjectableInterface2 injectableInterface2 = injector.getInstance(InjectableInterface2.class);
        assertThat(injectableInterface2).isNotNull();
        assertThat(injectableInterface2).isInstanceOf(InjecteeImplementation2.class);
    }
}
