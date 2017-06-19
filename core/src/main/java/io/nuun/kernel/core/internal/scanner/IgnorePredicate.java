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

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import com.google.common.base.Predicate;

import io.nuun.kernel.api.annotations.Ignore;

class IgnorePredicate implements Predicate<Class<?>> {

	private final boolean keepAbstractClasses;

	public IgnorePredicate(boolean keepAbstractClasses) {
		this.keepAbstractClasses = keepAbstractClasses;
	}

	@Override
	public boolean apply(Class<?> clazz) {
		boolean keepClass = true;

		if (!keepAbstractClasses && isAbstractClass(clazz)) {
			keepClass = false;
		}

		for (Annotation annotation : clazz.getAnnotations()) {
			if (annotation.annotationType().equals(Ignore.class) || annotation.annotationType().getAnnotation(Ignore.class) != null) {
				keepClass = false;
				break;
			}
		}
		return keepClass;
	}

	private boolean isAbstractClass(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface();
	}
}
