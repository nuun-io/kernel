package io.nuun.kernel.core.internal.topology;

import static java.util.Arrays.stream;
import io.nuun.kernel.api.annotations.Topology;

import java.util.function.Predicate;

public class TopologyPredicate implements Predicate<Class<?>>
{

    public static final TopologyPredicate INSTANCE = new TopologyPredicate();

    @Override
    public boolean test(Class<?> c)
    {
        return stream(c.getAnnotations()).anyMatch(a -> a.annotationType().equals(Topology.class)) ||
        // recursion
                stream(c.getAnnotations()).map(a -> a.annotationType())

                .filter(cc -> !cc.getName().startsWith("java")).anyMatch(this::test)

        ;
    }

}
