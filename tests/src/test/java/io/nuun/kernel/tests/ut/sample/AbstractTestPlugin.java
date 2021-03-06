/**
 * This file is part of Nuun IO Kernel Tests.
 *
 * Nuun IO Kernel Tests is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Tests is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Tests.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.tests.ut.sample;

import io.nuun.kernel.core.AbstractPlugin;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public abstract class AbstractTestPlugin extends AbstractPlugin
{

    @Override
    public String name()
    {
        return this.getClass().getSimpleName().toLowerCase();
    }
    
    @Override
    public Object nativeUnitModule()
    {
        Object object = null;
        try
        {
            object = underTestModuleClass ().newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return object;
    }
    
    protected abstract Class<?> underTestModuleClass ();
    
    

}
