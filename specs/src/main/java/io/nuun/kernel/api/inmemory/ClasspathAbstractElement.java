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


