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
package io.nuun.kernel.core.internal.utils;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;


public class AssertUtilsTest
{
    @Test
    public void testHasAnnotationDeep()
    {
        assertThat(AssertUtils.hasAnnotationDeep(Class1.class, MetaAnno2.class)).isTrue();
        assertThat(AssertUtils.hasAnnotationDeep(Class1.class, MetaAnno1.class)).isTrue();
        assertThat(AssertUtils.hasAnnotationDeep(Class1.class, Anno1.class)).isTrue();

        assertThat(AssertUtils.hasAnnotationDeep(Class2.class, Anno2.class)).isTrue();
        assertThat(AssertUtils.hasAnnotationDeep(Class2.class, MetaAnno2.class)).isTrue();
    }

    @Test
    public void testAnno2() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        AnnoFromClone annoFromClone = Annoted.class.getAnnotation(AnnoFromClone.class);

        assertThat(annoFromClone.v1()).isEqualTo("clone2");

        Method v1 = annoFromClone.getClass().getMethod("v1");
        Object invoke = v1.invoke(annoFromClone);
        assertThat(invoke).isEqualTo("clone2");
        // The following line break the test when using jacoco
        //assertThat( annoFromClone.getClass().getDeclaredMethods() ).hasSize(7);

        AnnoFrom annoFrom = AssertUtils.annotationProxyOf(AnnoFrom.class, annoFromClone);
        assertThat(annoFrom.v1()).isEqualTo("clone2");

        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone.class)).isTrue();
        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone2.class)).isFalse();
        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone3.class)).isTrue();
        assertThat(AssertUtils.isEquivalent(AnnoFrom.class, AnnoFromClone4.class)).isFalse();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE})
    @interface MetaAnno2
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE})
    @MetaAnno2
    @interface MetaAnno1
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @MetaAnno1
    @interface Anno1
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @MetaAnno2
    @interface Anno2
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface AnnoFrom
    {
        String value();

        String v1();

        long v2();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface AnnoFromClone
    {
        String value();

        String v1();

        long v2();
    }

    @interface AnnoFromClone2
    {
        String value();

        String v1();

        short v2();
    }

    @interface AnnoFromClone3
    {
        String value();

        String v1();

        long v2();

        short v3();

        String v4();
    }

    @interface AnnoFromClone4
    {
        String value();

        long v2();
    }

    @Anno1
    static class Class1
    {
    }

    @Anno2
    static class Class2
    {
    }

    @AnnoFromClone(value = "clone", v1 = "clone2", v2 = 3l)
    static class Annoted
    {
    }
}
