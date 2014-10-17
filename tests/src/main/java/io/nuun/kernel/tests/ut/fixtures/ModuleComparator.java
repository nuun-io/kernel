/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.tests.ut.fixtures;

import io.nuun.kernel.tests.ut.fixtures.delta.ModuleDelta;

import java.util.Collection;

import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class ModuleComparator
{
    private Module actual;
    private Module expected;
    
  

    public ModuleComparator ( Module actual , Module expected)
    {
        this.actual = actual;
        this.expected = expected;
    }
    
    public ModuleDelta diff ()
    {
        Store actualStore = visit(actual);
        Store expectedStore = visit(expected);
        
        boolean contains = true;
        boolean containsExactly = true;
        
        for ( Class<? extends Element> keyClass: expectedStore.keys())
        {
            Collection<Object> actualInstances = actualStore.get(keyClass);
            Collection<Object> expectedInstances = expectedStore.get(keyClass);
            
            if (actualInstances != null && expectedInstances != null && actualInstances.containsAll(actualInstances) )
            {
                //
                
                if (actualInstances.size() != expectedInstances.size())
                {
                    containsExactly = false;
                }
            }
            else
            {
                //
                contains = false;
            }
            
        }
        return null;
    }
    
    
    
    private Store visit(Module candidate)
    {
       Store store = null;
       MapElementVisitor visitor = new MapElementVisitor();
       for (Element element : Elements.getElements(Stage.DEVELOPMENT, candidate))
       {
           element.acceptVisitor(visitor);
       }
       store = visitor.getStore();
       return store;
    }
    
    private void compare ()
    {
        
    }
    
    
}
