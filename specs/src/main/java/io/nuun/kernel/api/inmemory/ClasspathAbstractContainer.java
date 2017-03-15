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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public abstract class ClasspathAbstractContainer< S extends ClasspathAbstractContainer<S>>
{

	protected S myself;
	protected String name;
	protected List<ClasspathAbstractElement<?>> entries = new ArrayList<>();

	
	public ClasspathAbstractContainer(String name)
	{
		this.name = name;
		this.initSelf();
	}
	
	@SuppressWarnings("unchecked")
	private void initSelf()
	{
		this.myself = (S) this.getClass().cast(this);
	}
	
	
	public String name ()
	{
		return name;
	}
	
	public S add(ClasspathAbstractElement<?> element)
	{
		entries.add(element);
		return myself;
	}
	
	public List<ClasspathAbstractElement<?>> entries ()
	{
		return Collections.unmodifiableList(entries);
	}

}
