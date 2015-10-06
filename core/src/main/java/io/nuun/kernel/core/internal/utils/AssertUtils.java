/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.core.internal.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Provides assertions commonly used by Nuun plugins.
 */
public class AssertUtils
{

    // INTERFACE

    /**
     * Indicates if the given class is an interface.
     *
     * @param candidate the class to check
     * @return true if the class is an interface, false otherwise
     */
    public static boolean isInterface(Class<? extends Object> candidate)
    {
        return candidate.isInterface();
    }

    /**
     * Asserts that the given class is an interface.
     *
     * @param candidate the class to check
     * @throws IllegalArgumentException if the assertion is not satisfied
     */
    public static void assertInterface(Class<? extends Object> candidate)
    {
        assertionIllegalArgument(isInterface(candidate), "Type " + candidate + " must be an interface.");
    }

    // CLASS //

    /**
     * Indicates if the class is not an interface.
     *
     * @param candidate the class to check
     * @return true if class is not an interface, false otherwise
     */
    public static boolean isClass(Class<? extends Object> candidate)
    {
        return !isInterface(candidate);
    }

    /**
     * Asserts that the class is not an interface.
     *
     * @param candidate the class to check
     * @throws IllegalArgumentException if the assertion is not satisfied
     */
    public static void assertIsClass(Class<? extends Object> candidate)
    {
        assertionIllegalArgument(isClass(candidate), "Type " + candidate + " must not be an interface.");
    }

    // ANNOTATION //

    /**
     * Indicates if a class or at least one of its annotations is annotated by a given annotation.
     * <p>
     * Notice that the classes with a package name starting with "java.lang" will be ignored.
     * </p>
     * @param memberDeclaringClass the class to check
     * @param candidate the annotation to find
     * @return true if the annotation is found, false otherwise
     */
    public static boolean hasAnnotationDeep(Class<?> memberDeclaringClass, Class<? extends Annotation> candidate)
    {

        if (memberDeclaringClass.equals(candidate))
        {
            return true;
        }

        for (Annotation anno : memberDeclaringClass.getAnnotations())
        {
            Class<? extends Annotation> annoClass = anno.annotationType();
            if (!annoClass.getPackage().getName().startsWith("java.lang") && hasAnnotationDeep(annoClass, candidate))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates if the class name or at least the name of one of its annotations matches the regex.
     * <p>
     * Notice that the classes with a package name starting with "java.lang" will be ignored.
     * </p>
     * @param memberDeclaringClass the class to check
     * @param metaAnnotationRegex the regex to match
     * @return true if the regex matches, false otherwise
     */
    public static boolean hasAnnotationDeepRegex(Class<?> memberDeclaringClass, String metaAnnotationRegex)
    {
        
        if (memberDeclaringClass.getName().matches(metaAnnotationRegex))
        {
            return true;
        }
        
        for (Annotation anno : memberDeclaringClass.getAnnotations())
        {
            Class<? extends Annotation> annoClass = anno.annotationType();
            if (!annoClass.getPackage().getName().startsWith("java.lang") && hasAnnotationDeepRegex(annoClass, metaAnnotationRegex))
            {
                return true;
            }
        }
        
        return false;
    }

    public static boolean isEquivalent(Class<? extends Annotation> original, Class<? extends Annotation> copy)
    {
        for (Method originalMethod : original.getDeclaredMethods())
        {
            if (originalMethod.getParameterTypes().length == 0)
            {

                String name = originalMethod.getName();
                try
                {
                    Method cloneMethod = null;
                    if ((cloneMethod = copy.getDeclaredMethod(name)) != null)
                    {
                        if (originalMethod.getReturnType() != cloneMethod.getReturnType())
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (SecurityException e)
                {
                    return false;
                }
                catch (NoSuchMethodException e)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static <A extends Annotation> A annotationProxyOf(Class<A> annotationModelType, Annotation annotationClone)
    {
        return AnnotationCopy.of(annotationModelType, annotationClone);

    }

    public static class AnnotationCopy implements InvocationHandler
    {

        private Annotation                  annotationClone;

        AnnotationCopy( Annotation annotationClone)
        {
            this.annotationClone = annotationClone;
        }

        @SuppressWarnings("unchecked")
        public static <A extends Annotation> A of(Class<A> annotationModelType, Annotation annotationClone)
        {
            return (A) Proxy.newProxyInstance (
                    annotationModelType.getClassLoader() ,
                    new Class[] { annotationModelType } ,
                    new AnnotationCopy(annotationClone));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            String name = method.getName();
            Method method2 = annotationClone.getClass().getMethod(name);

            return method2.invoke(annotationClone);
        }
    }

    /**
     * Asserts that the given parameter is true, otherwise throws an
     * {@link java.lang.IllegalArgumentException}.
     *
     * @param asserted the assertion result
     * @param message the error message
     * @throws java.lang.IllegalArgumentException if {@code asserted} is false
     */
    public static void assertionIllegalArgument(boolean asserted, String message)
    {
        if (!asserted) {
			throw new IllegalArgumentException(message);
		}
    }

    /**
     * Asserts that the given parameter is true, otherwise throws an
     * {@link java.lang.NullPointerException}.
     *
     * @param asserted the assertion result
     * @param message the error message
     * @throws java.lang.NullPointerException if {@code asserted} is false
     */
    public static void assertionNullPointer(boolean asserted, String message)
    {
        if (!asserted) {
			throw new NullPointerException(message);
		}
    }

    /**
     * Asserts that the given parameter is not null, otherwise throws an
     * {@link java.lang.IllegalArgumentException}.
     *
     * @param underAssertion the object to check
     * @param message the error message
     * @throws java.lang.IllegalArgumentException if {@code asserted} is null
     */
    public static void assertLegal(Object underAssertion, String message)
    {
        assertionIllegalArgument(underAssertion != null, message);
    }

    /**
     * Asserts that the given parameter is not null, otherwise throws an
     * {@link java.lang.NullPointerException}.
     *
     * @param underAssertion the object to check
     * @param message the error message
     * @throws java.lang.NullPointerException if {@code asserted} is null
     */
    public static void assertNotNull(Object underAssertion, String message)
    {
        assertionNullPointer(underAssertion != null, message);
    }

}
