package io.nuun.kernel.core.test_topo;

import io.nuun.kernel.api.annotations.Topology;
import io.nuun.kernel.core.test_topo.sample.MyLogger;
import io.nuun.kernel.core.test_topo.sample.MyLoggerFieldInjector;
import io.nuun.kernel.core.test_topo.sample.MyMethodInterceptor;
import io.nuun.kernel.core.test_topo.sample.MyService;
import io.nuun.kernel.core.test_topo.sample.MyService2;
import io.nuun.kernel.core.test_topo.sample.MyService2Provider;
import io.nuun.kernel.core.test_topo.sample.MyServiceImpl;
import io.nuun.kernel.core.test_topo.sample.MyServiceImpl2;
import io.nuun.kernel.core.test_topo.sample.Server;
import io.nuun.kernel.core.test_topo.sample.Serveur;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import com.google.inject.name.Named;

@Topology
public interface MyTopology
{
    // Constants
    @Server
    Long    port      = 8080l;

    @Serveur
    String  url       = "http://localhost.local";

    Integer theAnswer = 42;

    // Simple injection
    MyServiceImpl injects(MyService key);

    // Simple injection
    MyServiceImpl2 injectsTwo(@Named("two") MyService key);

    // auto injection
    // MyObject injects(MyObject key);

    // jsr330 providers
    MyService2Provider provides(MyService2 key);

    // AOP via AOP Alliance
    MyMethodInterceptor intercepts(Predicate<Class<?>> pc, Predicate<Method> pm);

    //
    MyLoggerFieldInjector manages(MyLogger annotation);

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
