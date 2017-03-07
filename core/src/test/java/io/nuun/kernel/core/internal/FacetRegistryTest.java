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

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.annotations.Facet;
import io.nuun.kernel.core.AbstractPlugin;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FacetRegistryTest
{
    @Test
    public void get_null_is_accepted()
    {
        FacetRegistry registry = new FacetRegistry(new ArrayList<>());
        Assertions.assertThat((Object)registry.getFacet(null)).isNull();
    }

    @Test
    public void scan_a_plugin_facet()
    {
        List<Plugin> plugins = new ArrayList<>();
        OneFacetPlugin oneFacetPlugin = new OneFacetPlugin();
        plugins.add(oneFacetPlugin);

        Facet1 facet1 = new FacetRegistry(plugins).getFacet(Facet1.class);

        Assertions.assertThat(facet1).isNotNull();
        Assertions.assertThat(facet1).isEqualTo(oneFacetPlugin);
    }

    @Test
    public void scan_multiple_facet_in_a_plugin()
    {
        List<Plugin> plugins = new ArrayList<>();
        TwoFacetPlugin twoFacetPlugin = new TwoFacetPlugin();
        plugins.add(twoFacetPlugin);

        FacetRegistry registry = new FacetRegistry(plugins);
        Facet1 facet1 = registry.getFacet(Facet1.class);
        Facet2 facet2 = registry.getFacet(Facet2.class);

        Assertions.assertThat(facet1).isNotNull();
        Assertions.assertThat(facet1).isEqualTo(twoFacetPlugin);
        Assertions.assertThat(facet2).isNotNull();
        Assertions.assertThat(facet2).isEqualTo(twoFacetPlugin);
    }

    @Test(expected = IllegalStateException.class)
    public void getFacet_fails_for_multiple_facet_implementation()
    {
        List<Plugin> plugins = new ArrayList<>();
        OneFacetPlugin oneFacetPlugin = new OneFacetPlugin();
        TwoFacetPlugin twoFacetPlugin = new TwoFacetPlugin();
        plugins.add(oneFacetPlugin);
        plugins.add(twoFacetPlugin);

        FacetRegistry registry = new FacetRegistry(plugins);
        registry.getFacet(Facet1.class);
    }

    @Test
    public void getFacets_never_return_null()
    {
        List<Plugin> plugins = new ArrayList<>();
        OneFacetPlugin oneFacetPlugin = new OneFacetPlugin();
        plugins.add(oneFacetPlugin);

        FacetRegistry registry = new FacetRegistry(plugins);
        List<Facet2> facet2s = registry.getFacets(Facet2.class);

        Assertions.assertThat(facet2s).isNotNull();
        Assertions.assertThat(facet2s).isEmpty();
    }

    @Test
    public void get_multiple_facet_implementations()
    {
        List<Plugin> plugins = new ArrayList<>();
        OneFacetPlugin oneFacetPlugin = new OneFacetPlugin();
        TwoFacetPlugin twoFacetPlugin = new TwoFacetPlugin();
        plugins.add(oneFacetPlugin);
        plugins.add(twoFacetPlugin);

        FacetRegistry registry = new FacetRegistry(plugins);
        List<Facet1> facet1s = registry.getFacets(Facet1.class);

        Assertions.assertThat(facet1s).hasSize(2);

        Assertions.assertThat(facet1s.get(0)).isEqualTo(oneFacetPlugin);
        Assertions.assertThat(facet1s.get(0)).isInstanceOf(OneFacetPlugin.class);

        Assertions.assertThat(facet1s.get(1)).isEqualTo(twoFacetPlugin);
        Assertions.assertThat(facet1s.get(1)).isInstanceOf(TwoFacetPlugin.class);


        List<Facet2> facet2s = registry.getFacets(Facet2.class);
        Assertions.assertThat(facet2s).hasSize(1);
    }

    @Test
    public void get_plugin_implementation()
    {
        List<Plugin> plugins = new ArrayList<>();
        LegacyPlugin legacyPlugin = new LegacyPlugin();
        plugins.add(legacyPlugin);

        FacetRegistry registry = new FacetRegistry(plugins);
        List<LegacyPlugin> legacyPlugins = registry.getFacets(LegacyPlugin.class);

        Assertions.assertThat(legacyPlugins).hasSize(1);

        Assertions.assertThat(legacyPlugins.get(0)).isEqualTo(legacyPlugin);
        Assertions.assertThat(legacyPlugins.get(0)).isInstanceOf(LegacyPlugin.class);
    }

    @Test
    public void get_missing_plugin_implementations()
    {
        List<Plugin> plugins = new ArrayList<>();

        FacetRegistry registry = new FacetRegistry(plugins);
        List<LegacyPlugin> legacyPlugins = registry.getFacets(LegacyPlugin.class);

        Assertions.assertThat(legacyPlugins).hasSize(0);
    }

    @Facet
    private interface Facet1
    {
    }

    @Facet
    private interface Facet2
    {
    }

    private static class OneFacetPlugin extends AbstractPlugin implements Facet1
    {
        @Override
        public String name()
        {
            return "plugin-with-trait";
        }
    }

    private static class TwoFacetPlugin extends AbstractPlugin implements Facet1, Facet2
    {
        @Override
        public String name()
        {
            return "plugin-with-trait2";
        }
    }

    private static class LegacyPlugin extends AbstractPlugin
    {
        @Override
        public String name()
        {
            return "legacy-plugin";
        }
    }
}
