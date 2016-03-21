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

import io.nuun.kernel.api.inmemory.Resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class InMemoryFactory {
	
	private static final String INMEMORY = "inmemory";
	private InMemoryHandler handler;

	public InMemoryFactory() {
		handler = new InMemoryHandler();
	}

	public URL createInMemoryClass(Class<?> claSs) throws MalformedURLException
	{
		return url( claSs.getName().replace('.', '/') + ".class");
	}
	
	public URL createInMemoryResource(String resource) throws MalformedURLException
	{
		String content = resource.replace('\\', '/');
		return url(content);
	}
	
	private URL url(String content) throws MalformedURLException
	{
		assertElementName(content);
		return new URL(null, INMEMORY + "://localhost/" + content, handler);
	}

    protected void assertElementName(String name)
    {
    	if ( ! Pattern.matches(Resource.PATTERN, name))
    	{
    		throw new IllegalArgumentException("\"" + name +"\" must be a valid ressource name : " + Resource.PATTERN);
    	}
    }
}