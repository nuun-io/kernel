package io.nuun.kernel.core.internal.topology;

import java.lang.annotation.Annotation;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

@SuppressWarnings({"unchecked" , "rawtypes"})
public class BindingInfoWalker implements BindingWalker
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

}
