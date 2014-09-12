/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nuun.kernel.core.internal.scanner.inmemory;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class InMemoryFactoryTest {
	
	InMemoryFactory underTest;
	
	@Before
	public void init ()
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
			
		}
		catch (MalformedURLException e)
		{
			fail("No exception should occu");
		}
	}

	@Test
	public void testCreateInMemoryResource()
	{
		try {
			String resource = "/data/stuf/content.properties";
			URL createInMemoryResource = underTest.createInMemoryResource(resource);
			assertThat(createInMemoryResource).isNotNull();
			assertThat(createInMemoryResource.toString()).isEqualTo("inmemory://localhost/" + resource);
			assertThat(createInMemoryResource.getHost()).isEqualTo( "localhost");
			assertThat(createInMemoryResource.getPath()).isEqualTo( "/" + resource);
			

		} catch (MalformedURLException e) {
			fail("No exception should occu");
		}
	}
	
	
	@Test
	public void testCreateInMemoryResource2()
	{
		try {
			String resource = "data\\stuf\\content.properties";
			String expected = "data/stuf/content.properties";
			URL createInMemoryResource = underTest.createInMemoryResource(resource);
			assertThat(createInMemoryResource).isNotNull();
			assertThat(createInMemoryResource.toString()).isEqualTo("inmemory://localhost/" + expected);
			
		} catch (MalformedURLException e) {
			fail("No exception should occu");
		}
	}
	
	
	@Test
	public void testEquality ()
	{
		try
		{
			String resource = "io/nuun/kernel/core/internal/scanner/inmemory/InMemoryFactoryTest.class";
			URL createInMemoryClass = underTest.createInMemoryClass(getClass());
			URL createInMemoryResource = underTest.createInMemoryResource(resource);
			assertThat(createInMemoryResource).isNotNull();
			assertThat(createInMemoryClass).isNotNull();
			assertThat(createInMemoryResource).isEqualTo(createInMemoryClass);
			
		}
		catch (MalformedURLException e)
		{
			fail("No exception should occu");
		}
		
	}
	
	

}
