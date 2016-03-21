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
