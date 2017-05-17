package io.nuun.kernel.core.internal.topology;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

class AnnotationProxy implements InvocationHandler {
    private static final String VALUE = "value";
    private static final String HASH_CODE = "hashCode";
    private final Map<String, Object> behavior;

    AnnotationProxy(Map<String, Object> behavior) {
        this.behavior = behavior;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (method.getName().equals(HASH_CODE)) {
            return behavior.containsKey(VALUE) ?
            // This is specified in java.lang.Annotation.
                    (127 * VALUE.hashCode()) ^ behavior.get(VALUE).hashCode()
                    :
                    // we hack a unique value
                    behavior.put(VALUE, new Object().hashCode());
        }

        return behavior.get(method.getName());
    }
}
