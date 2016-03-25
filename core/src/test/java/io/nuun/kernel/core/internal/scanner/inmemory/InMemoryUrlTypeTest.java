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
package io.nuun.kernel.core.internal.scanner.inmemory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static java.util.regex.Pattern.matches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class InMemoryUrlTypeTest
{
    private InMemoryUrlType underTest;
    private InMemoryFactory factory;

    @Before
    public void init()
    {
        List<? extends InMemoryFile<?>> fs = Lists.newArrayList();

        Map<String, List<? extends InMemoryFile<?>>> m = Maps.newHashMap();

        m.put("zobd", fs);

        underTest = new InMemoryUrlType();
        factory = new InMemoryFactory();
    }

    @Test
    public void testMatches() throws Exception
    {
        URL inMemo1 = factory.createInMemoryResource("toto.txt");

        assertThat(underTest.matches(inMemo1)).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRegex()
    {
        String pattern = "(/[a-zA-Z0-9\\-_\\.]+)+";

        Object[][] data = array(
                array("/zerze/efrez/ferzr/zerrerre", true),
                array("/zerze/efrez/ferzr/zerrerre.txt", true),
                array("zerzerze/zerzer/rez4re/erzre5z.txd", false),
                array("/zerzer-zrzerze/zerzere_zerzer55ze", true),
                array("/zerzer-zrzerze/zerzere_zerze/", false),
                array("/zeorzeiorize", true)
        );
        for (Object[] array : data)
        {
            String toTest = (String) array[0];
            Boolean assertion = (Boolean) array[1];
            assertThat(matches(pattern, toTest)).isEqualTo(assertion)
                    .as("Check that " + toTest + " is " + assertion);
        }
    }

}
