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
package io.nuun.kernel.core;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.internal.topology.TopologyModule.PredicateMatcherAdapter;
import io.nuun.kernel.core.internal.topology.TopologyPlugin;
import io.nuun.kernel.core.test_topo.ClassePredicate;
import io.nuun.kernel.core.test_topo.MethodPredicate;
import io.nuun.kernel.core.test_topo.MyService3;
import io.nuun.kernel.core.test_topo.MyService3Sample;
import io.nuun.kernel.core.test_topo.sample.MyMethodInterceptor;
import io.nuun.kernel.core.test_topo.sample.MyObject;
import io.nuun.kernel.core.test_topo.sample.MyService;
import io.nuun.kernel.core.test_topo.sample.MyService2;
import io.nuun.kernel.core.test_topo.sample.MyService2Impl;
import io.nuun.kernel.core.test_topo.sample.MyService4;
import io.nuun.kernel.core.test_topo.sample.MyService4Int;
import io.nuun.kernel.core.test_topo.sample.MyServiceImpl2Bis;
import io.nuun.kernel.core.test_topo.sample.MyServiceImplOver;
import io.nuun.kernel.core.test_topo.sample.Server;
import io.nuun.kernel.core.test_topo.sample.Serveur;
import io.nuun.kernel.spi.configuration.NuunProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;

public class KernelSuite9Test
{

    private Kernel   underTest;

    private Injector injector;

    @Before
    public void initkernel()
    {
        underTest = createKernel(

        newKernelConfiguration().rootPackages("io.nuun.kernel.core.test_topo") //
                .withoutSpiPluginsLoader().plugins(new TopologyPlugin()));

        underTest.init();
        underTest.start();
        injector = underTest.objectGraph().as(Injector.class);
    }

    @Test
    public void topology_should_handle_properties_binding()
    {
        HolderProp h = new HolderProp();

        injector.injectMembers(h);

        assertThat(h.key1).isEqualTo("value1");
        assertThat(h.key2).isEqualTo("value2");
        assertThat(h.key3).isEqualTo("value3");
        assertThat(h.key4).isEqualTo("value4");
        // assertThat(h.key1ViaAnno).isEqualTo("value1");
    }

    static class HolderProp
    {
        @Inject
        @Named("key1")
        String       key1;

        @Inject
        @Named("key2")
        String       key2;

        @Inject
        @Named("key3")
        String       key3;

        @Inject
        @Named("key4")
        String       key4;

        @NuunProperty("key1")
        String       key1ViaAnno;

        @Inject
        List<String> listOfString;

    }

    static class HolderGeneric
    {
        @Inject
        List<String> listOfString;

        @Inject
        @Nullable
        IsNull       isNull;

    }

    static class HolderGeneric2
    {
        @Inject
        Map<String, Integer> mapOfString;

        @Inject
        Set<Long>            setOfLong;

    }

    static interface Definition
    {
        Map<String, Integer> mapOfString(Set<Long> input);
    }

    static interface IsNull
    {
    }

    @Test
    public void checkStuff() throws Exception
    {

        {
            Field f = HolderGeneric.class.getDeclaredField("listOfString");
            assertThat(f).isNotNull();

            // http://www.programcreek.com/java-api-examples/index.php?api=java.lang.reflect.ParameterizedType
            Type genericType = f.getGenericType();

            TypeLiteral typeLiteral = TypeLiteral.get(f.getGenericType());

            Injector createInjector = Guice.createInjector(new AbstractModule()
            {

                @Override
                protected void configure()
                {
                    bind(typeLiteral).to(ArrayList.class);
                    bind(IsNull.class).toProvider(Providers.of(null));

                }
            });

            HolderGeneric instance = new HolderGeneric();
            createInjector.injectMembers(instance);

            assertThat(instance.listOfString).isNotNull();
            assertThat(instance.isNull).isNull();
        }
        {
            Method declaredMethod = Definition.class.getMethod("mapOfString", Set.class);

            assertThat(declaredMethod).isNotNull();

            Type returnType = declaredMethod.getGenericReturnType();
            TypeLiteral rt = TypeLiteral.get(returnType);

            assertThat(returnType).isNotNull();

            TypeLiteral pt = null;

            for (Parameter parameter : declaredMethod.getParameters())
            {
                Type parameterizedType = parameter.getParameterizedType();
                assertThat(parameterizedType).isNotNull();
                pt = TypeLiteral.get(parameterizedType);

                // /

                break;
            }

            final TypeLiteral pt1 = pt;

            Injector createInjector = Guice.createInjector(new AbstractModule()
            {

                @Override
                protected void configure()
                {

                    Map<String, Integer> map = new HashMap<String, Integer>();
                    map.put("un", 1);
                    map.put("deux", 2);

                    Set<Long> set = new HashSet<>();
                    set.add(1l);
                    set.add(2l);
                    set.add(3l);

                    bind(rt).toInstance(map);
                    bind(pt1).toInstance(set);

                }
            });

            HolderGeneric2 instance = new HolderGeneric2();
            createInjector.injectMembers(instance);

            assertThat(instance.mapOfString).isNotNull();
            assertThat(instance.setOfLong).isNotNull();

            assertThat(instance.mapOfString).containsKeys("un", "deux");
            assertThat(instance.setOfLong).containsExactly(1l, 2l, 3l);

        }

    }

    @Test
    public void topology_should_handle_aop_via_method_interceptor()
    {
        MyService3 ms3 = injector.getInstance(MyService3.class);

        assertThat(ms3.one()).isEqualTo("one");
        assertThat(ms3.one_aop()).isEqualTo("(one)");
    }

    public void matchers_and_interceptors_fixtures_should_work()
    {
        Injector i2 = Guice.createInjector(new AbstractModule()
        {

            @Override
            protected void configure()
            {
                bind(MyService3.class).to(MyService3Sample.class);

                Matcher<? super Class<?>> classMatcher = new PredicateMatcherAdapter<Class<?>>(new ClassePredicate());

                Matcher<Method> methodMatcher = new PredicateMatcherAdapter<Method>(new MethodPredicate());

                bindInterceptor(classMatcher, methodMatcher, new MethodInterceptor[] {
                    new MyMethodInterceptor()
                });

            }
        });

        MyService3 ms3 = i2.getInstance(MyService3.class);

        assertThat(ms3.one()).isEqualTo("one");
        assertThat(ms3.one_aop()).isEqualTo("(one)");

    }

    @Test
    public void topoly_should_work_with_provider_binding()
    {

        Object instance = injector.getInstance(Key.get(MyService2.class));
        assertThat(instance).isNotNull();
        assertThat(instance).isInstanceOf(MyService2Impl.class);

        instance = injector.getInstance(Key.get(MyService2.class, Server.class));
        assertThat(instance).isNotNull();
        assertThat(instance).isInstanceOf(MyServiceImpl2Bis.class);
    }

    @Test
    public void topoly_should_work_with_instance_binding()
    {
        //
        Integer theAnswer = injector.getInstance(Key.get(Integer.class));

        assertThat(theAnswer).isNotNull();
        assertThat(theAnswer).isEqualTo(42);

        //
        Long port = injector.getInstance(Key.get(Long.class, Server.class));
        assertThat(port).isNotNull();
        assertThat(port).isEqualTo(8080l);

        //
        String url = injector.getInstance(Key.get(String.class, Serveur.class));
        assertThat(url).isNotNull();
        assertThat(url).isEqualTo("http://localhost.local");

        //
        String context = injector.getInstance(Key.get(String.class, Names.named("main")));
        assertThat(context).isNotNull();
        assertThat(context).isEqualTo("cli");

        //
        MyObject mo = injector.getInstance(Key.get(MyObject.class));
        assertThat(mo).isNotNull();

        //
        List<String> listOfstring = injector.getInstance(Key.get(new TypeLiteral<List<String>>()
        {
        }));
        assertThat(listOfstring).isNotNull();

        Map<String, List<Integer>> complicatedMap = injector.getInstance(Key.get(new TypeLiteral<Map<String, List<Integer>>>()
        {
        }));
        assertThat(complicatedMap).isNotNull();

    }

    @Test
    public void topoly_should_work_with_linked_binding()
    {
        // MyServiceImpl injects(MyService key);
        Object myService = injector.getInstance(Key.get(MyService.class));

        assertThat(myService).isNotNull();
        assertThat(myService).isInstanceOf(MyServiceImplOver.class);

        // MyService2Impl injectsTwo(@Named("two") MyService key);
        Object myService2 = injector.getInstance(Key.get(MyService.class, Names.named("two")));
        assertThat(myService2).isNotNull();
        assertThat(myService2).isInstanceOf(MyService2Impl.class);

        MyService4Int myService4Int = (MyService4Int) injector.getInstance(Key.get(new TypeLiteral<MyService4<Integer>>()
        {
        }));
        assertThat(myService4Int).isNotNull();
        assertThat(myService4Int).isInstanceOf(MyService4Int.class);

    }

    @Test
    public void topology_should_be_reach_via_meta_inf_scan()
    {
        {
            String marker1 = injector.getInstance(Key.get(String.class, Names.named("topo1")));
            assertThat(marker1).isNotNull();
            assertThat(marker1).isEqualTo("topo1");
        }

        {
            String marker2 = injector.getInstance(Key.get(String.class, Names.named("topo2")));
            assertThat(marker2).isNotNull();
            assertThat(marker2).isEqualTo("topo2");
        }

        try
        {
            injector.getInstance(Key.get(String.class, Names.named("topo3")));
            failBecauseExceptionWasNotThrown(ConfigurationException.class);
        }
        catch (Exception e)
        {

        }

    }

    @After
    public void stopKernel()
    {
        underTest.stop();
    }

}
