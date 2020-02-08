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
package io.nuun.kernel.api.plugin.request;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

import io.nuun.kernel.api.di.UnitModule;

public class InterPluginRequestBuilder implements Builder<Collection<InterPluginRequest>>
{
    
    Collection<InterPluginRequest> requests;
    
    
    public InterPluginRequestBuilder()
    {
        requests = new HashSet<>();
    }
    
    
    public InterPluginRequestBuilder predicate(Predicate<UnitModule> predicate)
    {
        requests.add(new InterPluginRequest(predicate));
        return this;
    }
    
    @Override
    public Collection<InterPluginRequest> build()
    {
        return requests;
    }

    @Override
    public void reset()
    {
        requests.clear();
    }

}
