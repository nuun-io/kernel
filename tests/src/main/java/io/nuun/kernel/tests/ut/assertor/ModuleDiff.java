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
import io.nuun.kernel.tests.internal.visitor.MapElementVisitor;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.google.inject.util.Types;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 * @author pierre.thirouin@gmail.com
 *
 */
public class ModuleDiff
{
    private final Module actual;
    private final ElementMap<? extends ElementAssertor<? extends Element>> assertions;
  

    public ModuleDiff ( Module actual , ModuleAssertor moduleAssertor)
    {
        this.actual = actual;
        assertions = moduleAssertor.assertions();
    }
    
    static class ElementPredicate<EP extends ElementAssertor<C>  , C extends Element > implements Predicate<C>
    {

        private EP elementAssertor;
 
        public ElementPredicate(EP elementAssertor)
        {
            this.elementAssertor = elementAssertor;
        }
        
        @Override
        public boolean apply(C input)
        {
            return elementAssertor.asserts(input);
        }
        
        @SuppressWarnings("unchecked")
        public static  <A extends ElementAssertor<B>,B extends Element> ElementPredicate<A,B> create (ElementAssertor<?>  elementAssertor )
        {
            // elementClass is the resolved type variable class from  ElementAssertor<X> an element.
            // candidateClass is ElementAssertor children class only reachable at runtime. We use Types from google guice to dynamically resolve the full type.
            Class<?> elementClass = (Class<?>)((ParameterizedType)elementAssertor.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Class<?> candidateClass =   (Class<?>) TypeLiteral.get(  Types.newParameterizedType(ElementPredicate.class, elementAssertor.getClass() , elementClass )).getType();
            // we can then instantiate it.
            try
            {
                Constructor<?> constructor = candidateClass.getConstructor(elementAssertor.getClass());
                return (ElementPredicate<A, B>) constructor.newInstance(elementAssertor);
            }
            catch (Exception e)
            {
                throw new Error(e);
            }
        }
    }
    
    /**
     * 
     * 
     * @return
     */
    public ElementMap<ElementDelta> diff ()
    {
        ElementMap<ElementDelta> delta = new ElementMap<ElementDelta>();
        ElementMap<Element> actualStore = visit(actual);
        
        for ( Class<? extends Element> keyClass: assertions.keys())
        {
            Collection<? extends Element> actualInstances = actualStore.get(keyClass);
            
            Collection<? extends ElementAssertor<? extends Element>> elementAssertors = assertions.get(keyClass);
            
            if (actualInstances != null  && elementAssertors != null)
            {
                for (final ElementAssertor<? extends Element> elementAssertor : elementAssertors)
                {
                    int count =  Collections2.filter((Collection<? extends Element>)actualInstances, ElementPredicate.create(elementAssertor) ).size();
                    
                    if (count != elementAssertor.expectedTimes() )
                    {
                        delta.put(keyClass, new ElementDelta(elementAssertor,count));
                    }
                }
            }
        }
        return delta;
    }
    
    private ElementMap<Element> visit(Module candidate)
    {
       ElementMap<Element> elementMap = null;
       MapElementVisitor visitor = new MapElementVisitor();
       for (Element element : Elements.getElements(Stage.DEVELOPMENT, candidate))
       {
           element.acceptVisitor(visitor);
       }
       elementMap = visitor.getStore();
       return elementMap;
    }
    
    
    
}
