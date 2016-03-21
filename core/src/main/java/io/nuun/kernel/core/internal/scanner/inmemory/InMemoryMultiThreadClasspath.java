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

import io.nuun.kernel.api.inmemory.Classpath;
import io.nuun.kernel.api.inmemory.ClasspathAbstractContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public enum InMemoryMultiThreadClasspath implements Classpath {

	INSTANCE;
    
	private final ThreadLocal<Map<String , ClasspathAbstractContainer<?>>> perThreadListEntries = new ThreadLocal<Map<String , ClasspathAbstractContainer<?>>>()
	{
		@Override
		protected java.util.Map<String , ClasspathAbstractContainer<?>> initialValue() {
			return new HashMap<String , ClasspathAbstractContainer<?>>();
		}
	};

	
	private InMemoryMultiThreadClasspath()
	{
	}
	
	public InMemoryMultiThreadClasspath add(ClasspathAbstractContainer<?> entry)
	{
		perThreadListEntries.get().put(entry.name()  ,entry);
		return this;
	}
	
	public void reset()
	{
		perThreadListEntries.get().clear();
	}
	
	@Override
	public Collection<ClasspathAbstractContainer<?>> entries() {
		
		return perThreadListEntries.get().values();
	}
	
	@Override
	public ClasspathAbstractContainer<?> entry(String container)
	{
	    return perThreadListEntries.get().get(container);
	}

}
