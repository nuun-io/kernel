package io.nuun.kernel.core.internal.topology;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

class AnnotationProxy implements InvocationHandler
{
    private final Map behavior;

    AnnotationProxy(Map behavior)
    {
        this.behavior = behavior;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
    {
        if (method.getName().equals("hashCode"))
        {
            return behavior.containsKey("value") ?
            // This is specified in java.lang.Annotation.
                    (127 * "value".hashCode()) ^ behavior.get("value").hashCode()
                    :
                    // we hack a unique value
                    behavior.put("value", new Object().hashCode());
        }

        return behavior.get(method.getName());
    }
}