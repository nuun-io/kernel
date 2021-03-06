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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.NuunCore;
import io.nuun.kernel.core.internal.scanner.sample.DummyMethod;
import io.nuun.kernel.core.internal.scanner.sample.HolderForBeanWithParentType;
import io.nuun.kernel.core.internal.scanner.sample.HolderForContext;
import io.nuun.kernel.core.internal.scanner.sample.HolderForInterface;
import io.nuun.kernel.core.internal.scanner.sample.HolderForPlugin;
import io.nuun.kernel.core.internal.scanner.sample.HolderForPrefixWithName;
import io.nuun.kernel.core.internal.scanner.sample.ModuleInError;
import io.nuun.kernel.core.internal.scanner.sample.ModuleInterface;
import io.nuun.kernel.core.pluginsit.dummy1.DummyPlugin;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin2;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin3;
import io.nuun.kernel.core.pluginsit.dummy4.DummyPlugin4;
import io.nuun.kernel.core.pluginsit.dummy4.Pojo1;
import io.nuun.kernel.core.pluginsit.dummy4.Pojo2;
import io.nuun.kernel.core.pluginsit.dummy5.DescendantFromClass;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;
import io.nuun.kernel.core.pluginsit.dummy5.ParentClass;
import io.nuun.kernel.core.pluginsit.dummy5.ToFind;
import io.nuun.kernel.core.pluginsit.dummy5.ToFind2;
import java.util.ArrayList;
import java.util.Collection;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Epo Jemba
 */
public class KernelCoreIT
{
    private static Logger logger = LoggerFactory.getLogger(KernelCoreIT.class);

    private static Kernel underTest;
    private static DummyPlugin4 plugin4;

    private static long start;

    private Injector injector;

    @BeforeClass
    public static void init()
    {
        start = System.currentTimeMillis();
        try
        {
            underTest = createKernelWithPlugins(DummyPlugin.class);
            underTest.init();
            fail("Should Get a KernelException for dependency problem");
        } catch (KernelException ke)
        {
            underTest = createKernelWithPlugins(DummyPlugin.class, DummyPlugin2.class);
            try
            {
                underTest.init();
                fail("Should get a KernelException for dependency problem");
            } catch (KernelException ke2)
            {
                underTest = createKernelWithPlugins(DummyPlugin.class, DummyPlugin2.class,
                        DummyPlugin3.class, DummyPlugin4.class, DummyPlugin5.class);
                underTest.init();
                plugin4 = (DummyPlugin4) underTest.plugins().get("dummuyPlugin4");
            }
        }
        underTest.start();
    }

    public static Kernel createKernelWithPlugins(Class<?>... plugins)
    {
        KernelConfiguration configuration = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .param(DummyPlugin.ALIAS_DUMMY_PLUGIN1, "WAZAAAA")
                .param(DummyPlugin.NUUN_ROOT_ALIAS, "internal," + KernelCoreIT.class.getPackage().getName());

        for (Class<?> plugin : plugins)
        {
            //noinspection unchecked
            configuration.addPlugin((Class<? extends Plugin>) plugin);
        }
        return NuunCore.createKernel(configuration);
    }

    static class DummyInterceptor implements MethodInterceptor
    {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable
        {
            return invocation.proceed();
        }
    }

    @Before
    public void before()
    {
        Module aggregationModule = new AbstractModule()
        {
            @Override
            protected void configure()
            {
                bind(HolderForPlugin.class);
                bind(HolderForContext.class);
                bind(HolderForPrefixWithName.class);
                bind(HolderForBeanWithParentType.class);

                bindInterceptor(Matchers.any(), Matchers.annotatedWith(DummyMethod.class), new DummyInterceptor());
            }
        };
        injector = underTest.objectGraph().as(Injector.class).createChildInjector(aggregationModule);
    }

    @Test
    public void plugin_should_be_detected()
    {
        HolderForPlugin holder = injector.getInstance(HolderForPlugin.class);

        assertThat(holder.dummyService).isNotNull();
        assertThat(holder.dummyService.dummy()).isEqualTo("dummyserviceinternal");

        assertThat(holder.bean6).isNotNull();
        assertThat(holder.bean6.sayHi()).isEqualTo("dummyserviceinternal2");

        assertThat(holder.bean9).isNotNull();
        assertThat(holder.bean9.value).isEqualTo("lorem ipsum");
    }

    @Test(expected = CreationException.class)
    public void plugin_should_be_detected_error_case()
    {
        // Bean7 is not bound to the env because of @Ignore
        injector.createChildInjector(new ModuleInError());
    }

    @Test
    public void injector_should_retrieve_context()
    {
        HolderForContext holder = injector.getInstance(HolderForContext.class);

        assertThat(holder.context).isNotNull();
        assertThat(holder.context instanceof ContextInternal).isTrue();

        assertThat(holder.context.applicationObjectGraph()).isNotNull();
    }

    @Test
    public void classpath_scan_by_predicate_should_work()
    {
        assertThat(plugin4.collection).isNotNull();
        assertThat(plugin4.collection).isNotEmpty();
        assertThat(plugin4.collection).hasSize(1);
        assertThat(plugin4.collection).containsOnly(Pojo1.class);
    }

    @Test
    public void binding_by_predicate_should_work()
    {
        assertThat(injector.getInstance(Pojo2.class)).isNotNull();
        // we check for the scope
        assertThat(injector.getInstance(Pojo2.class)).isEqualTo(injector.getInstance(Pojo2.class));

        try
        {
            assertThat(injector.getInstance(Pojo1.class)).isNull();
            fail("Pojo1 should not be injector");
        } catch (ConfigurationException ce)
        {
            String nl = System.getProperty("line.separator");
            assertThat(ce.getMessage()).isEqualTo("Guice configuration errors:" + nl
                    + nl
                    + "1) Explicit bindings are required and io.nuun.kernel.core.pluginsit.dummy4.Pojo1 is not explicitly bound." + nl
                    + "  while locating io.nuun.kernel.core.pluginsit.dummy4.Pojo1" + nl
                    + nl
                    + "1 error"
            );
        }
    }

    @Test
    public void binding_by_meta_annotation_should_work()
    {
        ToFind tofind = injector.getInstance(ToFind.class);
        assertThat(tofind).isNotNull();
        // singleton
        assertThat(tofind).isEqualTo(injector.getInstance(ToFind.class));
    }

    @Test
    public void binding_by_meta_annotation_regex_should_work()
    {
        ToFind2 toFind = injector.getInstance(ToFind2.class);
        assertThat(toFind).isNotNull();
        // singleton
        assertThat(toFind).isEqualTo(injector.getInstance(ToFind2.class));
    }

    @Test
    public void binding_by_ancestor_should_work()
    {
        ParentClass instance = injector.getInstance(ParentClass.class);
        DescendantFromClass instance2 = injector.getInstance(DescendantFromClass.class);
        assertThat(instance).isNotNull();
        assertThat(instance2).isNotNull();
        // scope SINGLETON has been registered
        assertThat(instance).isEqualTo(injector.getInstance(ParentClass.class));
        assertThat(instance2).isEqualTo(injector.getInstance(DescendantFromClass.class));

        try
        {
            assertThat(injector.getInstance(Pojo1.class)).isNull();
            fail("Pojo1 should not be injector");
        } catch (ConfigurationException ce)
        {
            String nl = System.getProperty("line.separator");
            assertThat(ce.getMessage()).isEqualTo("Guice configuration errors:" + nl
                    + nl
                    + "1) Explicit bindings are required and io.nuun.kernel.core.pluginsit.dummy4.Pojo1 is not explicitly bound." + nl
                    + "  while locating io.nuun.kernel.core.pluginsit.dummy4.Pojo1" + nl
                    + nl
                    + "1 error"
            );
        }
    }

    @Test
    public void bean_should_be_bind_by_name()
    {
        HolderForPrefixWithName holder = injector.getInstance(HolderForPrefixWithName.class);
        assertThat(holder.customBean).isNotNull();
        assertThat(holder.customBean.name()).isNotNull();
        assertThat(holder.customBean.name()).isEqualTo("I am John");

    }

    @Test
    public void bean_should_be_bind_by_parent_type()
    {
        HolderForBeanWithParentType holder = injector.getInstance(HolderForBeanWithParentType.class);
        assertThat(holder.beanWithParentType).isNotNull();
        assertThat(holder.beanWithParentType.name()).isNotNull();
        assertThat(holder.beanWithParentType.name()).isEqualTo("I am Jane");

    }

    @Test
    public void interface_can_be_injected_without_instance_if_declared_nullable()
    {
        Injector createChildInjector = injector.createChildInjector(new ModuleInterface());
        HolderForInterface holder = createChildInjector.getInstance(HolderForInterface.class);
        assertThat(holder.customBean).isNull();
    }

    @org.junit.Ignore("fix it or remove it")
    @Test
    public void plugin_sort_algorithm() throws Exception
    {
        ArrayList<Plugin> plugins = new ArrayList<>(), plugins2;

        p1 e1 = new p1();
        plugins.add(e1);
        p2 e2 = new p2();
        plugins.add(e2); // -> 13
        p3 e3 = new p3();
        plugins.add(e3);
        p4 e4 = new p4();
        plugins.add(e4);
        p5 e5 = new p5();
        plugins.add(e5);
        p6 e6 = new p6();
        plugins.add(e6);
        p7 e7 = new p7();
        plugins.add(e7); // -> 11
        p8 e8 = new p8();
        plugins.add(e8);
        p9 e9 = new p9();
        plugins.add(e9);
        p10 e10 = new p10();
        plugins.add(e10);
        p11 e11 = new p11();
        plugins.add(e11);
        p12 e12 = new p12();
        plugins.add(e12); // -> 6
        p13 e13 = new p13();
        plugins.add(e13); // -> 11

        plugins2 = Whitebox.invokeMethod(underTest, "sortPlugins", plugins);

        assertThat(plugins2).isNotNull();
        assertThat(plugins2).containsOnly(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13);
        assertThat(plugins2).containsSequence(e11, e13, e6, e12, e10);

    }


    static abstract class AbstractTestPlugin extends AbstractPlugin
    {
        public Collection<Class<?>> dep(Class<?> klazz)
        {
            return Lists.newArrayList(klazz);
        }
    }

    static class p1 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p2 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }

        @Override
        public Collection<Class<?>> requiredPlugins()
        {
            return dep(p13.class);
        }
    }

    static class p3 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p4 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p5 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p6 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p7 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }

        @Override
        public Collection<Class<?>> requiredPlugins()
        {
            return dep(p11.class);
        }
    }

    static class p8 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p9 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p10 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p11 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }
    }

    static class p12 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }

        @Override
        public Collection<Class<?>> requiredPlugins()
        {
            return dep(p6.class);
        }
    }

    static class p13 extends AbstractTestPlugin
    {
        @Override
        public String name()
        {
            return this.getClass().getName();
        }

        @Override
        public Collection<Class<?>> requiredPlugins()
        {
            return dep(p11.class);
        }
    }

    @Test
    public void AliasMap_should_work()
    {
        AliasMap map = new AliasMap();
        map.putAlias("realkey1", "alias1");
        map.putAlias("realkey2", "alias2");

        Object object1 = new Object();
        Object object2 = new Object();

        map.put("realkey1", object1.toString());
        map.put("realkey2", object2.toString());

        assertThat(map.get("alias1")).isEqualTo(object1.toString());
        assertThat(map.get("alias2")).isEqualTo(object2.toString());

        assertThat(map.containsKey("alias1")).isTrue();
        assertThat(map.containsKey("alias2")).isTrue();
    }

    @AfterClass
    public static void clear()
    {
        underTest.stop();
        long end = System.currentTimeMillis();
        logger.info("Test took " + (end - start) + " ms.");
    }
}
