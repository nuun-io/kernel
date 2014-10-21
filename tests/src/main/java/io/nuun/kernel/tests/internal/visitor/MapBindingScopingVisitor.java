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
package io.nuun.kernel.tests.internal.visitor;

import java.lang.annotation.Annotation;

import com.google.common.collect.Multimap;
import com.google.inject.Binder;
import com.google.inject.Scope;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class MapBindingScopingVisitor implements BindingScopingVisitor<Void>
{

    public static class ScopingElement implements Element
    {

        @Override
        public Object getSource()
        {
            return null;
        }

        @Override
        public <T> T acceptVisitor(ElementVisitor<T> visitor)
        {
            return null;
        }

        @Override
        public void applyTo(Binder binder)
        {
        }
    }
    
    public static class EagerSingleton  extends ScopingElement
    {
    }
    
    public static class NoScoping  extends ScopingElement
    {
    }
    
    public static class ScopeElement  extends ScopingElement
    {
        public final Scope scope;

        public ScopeElement(Scope scope)
        {
            this.scope = scope;
            
        }
    }
    
    public static class ScopeAnnotation  extends ScopingElement
    {
        public final Class<? extends Annotation> scopeAnnotation;

        public ScopeAnnotation(Class<? extends Annotation> scopeAnnotation)
        {
            this.scopeAnnotation = scopeAnnotation;
        }
    }
    
    private Multimap<Class<? extends Element>, Object> store;

    MapBindingScopingVisitor(Multimap<Class<? extends Element>, Object> store)
    {
        this.store = store;
    }

    @Override
    public Void visitEagerSingleton()
    {
        store.put(EagerSingleton.class, null);
        log("Eager Singleton ");
        return null;
    }

    @Override
    public Void visitScope(Scope scope)
    {
        log("Scope " + scope);
        return null;
    }

    @Override
    public Void visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation)
    {
        log("Scope Annotation " + scopeAnnotation);
        return null;
    }

    @Override
    public Void visitNoScoping()
    {
        log("No Scoping");
        return null;
    }

    private void log(Object element)
    {
        System.out.println(""+element);
    }
    
    
}
