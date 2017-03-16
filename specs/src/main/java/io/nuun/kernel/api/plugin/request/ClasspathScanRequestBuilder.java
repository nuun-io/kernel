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

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Predicate;


/**
 * @author Epo Jemba
 *
 */
public class ClasspathScanRequestBuilder implements Builder<Collection<ClasspathScanRequest>>
{
    
    private Collection<ClasspathScanRequest> requests;
    
    /**
     * 
     */
    public ClasspathScanRequestBuilder()
    {
        requests = new HashSet<>();
    }
    
    
    public ClasspathScanRequestBuilder predicate(Predicate<Class<?>> classPredicate)
    {
        
        requests.add(new ClasspathScanRequest(classPredicate));
        
        return this;
    }

    public ClasspathScanRequestBuilder annotationType(Class<? extends Annotation> annotationTypeRequested)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.ANNOTATION_TYPE, annotationTypeRequested));
        
        return this;
    }

    public ClasspathScanRequestBuilder annotationRegex(String annotationRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.ANNOTATION_REGEX_MATCH, annotationRegex));
        
        return this;
    }

    public ClasspathScanRequestBuilder subtypeOf(Class<?> parentTypeRequested)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.SUBTYPE_OF_BY_CLASS, parentTypeRequested));
        
        return this;
    }
    

    public ClasspathScanRequestBuilder subtypeOfRegex(String parentTypeRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.SUBTYPE_OF_BY_REGEX_MATCH, parentTypeRegex));
        
        return this;
    }
    
    public ClasspathScanRequestBuilder typeOfRegex(String typeRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.TYPE_OF_BY_REGEX_MATCH, typeRegex));
        
        return this;
    }

    public ClasspathScanRequestBuilder resourcesRegex(String resourcesRegex)
    {
        
        requests.add(new ClasspathScanRequest(RequestType.RESOURCES_REGEX_MATCH, resourcesRegex));
        
        return this;
    }

    
    @Override
    public Collection<ClasspathScanRequest> build()
    {
        return Collections.unmodifiableCollection(requests);
    }
    
    @Override
    public void reset()
    {
        requests.clear();
    }
    
    
}
