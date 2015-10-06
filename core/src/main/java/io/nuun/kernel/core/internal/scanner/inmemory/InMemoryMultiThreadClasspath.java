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
