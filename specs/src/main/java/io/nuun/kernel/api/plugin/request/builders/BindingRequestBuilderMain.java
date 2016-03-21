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
package io.nuun.kernel.api.plugin.request.builders;

import java.lang.annotation.Annotation;

import org.kametic.specifications.Specification;

public  interface BindingRequestBuilderMain 
{
    public BindingRequestBuilderOptionsBuildMain specification(Specification<Class<?>> specification);
    public BindingRequestBuilderOptionsBuildMain annotationType(Class<? extends Annotation> annotationTypeRequested);
    public BindingRequestBuilderOptionsBuildMain annotationRegex(String annotationRegex);
    public BindingRequestBuilderOptionsBuildMain subtypeOf(Class<?> parentTypeRequested);
    public BindingRequestBuilderOptionsBuildMain descendentTypeOf(Class<?> ancestorTypeRequested);
    public BindingRequestBuilderOptionsBuildMain subtypeOfRegex(String parentTypeRegex);
    public BindingRequestBuilderOptionsBuildMain metaAnnotationType(Class<? extends Annotation> metaAnnotationTypeRequested);
    public BindingRequestBuilderOptionsBuildMain metaAnnotationRegex(String metaAnnotationRegex);

    
}