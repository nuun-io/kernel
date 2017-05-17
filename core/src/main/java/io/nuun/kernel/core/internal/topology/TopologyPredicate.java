package io.nuun.kernel.core.internal.topology;

import static java.util.Arrays.stream;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import io.nuun.kernel.api.annotations.Topology;

public class TopologyPredicate implements Predicate<Class<?>> {

    public static final TopologyPredicate INSTANCE = new TopologyPredicate();

    @Override
    public boolean test(Class<?> c) {
        return stream(c.getAnnotations()).anyMatch(a -> a.annotationType().equals(Topology.class)) ||
        // recursion
                stream(c.getAnnotations()).map(Annotation::annotationType)
                        // removing annotations from jdk
                        .filter(c1 -> !c1.getName().startsWith("java"))
                        // do recursion
                        .anyMatch(this::test)

        ;
    }

}
