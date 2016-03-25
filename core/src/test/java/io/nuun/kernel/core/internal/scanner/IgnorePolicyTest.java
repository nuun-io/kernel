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
package io.nuun.kernel.core.internal.scanner;

import io.nuun.kernel.api.annotations.Ignore;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class IgnorePolicyTest
{
    private IgnorePredicate ignorePredicate;

    @Before
    public void testIgnore()
    {
        ignorePredicate = new IgnorePredicate(false);
    }

    @Test
    public void testIgnorePredicate()
    {
        Assertions.assertThat(ignorePredicate.apply(IgnorePolicyTest.class)).isTrue();
        Assertions.assertThat(ignorePredicate.apply(IgnoredClass1.class)).isFalse();
        Assertions.assertThat(ignorePredicate.apply(IgnoredClass2.class)).isFalse();
    }

    @Ignore
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    static @interface CustomIgnore
    {
    }

    static class NormalClass
    {
    }

    @Ignore
    static class IgnoredClass1
    {
    }

    @CustomIgnore
    static class IgnoredClass2
    {
    }
}
