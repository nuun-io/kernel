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
package io.nuun.kernel.core.test_topo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.name.Named;

import io.nuun.kernel.api.annotations.Topology;
import io.nuun.kernel.core.test_topo.sample.MyMethodInterceptor;
import io.nuun.kernel.core.test_topo.sample.MyObject;
import io.nuun.kernel.core.test_topo.sample.MyService;
import io.nuun.kernel.core.test_topo.sample.MyService2;
import io.nuun.kernel.core.test_topo.sample.MyService2Impl;
import io.nuun.kernel.core.test_topo.sample.MyService2Provider;
import io.nuun.kernel.core.test_topo.sample.MyService2ProviderBis;
import io.nuun.kernel.core.test_topo.sample.MyService4;
import io.nuun.kernel.core.test_topo.sample.MyService4Int;
import io.nuun.kernel.core.test_topo.sample.MyServiceImpl;
import io.nuun.kernel.core.test_topo.sample.Server;
import io.nuun.kernel.core.test_topo.sample.Serveur;
import io.nuun.kernel.spi.topology.Multi;

@Topology(configurationFiles = {
        "classpath:topology.properties", "./src/test/resources/topology2.properties"

})
public interface MyTopology
{

    List<String>               generic        = new ArrayList<>();

    Map<String, List<Integer>> mapComplicated = new HashMap<String, List<Integer>>();

    // Constants
    @Server
    Long                       port           = 8080l;

    @Serveur
    String                     url            = "http://localhost.local";

    @Named("main")
    String                     context        = "cli";

    // will be overriden by MyTopologyOverride.theAnswer
    Integer                    theAnswer      = 41;

    // Simple injection
    MyServiceImpl injects(MyService key);

    // Simple injection
    MyService2Impl injectsTwo(@Named("two") MyService key);

    // Generic injection
    MyService4Int injectsInt(MyService4<Integer> key);

    // auto injection
    MyObject injects(MyObject key);

    // jsr330 providers
    MyService2Provider provides(MyService2 key);

    // jsr330 providers duplicates
    MyService2ProviderBis provides_bis(@Server MyService2 key);

    // AOP via AOP Alliance
    MyService3Sample injects(MyService3 key);

    MyMethodInterceptor intercepts(ClassePredicate pc, MethodPredicate pm);
    
    @Multi
    List<MyCommand1> command1List = null;
    
    @Multi
    Set<MyCommand2> command2Set = null;
    
    @Multi(MyCommand2Key.class)
    Map<String, MyCommand2> command2Maps = null;

    //
    // MyLoggerFieldInjector manages(MyLogger annotation);

    //
    // // Qualified injections
    // HisServiceImpl1 injectsOne (@One HisService key);
    // HisServiceImpl2 injectsTwo (@Two HisService key);
    //
    // @Singleton // jsr330
    // HisServiceImpl3 injectsThree (@Three HisService key);
    // /*
    // * 1) f(C) -> I1
    // * 2) f(I1) -> C1
    // */
    //
    // //
    // void chains ( ServiceImpl impl , ServiceInterface key);
    //
    // static class ServiceInterface implements Function<Class<?>, Class<?>> {
    //
    // @Override
    // public Class<?> apply(Class<?> t)
    // {
    // if (t.isInterface() && t.getSimpleName().endsWith("Service")) {
    // return t;
    // }
    //
    // return null;
    // }
    //
    // }
    //
    // static class InterfaceStuffPredicate implements Predicate<Class<?>> {
    //
    // @Override
    // public boolean test(Class<?> t)
    // {
    // return t.isInterface() && t.getSimpleName().endsWith("Stuff");
    // }
    //
    // }
    //
    // static class PlopPredicate implements Predicate<Class<?>> {
    //
    // @Override
    // public boolean test(Class<?> t)
    // {
    // return t.getSimpleName().endsWith("Plop");
    // }
    //
    // }
    //

}
