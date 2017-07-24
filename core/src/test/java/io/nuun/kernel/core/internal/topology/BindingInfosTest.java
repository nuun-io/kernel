package io.nuun.kernel.core.internal.topology;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Key;

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

}
