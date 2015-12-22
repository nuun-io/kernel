/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
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

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;

import org.kametic.specifications.Specification;

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
}
