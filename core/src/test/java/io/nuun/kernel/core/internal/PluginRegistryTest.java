package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.core.KernelException;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class PluginRegistryTest
{

    private PluginRegistry underTest = new PluginRegistry();

    @Test
    public void test_get_by_class()
    {
        Plugin plugin = new MyPlugin();
        Plugin plugin2 = new MyPlugin2();

        underTest.add(plugin);
        underTest.add(plugin2);

        Plugin myPlugin = underTest.get(MyPlugin.class);
        assertThat(myPlugin).isNotNull();
        assertThat(myPlugin).isEqualTo(plugin);

        Plugin myPlugin2 = underTest.get(MyPlugin2.class);
        assertThat(myPlugin2).isNotNull();
        assertThat(myPlugin2).isEqualTo(plugin2);
    }

    @Test
    public void test_get_by_name()
    {
        Plugin plugin = new MyPlugin();
        Plugin plugin2 = new MyPlugin2();

        underTest.add(plugin);
        underTest.add(plugin2);

        Plugin myPlugin = underTest.get(MyPlugin.NAME);
        assertThat(myPlugin).isNotNull();
        assertThat(myPlugin).isEqualTo(plugin);

        Plugin myPlugin2 = underTest.get(MyPlugin2.NAME);
        assertThat(myPlugin2).isNotNull();
        assertThat(myPlugin2).isEqualTo(plugin2);
    }

    @Test
    public void test_get_plugins_cant_be_null()
    {
        assertThat(underTest.getPlugins()).isNotNull();
    }

    @Test
    public void test_get_plugins()
    {
        Plugin plugin = new MyPlugin();
        Plugin plugin2 = new MyPlugin2();
        underTest.add(plugin);
        underTest.add(plugin2);

        Collection<Plugin> plugins = underTest.getPlugins();
        assertThat(plugins).containsOnly(plugin, plugin2);
    }

    @Test
    public void test_add_plugin_classes()
    {
        underTest.add(MyPlugin.class);

        Collection<Plugin> plugins = underTest.getPlugins();
        assertThat(plugins).hasSize(1);
    }

    @Test
    public void test_add_non_instantiable_plugin_classes()
    {
        try
        {
            underTest.add(MyPlugin2.class);
        } catch (KernelException e)
        {
            assertThat(e).hasMessage(String.format("Plugin %s can not be instantiated", MyPlugin2.class));
        }
    }

    @Test
    public void test_get_pluginClasses_cant_be_null()
    {
        assertThat(underTest.getPluginClasses()).isNotNull();
    }

    @Test
    public void test_get_plugin_classes()
    {
        Plugin plugin = new MyPlugin();
        Plugin plugin2 = new MyPlugin2();
        underTest.add(plugin);
        underTest.add(plugin2);

        Collection<Class<? extends Plugin>> plugins = underTest.getPluginClasses();
        //noinspection unchecked
        assertThat(plugins).containsOnly(MyPlugin.class, MyPlugin2.class);
    }

    @Test(expected = KernelException.class)
    public void test_assertNameNotBlank_with_null()
    {
        underTest.add(new UnnamedPlugin(null));
    }

    @Test(expected = KernelException.class)
    public void test_assertNameNotBlank_with_empty()
    {
        underTest.add(new UnnamedPlugin(""));
    }

    @Test(expected = KernelException.class)
    public void test_assertUniqueness_with_class_conflict()
    {
        underTest.add(new MyPlugin());
        underTest.add(new MyPlugin());
    }

    @Test(expected = KernelException.class)
    public void test_assertUniqueness_with_name_conflict()
    {
        underTest.add(new MyPlugin());
        underTest.add(new UnnamedPlugin(MyPlugin.NAME));
    }

    public static class MyPlugin extends AbstractPlugin
    {

        public static final String NAME = "my-plugin";

        @Override
        public String name()
        {
            return NAME;
        }
    }

    private static class MyPlugin2 extends AbstractPlugin
    {

        public static final String NAME = "my-plugin2";

        @Override
        public String name()
        {
            return NAME;
        }
    }

    private static class UnnamedPlugin extends AbstractPlugin
    {

        private String name;

        public UnnamedPlugin(String name)
        {
            this.name = name;
        }

        @Override
        public String name()
        {
            return name;
        }
    }
}
