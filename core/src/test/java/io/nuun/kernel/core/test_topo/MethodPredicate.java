package io.nuun.kernel.core.test_topo;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public class MethodPredicate implements Predicate<Method>
{

    @Override
    public boolean test(Method t)
    {
        return t.getName().endsWith("_aop");
    }

}
