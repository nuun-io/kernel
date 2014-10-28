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
	protected List<ClasspathAbstractElement<?>> entries = new ArrayList<ClasspathAbstractElement<?>>();

	
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
