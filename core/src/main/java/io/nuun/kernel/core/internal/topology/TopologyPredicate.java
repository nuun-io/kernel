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
