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

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class InMemoryFactoryTest
{

    InMemoryFactory underTest;

    @Before
    public void init()
    {
        underTest = new InMemoryFactory();
    }

    @Test
    public void testCreateInMemoryClass()
    {
        try
        {
            URL createInMemoryClass = underTest.createInMemoryClass(getClass());
            assertThat(createInMemoryClass).isNotNull();
            assertThat(createInMemoryClass.toString()).isEqualTo("inmemory://localhost/io/nuun/kernel/core/internal/scanner/inmemory/InMemoryFactoryTest.class");

        } catch (MalformedURLException e)
        {
            fail("No exception should occu");
        }
    }

    @Test
    public void testCreateInMemoryResource()
    {
        try
        {
            String resource = "data/stuf/content.properties";
            URL createInMemoryResource = underTest.createInMemoryResource(resource);
            assertThat(createInMemoryResource).isNotNull();
            assertThat(createInMemoryResource.toString()).isEqualTo("inmemory://localhost/" + resource);
            assertThat(createInMemoryResource.getHost()).isEqualTo("localhost");
            assertThat(createInMemoryResource.getPath()).isEqualTo("/" + resource);


        } catch (MalformedURLException e)
        {
            fail("No exception should occu");
        }
    }


    @Test
    public void testCreateInMemoryResource2()
    {
        try
        {
            String resource = "data\\stuf\\content.properties";
            String expected = "data/stuf/content.properties";
            URL createInMemoryResource = underTest.createInMemoryResource(resource);
            assertThat(createInMemoryResource).isNotNull();
            assertThat(createInMemoryResource.toString()).isEqualTo("inmemory://localhost/" + expected);

        } catch (MalformedURLException e)
        {
            fail("No exception should occu");
        }
    }

    @Test(/*expected=IllegalArgumentException.class*/)
    public void testCreateInMemoryResourceError()
    {
        try
        {
            String resource = "data\\stuf\\content.properties";

            URL createInMemoryResource = underTest.createInMemoryResource(resource);
            createInMemoryResource.toExternalForm();
        } catch (MalformedURLException e)
        {
            fail("No exception should occu");
        }
    }


    @Test
    public void testEquality()
    {
        try
        {
            String resource = "io/nuun/kernel/core/internal/scanner/inmemory/InMemoryFactoryTest.class";
            URL createInMemoryClass = underTest.createInMemoryClass(getClass());
            URL createInMemoryResource = underTest.createInMemoryResource(resource);
            assertThat(createInMemoryResource).isNotNull();
            assertThat(createInMemoryClass).isNotNull();
            assertThat(createInMemoryResource).isEqualTo(createInMemoryClass);

        } catch (MalformedURLException e)
        {
            fail("No exception should occu");
        }

    }


}
