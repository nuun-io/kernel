/**
 * This file is part of Nuun IO Kernel Specs.
 *
 * Nuun IO Kernel Specs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Specs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Specs.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.api.inmemory;

import java.util.regex.Pattern;

/**
 * 
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public abstract class ClasspathAbstractElement<E>
{
	
	protected final  E element;
	private final String name;
	private final String relativePath;
    
	public ClasspathAbstractElement(E element)
	{
		this.element = element;
		this.name = computeName(this.element);
		this.relativePath = computeRelativePath(element);
		assertRelativePath(this.relativePath);
	}
	
    protected void assertRelativePath (String path)
    {
    	
    	
    	if ( ! Pattern.matches(Resource.PATTERN, path))
    	{
    		throw new IllegalArgumentException("\"" + path +"\" must be a valid ressource name : " + Resource.PATTERN );
    	}
    }
	
	protected abstract String computeName(E element);
	
	protected abstract String computeRelativePath(E element);

	public String name ()
	{
		return name;
	}
	
	public String relativePath()
	{
		return relativePath;
	}
	
	public E internalRepresentation ()
	{
		return this.element;
	}

}


