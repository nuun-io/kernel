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

import io.nuun.kernel.tests.internal.ElementMap;

import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.Element;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.UntargettedBinding;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class MapBindingTargetVisitor implements BindingTargetVisitor<Object, Void>
{

    private ElementMap<Element> elementMap;

    public MapBindingTargetVisitor(ElementMap<Element> elementMap)
    {
        this.elementMap = elementMap;
    }

    @Override
    public Void visit(InstanceBinding<? extends Object> binding)
    {
        elementMap.put(InstanceBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ProviderInstanceBinding<? extends Object> binding)
    {
        elementMap.put(ProviderInstanceBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ProviderKeyBinding<? extends Object> binding)
    {
        elementMap.put(ProviderKeyBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(LinkedKeyBinding<? extends Object> binding)
    {
        elementMap.put(LinkedKeyBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ExposedBinding<? extends Object> binding)
    {
        elementMap.put(ExposedBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(UntargettedBinding<? extends Object> binding)
    {
        elementMap.put(UntargettedBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ConstructorBinding<? extends Object> binding)
    {
        elementMap.put(ConstructorBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ConvertedConstantBinding<? extends Object> binding)
    {
        elementMap.put(ConvertedConstantBinding.class, binding);
        log(binding);
        return null;
    }

    @Override
    public Void visit(ProviderBinding<? extends Object> binding)
    {
        elementMap.put(ProviderBinding.class, binding);
        log(binding);
        return null;
    }
    
    private void log(Element element)
    {
        System.out.println("bt : "+element);
    }
   
}
