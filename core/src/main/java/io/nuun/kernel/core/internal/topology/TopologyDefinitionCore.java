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

import static java.util.Arrays.stream;

import java.awt.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.inject.Qualifier;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.BindingAnnotation;
import com.google.inject.TypeLiteral;

import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.spi.topology.Multi;
import io.nuun.kernel.spi.topology.TopologyDefinition;
import io.nuun.kernel.spi.topology.binding.InstanceBinding;
import io.nuun.kernel.spi.topology.binding.InterceptorBinding;
import io.nuun.kernel.spi.topology.binding.LinkedBinding;
import io.nuun.kernel.spi.topology.binding.MultiBinding;
import io.nuun.kernel.spi.topology.binding.MultiBinding.MultiKind;
import io.nuun.kernel.spi.topology.binding.NullableBinding;
import io.nuun.kernel.spi.topology.binding.ProviderBinding;
import net.jodah.typetools.TypeResolver;

class TopologyDefinitionCore implements TopologyDefinition
{

    private final Logger logger = LoggerFactory.getLogger(TopologyDefinitionCore.class);

    @Override
    public Optional<InterceptorBinding> interceptorBinding(Member candidate)
    {

        if (Method.class.equals(candidate.getClass()) && candidate.getName().startsWith("intercepts"))
        {
            Method m = (Method) candidate;

            if (m.getParameterCount() == 2)
            {
                Class<?> classPredicate = m.getParameterTypes()[0];
                assertPredicateOf(candidate, classPredicate, Class.class);
                // assertMethodPredicate
                Class<?> methodPredicate = m.getParameterTypes()[1];
                assertPredicateOf(candidate, methodPredicate, Method.class);
                // assertMethodInterceptor
                Class<?> interceptor = m.getReturnType();
                assertInterceptor(m, interceptor);

                return Optional.of(new InterceptorBinding(classPredicate, methodPredicate, interceptor));

            }
            else
            {
                throw new KernelException(
                        "Method %s of class %s should have only two parameters.", candidate.getName(), candidate.getDeclaringClass().getName());
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProviderBinding> providerBinding(Member candidate)
    {

        if (Method.class.equals(candidate.getClass()) && candidate.getName().startsWith("provides"))
        {
            Method m = (Method) candidate;

            if (m.getParameterCount() == 1)
            {

                TypeLiteral<?> key = typeLiteral(m.getParameterTypes()[0]);

                Class<?> provided = m.getReturnType();

                assertProviderOf(m.getParameterTypes()[0], provided);

                Optional<Annotation> qualifier = qualifier(m.getParameterAnnotations()[0]);
                if (!qualifier.isPresent())
                {
                    return Optional.of(new ProviderBinding(key, provided));
                }
                else
                {
                    return Optional.of(new ProviderBinding(key, qualifier.get(), provided));
                }

            }
            else
            {
                throw new KernelException(
                        "Method %s of class %s should have only one parameter.", candidate.getName(), candidate.getDeclaringClass().getName());
            }
        }

        return Optional.empty();
    }

    private void assertInterceptor(Member context, Class<?> interceptor)
    {
        Boolean isInterceptorChild = MethodInterceptor.class.isAssignableFrom(interceptor);

        if (interceptor.equals(MethodInterceptor.class))
        {
            throw new KernelException("Topology (%s) : \n  The return type cannot be %s", context, MethodInterceptor.class.getSimpleName());
        }

        if (!isInterceptorChild)
        {
            throw new KernelException("Topology (%s) : Class %s should be an implementation of.", context, interceptor.getName());
        }
    }

    private void assertPredicateOf(Member context, Class<?> xPredicate, Class<?> candidateClass)
    {
        Boolean isPredicateChild = Predicate.class.isAssignableFrom(xPredicate);

        if (xPredicate.equals(Predicate.class))
        {
            throw new KernelException(
                    "Topology (%s) : %s can not be passed as parameter to an intercepts definition. \nYou need to pass a child.", context,
                    xPredicate.getName());
        }

        if (!isPredicateChild)
        {
            throw new KernelException("Topology (%s) : Class %s should be an implementation of Predicate.", xPredicate.getName());
        }

        Class<?> actualClass = genericClass(Predicate.class, xPredicate, 0);

        if (!actualClass.equals(candidateClass))
        {
            throw new KernelException("Class %s should be a %s.", candidateClass.getName(), actualClass.getName());
        }

    }

    private void assertProviderOf(Class<?> key, Class<?> providerChild)
    {
        Boolean jsr = Provider.class.isAssignableFrom(providerChild);
        Boolean google = com.google.inject.Provider.class.isAssignableFrom(providerChild);

        if (providerChild.equals(Provider.class) || providerChild.equals(com.google.inject.Provider.class) || ((!jsr) && (!google)))
        {
            throw new KernelException("Class %s should be a subclass of JSR330 or Guice Provider.", providerChild.getName());

        }

        Class<?> providerClass = null;
        if (jsr)
        {
            providerClass = Provider.class;
        }
        if (google)
        {
            providerClass = com.google.inject.Provider.class;
        }

        int classIndex = 0;

        Class<?> provided = genericClass(providerClass, providerChild, classIndex);

        if (!key.equals(provided))
        {
            throw new KernelException("Parameter key %s should be a equals to Provider<T> type parameter which is %s.", key.getName(), provided.getName());
        }
    }

    private Class<?> genericClass(Class<?> parentClass, Class<?> childClass, int childIndex)
    {
        return TypeResolver.resolveRawArguments(TypeResolver.resolveGenericType(parentClass, childClass), childClass)[childIndex];
    }

    private Type[] genericClass(Type childClass)
    {
        ParameterizedType type = (ParameterizedType) childClass;
        
        return type.getActualTypeArguments();

    }

    @Override
    public Optional<LinkedBinding> linkedBinding(Member candidate)
    {

        if (Method.class.equals(candidate.getClass()))
        {
            Method m = (Method) candidate;

            Boolean isMulti = m.isAnnotationPresent(Multi.class);

            if (m.getParameterCount() == 1 && candidate.getName().startsWith("injects") && !isMulti)
            {
                TypeLiteral<?> key = typeLiteral(m.getParameters()[0].getParameterizedType());

                Class<?> provided = m.getReturnType();

                Optional<Annotation> qualifier = qualifier(m.getParameterAnnotations()[0]);
                if (!qualifier.isPresent())
                {
                    return Optional.of(new LinkedBinding(key, provided));
                }
                else
                {
                    return Optional.of(new LinkedBinding(key, qualifier.get(), provided));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<MultiBinding> multiBinding(Member candidate)
    {

        Boolean isMulti = ((AccessibleObject) candidate).isAnnotationPresent(Multi.class);

        if (isMulti)
        {
            return extractMultiBinding(candidate);
        }

        return Optional.empty();

    }

    @Override
    public Optional<InstanceBinding> instanceBinding(Member candidate)
    {
        if (Field.class.equals(candidate.getClass()))
        {
            Field f = Field.class.cast(candidate);

            Boolean isNullable = f.isAnnotationPresent(Nullable.class);

            Boolean isMulti = f.isAnnotationPresent(Multi.class);

            Object instance = value((Field) candidate);

            if (instance == null && !isNullable && !isMulti)
            {
                throw new KernelException(
                        "Topology %s field %s is null, Please set a value.", candidate.getDeclaringClass().getSimpleName(), candidate.getName());
            }

            if (isNullable && isMulti)
            {
                throw new KernelException(
                        "Topology %s field %s can not be @Nullable and @Multi.", candidate.getDeclaringClass().getSimpleName(), candidate.getName());
            }

            TypeLiteral<?> key = typeLiteral(f.getGenericType());

            Optional<Annotation> qualifier = qualifier(f);

            if (instance != null && !isMulti)
            {
                if (qualifier.isPresent())
                {
                    return Optional.of(new InstanceBinding(key, qualifier.get(), instance));
                }
                else
                {
                    return Optional.of(new InstanceBinding(key, instance));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<NullableBinding> nullableBinding(Member candidate)
    {

        if (Field.class.equals(candidate.getClass()))
        {
            Field f = Field.class.cast(candidate);

            Boolean isNullable = f.isAnnotationPresent(Nullable.class);

            Boolean isMulti = f.isAnnotationPresent(Multi.class);

            if (isNullable && !isMulti)
            {
                TypeLiteral<?> key = typeLiteral(f.getGenericType());
                Optional<Annotation> qualifier = qualifier(f);

                if (qualifier.isPresent())
                {
                    return Optional.of(new NullableBinding(key, qualifier.get()));
                }
                else
                {
                    return Optional.of(new NullableBinding(key));
                }
            }

        }

        return Optional.empty();
    }

    private Optional<MultiBinding> extractMultiBinding(Member candidate)
    {

        MultiKind kind = kindFromMember(candidate);
        Multi multi = ((AccessibleObject) candidate).getAnnotation(Multi.class);
        Type keyType = null;

        if (candidate instanceof Method)
        {
            Method method = (Method) candidate;
            keyType = method.getGenericReturnType();
        }
        else if (candidate instanceof Field)
        {
            Field f = (Field) candidate;
            keyType = f.getGenericType();
        }

        if (kind == MultiKind.SET || kind == MultiKind.LIST)
        {
            Type[] collectionOf = genericClass(keyType);
            return Optional.of(new MultiBinding(collectionOf[0], kind));
        }
        else if (kind == MultiKind.MAP)
        {
            if (multi.value().equals(Multi.VoidFunction.class))
            {
                throw new KernelException(
                        "Topology %s field %s : @Multi.value() must be set.", candidate.getDeclaringClass().getSimpleName(), candidate.getName());
            }
            Type[] mapOf = genericClass(keyType);

            return Optional.of(new MultiBinding(mapOf[0], mapOf[1], kind, multi.value()));
        }

        return Optional.empty();
    }

    private MultiKind kindFromMember(Member member)
    {

        Class<?> type = null;

        if (member instanceof Field)
        {
            Field f = (Field) member;

            type = f.getType();

        }
        else if (member instanceof Method)
        {
            Method m = (Method) member;

            type = m.getReturnType();
        }

        if (type.isAssignableFrom(List.class))
        {
            return MultiKind.LIST;
        }
        if (type.isAssignableFrom(Set.class))
        {
            return MultiKind.SET;
        }
        if (type.isAssignableFrom(Map.class))
        {
            return MultiKind.MAP;
        }

        return MultiKind.NONE;
    }

    private TypeLiteral<?> typeLiteral(Type key)
    {
        return TypeLiteral.get(key);
    }

    private Optional<Annotation> qualifier(AccessibleObject m)
    {

        return qualifier(m.getAnnotations());
    }

    private Optional<Annotation> qualifier(Annotation[] annotations)
    {
        return stream(annotations)
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class) || a.annotationType().isAnnotationPresent(BindingAnnotation.class))
                .findFirst();
    }

    private Object value(Field f)
    {
        try
        {
            return f.get(null);
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
