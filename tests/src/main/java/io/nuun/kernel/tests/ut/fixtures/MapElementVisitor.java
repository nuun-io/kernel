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

import com.google.inject.Binding;
import com.google.inject.spi.DisableCircularProxiesOption;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.InjectionRequest;
import com.google.inject.spi.InterceptorBinding;
import com.google.inject.spi.MembersInjectorLookup;
import com.google.inject.spi.Message;
import com.google.inject.spi.PrivateElements;
import com.google.inject.spi.ProviderLookup;
import com.google.inject.spi.ProvisionListenerBinding;
import com.google.inject.spi.RequireAtInjectOnConstructorsOption;
import com.google.inject.spi.RequireExplicitBindingsOption;
import com.google.inject.spi.ScopeBinding;
import com.google.inject.spi.StaticInjectionRequest;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.spi.TypeListenerBinding;


/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class MapElementVisitor implements ElementVisitor<Void>
{

    private final Store store;
    
    private final MapBindingTargetVisitor mapBindingTargetVisitor;
//    private final MapBindingScopingVisitor mapBindingScopingVisitor;
    
    public MapElementVisitor ()
    {
        store = new Store();
        mapBindingTargetVisitor = new MapBindingTargetVisitor(store);
//        mapBindingScopingVisitor = new MapBindingScopingVisitor(store);
    }
    
    
    @Override
    public <T> Void visit(Binding<T> binding)
    {
        log("Bindings found " + binding.getKey().getTypeLiteral().getRawType());
        binding.acceptTargetVisitor(mapBindingTargetVisitor);
//        binding.acceptScopingVisitor(mapBindingScopingVisitor);
        
        return null;
    }

    @Override
    public Void visit(InterceptorBinding binding)
    {
        store.put(InterceptorBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ScopeBinding binding)
    {
        store.put(ScopeBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(TypeConverterBinding binding)
    {
        store.put(TypeConverterBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(InjectionRequest<?> request)
    {
        store.put(InjectionRequest.class, request);
        log(request);
        return null;
    }

    @Override
    public Void visit(StaticInjectionRequest request)
    {
        store.put(StaticInjectionRequest.class, request);
        log(request);
        return null;
    }

    @Override
    public <T> Void visit(ProviderLookup<T> lookup)
    {
        store.put(ProviderLookup.class, lookup);
        log(lookup);
        return null;
    }

    @Override
    public <T> Void visit(MembersInjectorLookup<T> lookup)
    {
        store.put(ProviderLookup.class, lookup);
        log(lookup);
        return null;
    }

    @Override
    public Void visit(Message message)
    {
        store.put(Message.class, message);
        log(message);
        return null;
    }

    @Override
    public Void visit(PrivateElements elements)
    {
        store.put(PrivateElements.class, elements);
        log(elements);
        return null;
    }

    @Override
    public Void visit(TypeListenerBinding binding)
    {
        store.put(TypeListenerBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ProvisionListenerBinding binding)
    {
        store.put(ProvisionListenerBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(RequireExplicitBindingsOption option)
    {
        store.put(RequireExplicitBindingsOption.class, option);
        log(option);
        return null;
    }

    @Override
    public Void visit(DisableCircularProxiesOption option)
    {
        store.put(DisableCircularProxiesOption.class, option);
        log(option);
        return null;
    }

    @Override
    public Void visit(RequireAtInjectOnConstructorsOption option)
    {
        store.put(RequireAtInjectOnConstructorsOption.class, option);
        log(option);
        return null;
    }
    
    public Store getStore()
    {
        return store;
    }


    private void log(Object element)
    {

        System.out.println("------------" );
        System.out.println("e : " + element );
    }
   

}
