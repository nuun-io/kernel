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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public enum SimpleInMemoryClasspath implements InMemoryClasspath {

	INSTANCE;
    
	private final ThreadLocal<Map<String , InMemoryClasspathAbstractContainer<?>>> perThreadListEntries = new ThreadLocal<Map<String , InMemoryClasspathAbstractContainer<?>>>()
	{
		@Override
		protected java.util.Map<String , InMemoryClasspathAbstractContainer<?>> initialValue() {
			return new HashMap<String , InMemoryClasspathAbstractContainer<?>>();
		}
	};

	
	private SimpleInMemoryClasspath()
	{
	}
	
	public SimpleInMemoryClasspath add(InMemoryClasspathAbstractContainer<?> entry)
	{
		perThreadListEntries.get().put(entry.name()  ,entry);
		return this;
	}
	
	public void reset()
	{
		perThreadListEntries.get().clear();
	}
	
	@Override
	public Collection<InMemoryClasspathAbstractContainer<?>> entries() {
		
		return perThreadListEntries.get().values();
	}
	
	@Override
	public InMemoryClasspathAbstractContainer<?> entry(String container)
	{
		return perThreadListEntries.get().get(container);
	}

}
