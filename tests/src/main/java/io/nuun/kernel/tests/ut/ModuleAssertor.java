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
package io.nuun.kernel.tests.ut;

import io.nuun.kernel.tests.internal.ElementMap;
import io.nuun.kernel.tests.internal.dsl.builder.LinkedBindingBuilderImpl;
import io.nuun.kernel.tests.internal.dsl.builder.TimedLinkedBindingBuilderImpl;
import io.nuun.kernel.tests.internal.dsl.holder.GlobalHolder;
import io.nuun.kernel.tests.internal.dsl.holder.InjecteeHolder;
import io.nuun.kernel.tests.ut.dsl.assertor.LinkedBindingBuilder;
import io.nuun.kernel.tests.ut.dsl.assertor.ScopedBindingBuilder;
import io.nuun.kernel.tests.ut.dsl.assertor.TimedLinkedBindingBuilder;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Key;

/**
 * 
 * 
 * @author epo.jemba@kametic.com
 *
 */
public abstract class ModuleAssertor
{
    private ElementMap<ElementAssertor> assertors;
    
    private List<GlobalHolder> globalHolders;
    
    public ModuleAssertor()
    {
        assertors = new ElementMap<ElementAssertor>();
        globalHolders = new ArrayList<GlobalHolder>();
    }
    
    public ElementMap<ElementAssertor> assertions ()
    {
        return assertors;
    }
    
    //
    protected <T >LinkedBindingBuilder<T> assertBind(Key<T> key)
    {
        GlobalHolder globalHolder = new GlobalHolder();
        globalHolders.add(globalHolder);
        
        InjecteeHolder injecteeHolder = globalHolder;
        injecteeHolder.setInjecteeKey(key);
        
        return new LinkedBindingBuilderImpl<T>(globalHolder);
    }
    

    protected <T > TimedLinkedBindingBuilder<T,ScopedBindingBuilder> assertBind(Wildcard wildcard)
    {
        GlobalHolder globalHolder = new GlobalHolder();
        globalHolders.add(globalHolder);
        
        InjecteeHolder injecteeHolder = globalHolder;
        injecteeHolder.setInjecteeWildcard(wildcard);
        

        return new TimedLinkedBindingBuilderImpl<T>(globalHolder);

    }
    
    protected abstract void configure();

}
