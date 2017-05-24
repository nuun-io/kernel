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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Predicate;

import javax.inject.Provider;
import javax.inject.Qualifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.BindingAnnotation;

import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.spi.topology.InstanceBinding;
import io.nuun.kernel.spi.topology.InterceptorBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;
import io.nuun.kernel.spi.topology.ProviderBinding;
import io.nuun.kernel.spi.topology.TopologyDefinition;
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
                assertPredicateOf(candidate,methodPredicate, Method.class);
                // assertMethodInterceptor

            }
            else
            {
                throw new KernelException("Method %s of class %s should have only two parameters.", candidate.getName(), candidate.getDeclaringClass()
                        .getName());
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
                Class<?> key = m.getParameterTypes()[0];
                Class<?> provided = m.getReturnType();

                assertProviderOf(key, provided);

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
                throw new KernelException("Method %s of class %s should have only one parameter.", candidate.getName(), candidate.getDeclaringClass().getName());
            }
        }

        return Optional.empty();
    }

    private void assertPredicateOf(Member context, Class<?> xPredicate, Class<?> candidateClass)
    {
        Boolean isPredicateChild = Predicate.class.isAssignableFrom(xPredicate);
        
        if ( xPredicate.equals(Predicate.class)  ) 
        {
            throw new KernelException("Topology (%s) : %s can not be passed as parameter to an intercepts definition. \nYou need to pass a child.", context , xPredicate.getName() );
        }
        
        if (! isPredicateChild ) 
        {
            throw new KernelException("Topology (%s) : Class %s should be a subclass of Predicate.", xPredicate.getName());
        }
        
        Class<?> actualClass = genericClass(Predicate.class, xPredicate, 0);
        
        if (!actualClass.equals(candidateClass)) {
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

    public Class<?> genericClass(Class<?> parentClass, Class<?> childClass, int childIndex)
    {
        return TypeResolver.resolveRawArguments(TypeResolver.resolveGenericType(parentClass, childClass), childClass)[childIndex];
    }

    @Override
    public Optional<LinkedBinding> linkedBinding(Member candidate)
    {

        if (Method.class.equals(candidate.getClass()) && candidate.getName().startsWith("injects"))
        {
            Method m = (Method) candidate;

            if (m.getParameterCount() == 1)
            {

                Class<?> key = m.getParameterTypes()[0];
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
    public Optional<InstanceBinding> instanceBinding(Member candidate)
    {
        if (Field.class.equals(candidate.getClass()))
        {

            Object instance = getValue((Field) candidate);

            if (instance == null)
            {
                throw new KernelException(
                        "Topology %s field %s is null, Please set a value.", candidate.getDeclaringClass().getSimpleName(), candidate.getName());
            }

            Field f = (Field) candidate;
            Class<?> key = f.getType();
            Optional<Annotation> qualifier = qualifier(f);

            if (qualifier.isPresent())
            {
                return Optional.of(new InstanceBinding(key, qualifier.get(), instance));
            }
            else
            {
                return Optional.of(new InstanceBinding(key, instance));
            }
        }

        return Optional.empty();
    }

    private Optional<Annotation> qualifier(AccessibleObject m)
    {

        return qualifier(m.getAnnotations());
    }

    private Optional<Annotation> qualifier(Annotation[] annotations)
    {
        return stream(annotations).filter(
                a -> a.annotationType().isAnnotationPresent(Qualifier.class) || a.annotationType().isAnnotationPresent(BindingAnnotation.class)).findFirst();
    }

    private Object getValue(Field f)
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
