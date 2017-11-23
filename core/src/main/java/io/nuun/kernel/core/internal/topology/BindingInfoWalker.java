/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.core.internal.topology;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import io.nuun.kernel.spi.topology.binding.MultiBinding.MultiKind;

@SuppressWarnings({
        "unchecked", "rawtypes"
})
public class BindingInfoWalker implements Walker
{

    private BindingInfos bindingInfos;

    public BindingInfoWalker(BindingInfos bindingInfos)
    {
        this.bindingInfos = bindingInfos;
    }

    @Override
    public void bindInstance(TypeLiteral typeLiteral, Annotation qualifierAnno, Object injected)
    {
        updateBindingInfo(typeLiteral, qualifierAnno);
    }

    @Override
    public void bindInstance(TypeLiteral typeLiteral, Object injected)
    {
        updateBindingInfo(typeLiteral);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral, Annotation qualifierAnno, Class<?> injected)
    {
        updateBindingInfo(typeLiteral, qualifierAnno);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral, Annotation qualifierAnno, Object injected)
    {
        updateBindingInfo(typeLiteral, qualifierAnno);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral, Class<?> injected)
    {
        updateBindingInfo(typeLiteral);
    }

    @Override
    public void bindLink(TypeLiteral typeLiteral)
    {
        updateBindingInfo(typeLiteral);
    }

    @Override
    public void bindProvider(TypeLiteral typeLiteral, Annotation qualifierAnno, Class<?> injected)
    {
        updateBindingInfo(typeLiteral, qualifierAnno);
    }

    @Override
    public void bindProvider(TypeLiteral typeLiteral, Class<?> injected)
    {
        updateBindingInfo(typeLiteral);
    }

    @Override
    public void bindInterceptor(Class<?> classPredicate, Class<?> methodPredicate, Class<?> methodInterceptor)
    {

    }

    private void updateBindingInfo(TypeLiteral typeLiteral, Annotation qualifierAnno)
    {
        bindingInfos.put(Key.get(typeLiteral, qualifierAnno), BindingInfo.IS_BOUND);
    }

    private void updateBindingInfo(TypeLiteral typeLiteral)
    {
        bindingInfos.put(Key.get(typeLiteral), BindingInfo.IS_BOUND);
    }

    @Override
    public void bindMulti(TypeLiteral keyTypeLiteral, TypeLiteral valueTypeLiteral, MultiKind kind, Class<? extends Function<?, ?>> keyResolver)
    {
        // TODO Auto-generated method stub

    }

}
