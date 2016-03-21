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
import io.nuun.kernel.core.KernelException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class DependencyProviderTest {

    @Facet
    private static interface Facet1 {}

    @Facet
    private static interface Facet2 {}

    private static class RequiredPlugin1 extends AbstractPlugin implements Facet1 {
        @Override
        public String name() {
            return "required-plugin1";
        }
    }
    private static class RequiredPlugin2 extends AbstractPlugin implements Facet1 {
        @Override
        public String name() {
            return "required-plugin2";
        }
    }

    private static class DependentPlugin extends AbstractPlugin {
        @Override
        public String name() {
            return "dependent-plugin";
        }
    }

    private static class WithDepsPlugin extends AbstractPlugin {
        @Override
        public String name() {
            return "with-deps-plugin";
        }

        @Override
        public Collection<Class<?>> requiredPlugins() {
            return Lists.<Class<?>>newArrayList(Facet1.class, Facet2.class);
        }
        @Override
        public Collection<Class<?>> dependentPlugins() {
            return Lists.<Class<?>>newArrayList(DependentPlugin.class);
        }
    }

    private static class NoDepsPlugin extends AbstractPlugin {
        @Override
        public String name() {
            return "no-deps-plugin";
        }
    }

    private DependencyProvider underTest;
    private WithDepsPlugin withDepsPlugin;
    private RequiredPlugin1 requiredPlugin1;
    private RequiredPlugin2 requiredPlugin2;
    private DependentPlugin dependentPlugin;

    @Before
    public void setup() {
        FacetRegistry facetRegistry = Mockito.mock(FacetRegistry.class);

        List<Facet1> requiredPlugins = new ArrayList<Facet1>();
        requiredPlugin1 = new RequiredPlugin1();
        requiredPlugin2 = new RequiredPlugin2();
        requiredPlugins.add(requiredPlugin1);
        requiredPlugins.add(requiredPlugin2);

        List<DependentPlugin> dependentPlugins = new ArrayList<DependentPlugin>();
        dependentPlugin = new DependentPlugin();
        dependentPlugins.add(dependentPlugin);

        Mockito.when(facetRegistry.getFacets(Facet1.class)).thenReturn(requiredPlugins);
        Mockito.when(facetRegistry.getFacets(DependentPlugin.class)).thenReturn(dependentPlugins);

        PluginRegistry pluginRegistry = Mockito.mock(PluginRegistry.class);
        withDepsPlugin = new WithDepsPlugin();
        Mockito.when(pluginRegistry.get(WithDepsPlugin.class)).thenReturn(withDepsPlugin);
        Mockito.when(pluginRegistry.get(NoDepsPlugin.class)).thenReturn(new NoDepsPlugin());

        underTest = new DependencyProvider(pluginRegistry, facetRegistry);

    }

    @Test
    public void test_provide_dependencies_never_null() {
        List<Plugin> dependencies = underTest.getRequiredPluginsOf(NoDepsPlugin.class);
        Assertions.assertThat(dependencies).isNotNull();
    }

    @Test
    public void test_provide_required_dependencies() {
        List<Plugin> dependencies = underTest.getRequiredPluginsOf(WithDepsPlugin.class);

        Assertions.assertThat(dependencies).hasSize(2);
        Assertions.assertThat(dependencies).containsOnly(requiredPlugin1, requiredPlugin2);
    }

    @Test
    public void test_provide_missing_dependent_dependencies() {
        List<Plugin> dependencies = underTest.getDependentPluginsOf(NoDepsPlugin.class);
        Assertions.assertThat(dependencies).isNotNull();
    }

    @Test
    public void test_provide_dependent_dependencies() {
        List<Plugin> dependencies = underTest.getDependentPluginsOf(WithDepsPlugin.class);

        Assertions.assertThat(dependencies).hasSize(1);
        Assertions.assertThat(dependencies).containsOnly(dependentPlugin);
    }

    @Test
    public void test_provide_all_dependencies() {
        List<Plugin> dependencies = underTest.getDependenciesOf(WithDepsPlugin.class);

        Assertions.assertThat(dependencies).hasSize(3);
        Assertions.assertThat(dependencies).containsOnly(requiredPlugin1, requiredPlugin2, dependentPlugin);
    }

    @Test
    public void test_provide_facet_dependencies_never_return_null() {
        List<Facet2> dependencies = underTest.getFacets(WithDepsPlugin.class, Facet2.class);
        Assertions.assertThat(dependencies).isNotNull();
    }

    @Test(expected = KernelException.class)
    public void test_provide_facet_not_required_dependency() {
        underTest.getFacets(NoDepsPlugin.class, Facet1.class);
    }

    @Test
    public void test_provide_facet_dependencies() {
        List<Facet1> dependencies = underTest.getFacets(WithDepsPlugin.class, Facet1.class);

        Assertions.assertThat(dependencies).hasSize(2);
        Assertions.assertThat(dependencies).containsOnly(requiredPlugin1, requiredPlugin2);
    }
}
