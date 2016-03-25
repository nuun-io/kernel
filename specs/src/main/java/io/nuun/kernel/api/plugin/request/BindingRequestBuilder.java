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
/**
 * 
 */
package io.nuun.kernel.api.plugin.request;

import io.nuun.kernel.api.plugin.request.builders.BindingRequestBuilderOptionsBuildMain;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.kametic.specifications.Specification;

/**
 * @author Epo Jemba
 */
public class BindingRequestBuilder implements BindingRequestBuilderOptionsBuildMain
{
    private Collection<BindingRequest> requests;
    private BindingRequest currentBindingRequest = null;
    
    public BindingRequestBuilder()
    {
        requests = new HashSet<BindingRequest>();
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain specification(Specification<Class<?>> specification)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.VIA_SPECIFICATION, null,specification));
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain annotationType(Class<? extends Annotation> annotationTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested));
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain metaAnnotationType(Class<? extends Annotation> metaAnnotationTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.META_ANNOTATION_TYPE, metaAnnotationTypeRequested));
        return this;
    }
 
    @Override
    public BindingRequestBuilderOptionsBuildMain annotationRegex(String annotationRegex)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex));
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain metaAnnotationRegex(String metaAnnotationRegex)  {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.META_ANNOTATION_REGEX_MATCH, metaAnnotationRegex));
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain subtypeOf(Class<?> parentTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested));
        
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain descendentTypeOf(Class<?> ancestorTypeRequested)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.SUBTYPE_OF_BY_TYPE_DEEP, ancestorTypeRequested));
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain subtypeOfRegex(String parentTypeRegex)
    {
        requests.add(currentBindingRequest = new BindingRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex));
        return this;
    }
    
    @Override
    public BindingRequestBuilderOptionsBuildMain withConstraint(Object constraint)
    {
        currentBindingRequest.requestedConstraint = constraint;
        return this;
    }

    @Override
    public BindingRequestBuilderOptionsBuildMain withScope(Object scope)
    {
        currentBindingRequest.requestedScope = scope;
        return this;
    }
    
    @Override
    public Collection<BindingRequest> build()
    {
        return Collections.unmodifiableCollection(requests);
    }
    
    @Override
    public void reset()
    {
        requests.clear();
    }
}
