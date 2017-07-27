package io.nuun.kernel.core.internal.topology;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class BindingInfosTest
{

    BindingInfos underTest;

    @Before
    public void init()
    {
        underTest = new BindingInfos();
    }

    @Test
    public void testPut()
    {
        // given
        Key<String> key = Key.get(String.class);

        assertThat(underTest.contains(key, BindingInfo.NULLABLE)).isFalse();

        underTest.put(key, BindingInfo.NULLABLE);
        assertThat(underTest.get(key)).hasSize(1);
        assertThat(underTest.contains(key, BindingInfo.NULLABLE)).isTrue();

        underTest.put(key, BindingInfo.NULLABLE);
        assertThat(underTest.get(key)).hasSize(1);

    }

    @Test
    public void contains()
    {
        Key<String> key = Key.get(String.class);
        underTest.put(key, BindingInfo.NULLABLE);
        underTest.put(key, BindingInfo.IS_BOUND);

        assertThat(underTest.contains(Key.get(String.class), BindingInfo.NULLABLE)).isTrue();
        assertThat(underTest.contains(Key.get(String.class), BindingInfo.IS_BOUND)).isTrue();

        assertThat(underTest.keys(BindingInfo.NULLABLE)).hasSize(1);

        Key<Long> key2 = Key.get(Long.class);
        underTest.put(key2, BindingInfo.NULLABLE);

        assertThat(underTest.keys(BindingInfo.NULLABLE)).hasSize(2);

    }

    @Test
    public void key1()
    {
        Key<String> key = Key.get(String.class);
        underTest.put(key, BindingInfo.NULLABLE);

        assertThat(underTest.get(key)).hasSize(1);

        Key<String> key2 = Key.get(TypeLiteral.get(String.class));
        underTest.put(key2, BindingInfo.NULLABLE);
        assertThat(underTest.get(key2)).hasSize(1);
        assertThat(underTest.get(key)).hasSize(1);

        assertThat(key).isEqualTo(key2);
    }

}
