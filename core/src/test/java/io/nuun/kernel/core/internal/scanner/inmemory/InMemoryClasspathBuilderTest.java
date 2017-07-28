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

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class InMemoryClasspathBuilderTest
{
    @Test
    public void testConfigure()
    {
        InMemoryClasspath.INSTANCE.reset();
        ClasspathBuilder builder = new ClasspathBuilder()
        {

            @Override
            public void configure()
            {
                addDirectory("zerzerze");
                addResource("zerezrez", null);
                addJar("epo.jar");
                addClass(String.class);
            }
        };

        builder.configure();

        Assertions.assertThat(InMemoryClasspath.INSTANCE.entries()).hasSize(2);
    }

}
