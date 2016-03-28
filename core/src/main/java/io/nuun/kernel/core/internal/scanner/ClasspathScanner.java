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

import org.kametic.specifications.Specification;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

public interface ClasspathScanner
{
    Collection<Class<?>> scanTypes(String typeRegex);

    Collection<Class<?>> scanTypes(Specification<Class<?>> specification);

    Collection<Class<?>> scanTypesAnnotatedBy(Class<? extends Annotation> annotationType);

    Collection<Class<?>> scanTypesAnnotatedBy(String annotationTypeRegex);

    Collection<Class<?>> scanTypesMetaAnnotated(Class<? extends Annotation> annotationType);

    Collection<Class<?>> scanTypesMetaAnnotated(String metaAnnotationRegex);

    Collection<Class<?>> scanSubTypesOf(Class<?> subType);

    Collection<Class<?>> scanSubTypesOf(String typeRegex);

    Set<String> scanResources(String pattern);

    Set<URL> getUrls();
}
