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
package io.nuun.kernel.core.internal.scanner;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import io.nuun.kernel.core.internal.scanner.inmemory.InMemoryUrlType;
import org.reflections.vfs.Vfs;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public abstract class AbstractClasspathScanner implements ClasspathScanner
{
	static
	{
	  Vfs.setDefaultURLTypes(Arrays.asList(
			  new InMemoryUrlType(),
			  Vfs.DefaultUrlTypes.jarFile,
			  Vfs.DefaultUrlTypes.jarUrl,
			  Vfs.DefaultUrlTypes.directory,
			  Vfs.DefaultUrlTypes.jarInputStream
	  ));
	}

	private final boolean reachAbstractClass;

	public AbstractClasspathScanner(boolean reachAbstractClass) {
		this.reachAbstractClass = reachAbstractClass;
	}

	protected Collection<Class<?>> postTreatment(@Nullable Collection<Class<?>> set)
	{
		if (set == null) {
            return Lists.newArrayList();
        }
		return Collections2.filter(set, new IgnorePredicate(reachAbstractClass));
	}
}
