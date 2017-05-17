package io.nuun.kernel.core.internal.topology;

import io.nuun.kernel.api.annotations.Topology;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class TopologyPredicateTest
{

    TopologyPredicate underTest;

    @Before
    public void init()
    {
        underTest = new TopologyPredicate();
    }

    @Topology
    static class Nominal
    {
    }

    static class None
    {
    }

    @Topology
    @Retention(RetentionPolicy.RUNTIME)
    @Target({
        ElementType.TYPE
    })
    static @interface MyTopo1
    {}

    @MyTopo1
    @Retention(RetentionPolicy.RUNTIME)
    @Target({
        ElementType.TYPE
    })
    static @interface MyTopo2
    {}

    @Test
    public void nominal()
    {
        Assertions.assertThat(underTest.test(Nominal.class)).isTrue();
    }

    @Test
    public void none()
    {
        Assertions.assertThat(underTest.test(None.class)).isFalse();
    }

    @Test
    public void level1()
    {
        Assertions.assertThat(underTest.test(MyTopo1.class)).isTrue();
    }

    @Test
    public void level2()
    {
        Assertions.assertThat(underTest.test(MyTopo2.class)).isTrue();
    }

}
