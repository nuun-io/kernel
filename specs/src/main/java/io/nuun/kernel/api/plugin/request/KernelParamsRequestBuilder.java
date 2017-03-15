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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


/**
 * @author Epo Jemba
 *
 */
public class KernelParamsRequestBuilder implements Builder<Collection<KernelParamsRequest>>
{
    
    private Collection<KernelParamsRequest> requests;
    
    /**
     * 
     */
    public KernelParamsRequestBuilder()
    {
        requests = new HashSet<>();
    }
    
    
    public KernelParamsRequestBuilder optional(String keyRequested)
    {
        
        requests.add(new KernelParamsRequest(KernelParamsRequestType.OPTIONAL, keyRequested));
        
        return this;
    }

    public KernelParamsRequestBuilder mandatory(String keyRequested)
    {
        
        requests.add(new KernelParamsRequest(KernelParamsRequestType.MANDATORY, keyRequested));
        
        return this;
    }
    
    
    
    @Override
    public Collection<KernelParamsRequest> build()
    {
        return Collections.unmodifiableCollection(requests);
    }
    
    @Override
    public void reset()
    {
        requests.clear();
    }
    
    
}
