package io.nuun.kernel.core.test_topo;

import java.util.function.Predicate;

public class ClassePredicate implements Predicate<Class<?>>
{

    @Override
    public boolean test(Class<?> arg0)
    {
        return arg0.getSimpleName().endsWith("Sample");
    }

}
