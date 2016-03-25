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
package io.nuun.kernel.core.internal;

import com.google.common.collect.Lists;
import io.nuun.kernel.core.KernelException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.data.MapEntry.entry;

/**
 * @author Pierre THIROUIN (pierre.thirouin@ext.inetpsa.com)
 */
public class AliasMapTest
{
    @Test
    public void testAccessNormalParam() throws Exception
    {
        AliasMap aliasMap = new AliasMap();
        aliasMap.put("param", "val1");

        Assertions.assertThat(aliasMap.get("param")).isEqualTo("val1");
        Assertions.assertThat(aliasMap.containsKey("param")).isTrue();
    }

    @Test
    public void testAliasAKernelParam() throws Exception
    {
        AliasMap aliasMap = new AliasMap();
        aliasMap.put("alias", "val1");
        aliasMap.putAlias("alias", "param");

        Assertions.assertThat(aliasMap.get("param")).isEqualTo("val1");
        Assertions.assertThat(aliasMap.containsAllKeys(Lists.newArrayList("param", "alias"))).isTrue();
    }

    @Test
    public void testCannotAliasAnExistingParameter() throws Exception
    {
        AliasMap aliasMap = new AliasMap();
        try
        {
            aliasMap.put("param", "val1");
            aliasMap.putAlias("alias", "param");
        } catch (IllegalArgumentException e)
        {
            Assertions.assertThat(e).hasMessage("The key \"param\" to alias is already present in the kernel parameters.");

            Assertions.assertThat(aliasMap.containsKey("param")).isTrue();
        }
    }

    @Test
    public void testContainsKey() throws Exception
    {
        AliasMap aliasMap = new AliasMap();
        Assertions.assertThat(aliasMap.containsKey("foo")).isFalse();
        Assertions.assertThat(aliasMap.containsAllKeys(Lists.newArrayList("foo", "bar"))).isFalse();
    }

    @Test
    public void testComputedMapNotNull() throws Exception
    {
        Assertions.assertThat(new AliasMap().toMap()).isNotNull();
    }

    @Test
    public void testComputedMapContainsParameters() throws Exception
    {
        final AliasMap aliasMap = new AliasMap();
        aliasMap.put("param1", "val1");
        aliasMap.put("param2", "val2");
        Assertions.assertThat(aliasMap.toMap()).containsExactly(entry("param1", "val1"), entry("param2", "val2"));
    }

    @Test
    public void testComputedMapContainsAlias() throws Exception
    {
        final AliasMap aliasMap = new AliasMap();
        aliasMap.put("alias1", "val1");
        aliasMap.put("alias2", "val2");
        aliasMap.putAlias("alias1", "param1");
        aliasMap.putAlias("alias2", "param2");
        Assertions.assertThat(aliasMap.toMap())
                .containsOnly(
                        entry("param1", "val1"),
                        entry("param2", "val2"),
                        entry("alias1", "val1"),
                        entry("alias2", "val2")
                );
    }

    @Test
    public void testMultipleAliasIndirection() throws Exception
    {
        final AliasMap aliasMap = new AliasMap();
        aliasMap.put("alias2", "val1");
        aliasMap.putAlias("alias1", "param1");
        aliasMap.putAlias("alias2", "alias1");
        Assertions.assertThat(aliasMap.toMap())
                .containsOnly(
                        entry("param1", "val1"),
                        entry("alias1", "val1"),
                        entry("alias2", "val1")
                );
    }

    @Test
    public void testInfiniteIndirection() throws Exception
    {
        final AliasMap aliasMap = new AliasMap();
        aliasMap.put("alias1", "val1");
        aliasMap.putAlias("alias1", "param1");
        aliasMap.putAlias("param1", "alias1");
        try
        {
            aliasMap.toMap();
        } catch (KernelException e)
        {
            Assertions.assertThat(e).hasMessage("Cycle detected in kernel parameter aliases.");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComputedMapIsImmutable() throws Exception
    {
        new AliasMap().toMap().put("foo", "bar");
        Assertions.fail("The map should be immutable");
    }
}
