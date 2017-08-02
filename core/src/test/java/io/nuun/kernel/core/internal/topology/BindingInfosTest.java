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
