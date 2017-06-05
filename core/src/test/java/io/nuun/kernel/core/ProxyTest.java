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