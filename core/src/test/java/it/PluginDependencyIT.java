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

import io.nuun.kernel.core.KernelException;
import it.fixture.dependencies.*;
import org.junit.Test;

import static io.nuun.kernel.core.internal.Fixture.config;
import static io.nuun.kernel.core.internal.Fixture.initKernel;

/**
 * Tests dependencies between plugins.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class PluginDependencyIT {

    /**
     * WithDependentDepsPlugin throws an exception if DependentPlugin1 is initialized before it.
     */
    @Test
    public void test_dependent_plugin() {
        initKernel(config()
                .addPlugin(WithDependentDepsPlugin.class)
                .addPlugin(DependentPlugin1.class));
    }

    /**
     * The kernel should throws an exception if the dependent plugin is not present.
     */
    @Test(expected = KernelException.class)
    public void test_dependent_plugin_with_missing_dependency() {
        initKernel(config().addPlugin(WithDependentDepsPlugin.class));
    }

    /**
     * WithRequiredDepsPlugin throws an exception if RequiredPlugin1 is initialized after it.
     */
    @Test
    public void test_required_plugin() {
        initKernel(config()
                .addPlugin(WithRequiredDepsPlugin.class)
                .addPlugin(RequiredPlugin1.class));
    }

    /**
     * The kernel should throws an exception if the required plugin is not present.
     */
    @Test(expected = KernelException.class)
    public void test_required_plugin_with_missing_dependency() {
        initKernel(config().addPlugin(WithRequiredDepsPlugin.class));
    }

    /**
     * WithDependentDepsPlugin throws an exception if DependentPlugin1 is initialized before it.
     */
    @Test
    public void test_required_facet() {
        initKernel(config()
                .addPlugin(Facet1Plugin.class)
                .addPlugin(WithRequiredFacetPlugin.class));
    }
}
