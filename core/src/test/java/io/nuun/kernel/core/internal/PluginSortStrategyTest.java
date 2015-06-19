package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.pluginsit.dummy6.DummyPlugin6_B;
import io.nuun.kernel.core.pluginsit.dummy6.DummyPlugin6_C;
import io.nuun.kernel.core.pluginsit.dummy6.DummyPlugin6_D;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Pierre Thirouin
 */
public class PluginSortStrategyTest {

    private PluginSortStrategy strategy = new PluginSortStrategy();

    @Test
    public void testSort() {
        List<Plugin> plugins = new ArrayList<Plugin>();

        DummyPlugin6_C c = new DummyPlugin6_C();
        plugins.add(c);
        DummyPlugin6_D d = new DummyPlugin6_D();
        plugins.add(d);
        DummyPlugin6_B b = new DummyPlugin6_B();  // <--- C ,  D
        plugins.add(b);

        List<Plugin> orderedPlugins = strategy.sortPlugins(plugins);

        assertThat(orderedPlugins).isNotNull();
        assertThat(orderedPlugins).containsOnly(b, c, d);
        assertThat(orderedPlugins).containsSequence(b, d, c );
    }
}
