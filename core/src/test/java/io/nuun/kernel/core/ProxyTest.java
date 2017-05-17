package io.nuun.kernel.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ProxyTest
{
    @Retention(RetentionPolicy.RUNTIME)
    static @interface StuffAnnotation
    {
        String message();

        long value();
    }

    static class Handler implements InvocationHandler
    {
        private final Map behavior;

        Handler(Map behavior)
        {
            this.behavior = behavior;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
        {
            return behavior.get(method.getName());
        }
    }

    @Test
    @StuffAnnotation(value = 10L, message = "Goodbye, World")
    public void testProxy() throws Exception
    {
        final Map map = new HashMap(1);
        map.put("message", "Hello World!");
        map.put("value", 10L);

        final StuffAnnotation min = (StuffAnnotation) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {
            StuffAnnotation.class
        }, new Handler(map));
        final StuffAnnotation staticMin = getClass().getMethod("testProxy").getAnnotation(StuffAnnotation.class);
        System.out.println(min.message()); // Hello World!
        System.out.println(staticMin.message());
        Assert.assertEquals(min.value(), staticMin.value()); // 10L
    }
}