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

/**
 * 
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class ClasspathClass extends ClasspathAbstractElement<Class<?>>
{

	public ClasspathClass(Class<?> element) {
		super(element);
	}

	@Override
	protected String computeName(Class<?> element) {
		return element.getSimpleName() + ".class";
	}
	
	@Override
	protected String computeRelativePath(Class<?> element) {
		return element.getName().replace('.', '/') + ".class";
	}
	
	public Class<?> getType ()
	{
		return element;
	}

}
