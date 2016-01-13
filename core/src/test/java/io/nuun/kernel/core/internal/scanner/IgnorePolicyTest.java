package io.nuun.kernel.core.internal.scanner;

import io.nuun.kernel.api.annotations.Ignore;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pierre Thirouin
 *         Date: 08/06/15
 */
public class IgnorePolicyTest {

    private IgnorePredicate ignorePredicate;

    @Before
    public void testIgnore() {
        ignorePredicate = new IgnorePredicate(false);
    }

    @Test
    public void testIgnorePredicate() {
        Assertions.assertThat(ignorePredicate.apply(IgnorePolicyTest.class)).isTrue();
        Assertions.assertThat(ignorePredicate.apply(IgnoredClass1.class)).isFalse();
        Assertions.assertThat(ignorePredicate.apply(IgnoredClass2.class)).isFalse();
    }

    static class NormalClass {}

    @Ignore
    static class IgnoredClass1 {}

    @CustomIgnore
    static class IgnoredClass2 {}

    @Ignore
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    static @interface CustomIgnore {}
}
