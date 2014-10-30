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
package io.nuun.kernel.tests.ut.assertor;

import io.nuun.kernel.tests.internal.ElementMap;
import io.nuun.kernel.tests.internal.dsl.builder.AnnotatedBindingBuilderImpl;
import io.nuun.kernel.tests.internal.dsl.builder.LinkedBindingBuilderImpl;
import io.nuun.kernel.tests.internal.dsl.builder.TimedScopedBindingBuilderImpl;
import io.nuun.kernel.tests.internal.dsl.holder.GlobalHolder;
import io.nuun.kernel.tests.internal.dsl.holder.InjecteeHolder;
import io.nuun.kernel.tests.ut.assertor.dsl.AnnotatedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.LinkedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.TimedScopedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.wildcard.Wildcard;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Key;
import com.google.inject.spi.Element;

/**
 * 
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
 *
 */
public abstract class ModuleAssertor
{
    private ElementMap<ElementAssertor<? extends Element>> assertors;
    
    private List<GlobalHolder> globalHolders;
    
    public ModuleAssertor()
    {
        assertors = new ElementMap<ElementAssertor<?>>();
        globalHolders = new ArrayList<GlobalHolder>();
    }
    
    public ElementMap<? extends ElementAssertor<? extends Element>> assertions ()
    {
        computeAssertors();
    	return assertors;
    }
    
    private void computeAssertors()
    {
    	assertors.clear();
    	ElementAssertorFactory factory = new ElementAssertorFactory();
    	
    	for ( GlobalHolder globalHolder : globalHolders)
    	{
    		ElementAssertor<?> elementAssertor = factory.create(globalHolder);
    		assertors.put(elementAssertor., value)
    	}
    }
    /*
	Assertor ::= ( assertBind(Key) | assertBind(TypeLiteral) ) <LinkedBindingBuilder>   |  assertBind (Class) <AnnotatedBindingBuilder>  |
	             ( assertBind(WildcardKey) | assertBind(WildcardTypeLiteral) ) <TimedLinkedBindingBuilder>
	               
    LinkedBindingBuilder(T) ::= toInstance(T) | to(Class(? extends T))  <ScopedBindingBuilder> | 
                                toInstance(WildCardInstance) <TimedBuilder>| to(WildcardClass) <TimedScopedBindingBuilder> 
     */
    
    
    //
    protected <T >LinkedBindingBuilder<T> assertBind(Key<T> key)
    {
        GlobalHolder globalHolder = new GlobalHolder();
        globalHolders.add(globalHolder);
        
        
        globalHolder.as(InjecteeHolder.class). setInjecteeKey(key);
        
        return new LinkedBindingBuilderImpl<T>(globalHolder);
    }
    
    //
    protected <T> AnnotatedBindingBuilder<T> assertBind(Class<T> clss)
    {
        GlobalHolder globalHolder = new GlobalHolder();
        globalHolders.add(globalHolder);

        globalHolder.as(InjecteeHolder.class) .setInjecteeClass(clss);
        
        return new AnnotatedBindingBuilderImpl<T>(globalHolder);
    }
    

    protected TimedScopedBindingBuilder assertBind(Wildcard wildcard)
    {
        GlobalHolder globalHolder = new GlobalHolder();
        globalHolders.add(globalHolder);
        
        InjecteeHolder injecteeHolder = globalHolder;
        injecteeHolder.setInjecteeWildcard(wildcard);
        

        return new TimedScopedBindingBuilderImpl(globalHolder);

    }
    
    public List<GlobalHolder> globalHolders()
    {
        return globalHolders;
    }
    
    
    
    public abstract void configure();

}
