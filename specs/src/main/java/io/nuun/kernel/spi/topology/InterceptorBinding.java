package io.nuun.kernel.spi.topology;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public class InterceptorBinding extends Binding
{
    public final Class<? extends Predicate<Class<?>>> classPredicate;
    public final Class<? extends Predicate<Method>>   methodPredicate;
    public final Class<?>                             methodInterceptor;

    public InterceptorBinding(
            Class<? extends Predicate<Class<?>>> classPredicate,
            Class<? extends Predicate<Method>> methodPredicate,
            Class<?> methodInterceptor)
    {
        this.classPredicate = classPredicate;
        this.methodPredicate = methodPredicate;
        this.methodInterceptor = methodInterceptor;

    }
}
