package io.nuun.kernel.core.internal.topology;

import java.lang.annotation.Annotation;

import com.google.inject.TypeLiteral;

public interface BindingWalker
{

    void bindInstance(TypeLiteral typeLiteral, Annotation qualifierAnno, Object injected);

    void bindInstance(TypeLiteral typeLiteral, Object injected);

    void bindLink(TypeLiteral typeLiteral, Annotation qualifierAnno, Class<?> injected);

    void bindLink(TypeLiteral typeLiteral, Annotation qualifierAnno, Object injected);

    void bindLink(TypeLiteral typeLiteral, Class<?> injected);

    void bindLink(TypeLiteral typeLiteral);

    void bindProvider(TypeLiteral typeLiteral, Annotation qualifierAnno, Class<?> injected);

    void bindProvider(TypeLiteral typeLiteral, Class<?> injected);

    void bindInterceptor(Class<?> classPredicate, Class<?> methodPredicate, Class<?> methodInterceptor);

}
