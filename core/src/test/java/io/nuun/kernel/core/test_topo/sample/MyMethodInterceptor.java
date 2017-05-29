package io.nuun.kernel.core.test_topo.sample;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MyMethodInterceptor implements MethodInterceptor
{
    public static int counter = 0;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {

        Object o = invocation.proceed();
        if (o instanceof String)
        {

            return "(" + o + ")";
        }
        counter++;
        return o;
    }

}
