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
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.annotations.Facet;
import io.nuun.kernel.core.AbstractPlugin;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Tests the plugin sort according to their dependencies.
 *
 * @author Pierre Thirouin
 */
public class PluginSortStrategyTest
{

    @Test
    public void test_sort_plugins_with_dependencies()
    {
        Plugin1 plugin1 = new Plugin1(); // declare P2 as dependent
        Plugin2 plugin2 = new Plugin2();
        Plugin3 plugin3 = new Plugin3(); // declare P2 as required and declare P4 as dependent
        Plugin4 plugin4 = new Plugin4();

        List<Plugin> plugins = Lists.<Plugin>newArrayList(plugin4, plugin2, plugin3, plugin1);
        FacetRegistry facetRegistry = new FacetRegistry(plugins);
        PluginSortStrategy strategy = new PluginSortStrategy(facetRegistry, plugins);
        List<Plugin> orderedPlugins = strategy.sortPlugins();

        // Assert the old order
        assertThat(plugins).containsSequence(plugin4, plugin2, plugin3, plugin1);
        // Assert the new order
        assertThat(orderedPlugins).containsSequence(plugin1, plugin2, plugin3, plugin4);
    }

    @Facet
    private static interface Facet1
    {
    }

    private static class Plugin1 extends TestPlugin
    {

        public Plugin1()
        {
            index = 1;
        }

        @Override
        public Collection<Class<?>> dependentPlugins()
        {
            return Lists.<Class<?>>newArrayList(Facet1.class);
        }
    }

    private static class Plugin2 extends TestPlugin implements Facet1
    {

        public Plugin2()
        {
            index = 2;
        }
    }

    private static class Plugin3 extends TestPlugin
    {

        public Plugin3()
        {
            index = 3;
        }

        @Override
        public Collection<Class<?>> dependentPlugins()
        {
            return Lists.<Class<?>>newArrayList(Plugin4.class);
        }

        @Override
        public Collection<Class<?>> requiredPlugins()
        {
            return Lists.<Class<?>>newArrayList(Facet1.class);
        }
    }

    private static class Plugin4 extends TestPlugin
    {

        public Plugin4()
        {
            index = 4;
        }
    }

    private static class TestPlugin extends AbstractPlugin
    {

        protected int index;

        @Override
        public String name()
        {
            return "P" + index;
        }

        @Override
        public String toString()
        {
            return name();
        }
    }
}
