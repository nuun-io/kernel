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
package io.nuun.kernel.core.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.matcher.Matchers;
import com.google.inject.util.Providers;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.spi.Concern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.*;

import static org.reflections.ReflectionUtils.withAnnotation;

/**
 * Bootstrap the plugins needed to initialize the application.
 *
 * @author ejemba
 */
public class KernelGuiceModuleInternal extends AbstractModule
{

    private final Logger logger = LoggerFactory.getLogger(KernelGuiceModuleInternal.class);
    private final RequestHandler requestHandler;
    private boolean overriding = false;

    public KernelGuiceModuleInternal(RequestHandler requestHandler)
    {
        this.requestHandler = requestHandler;
    }

    public KernelGuiceModuleInternal overriding()
    {
        overriding = true;
        return this;
    }

    @Override
    protected final void configure()
    {
        // All bindings will be needed explicitly.
        // this simple line makes the framework bullet-proof !
        binder().requireExplicitBindings();

        bind(Context.class).to(ContextInternal.class);

        // Bind Types, Subtypes from classpath
        bindFromClasspath();

        // Start Plugins
    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    private void bindFromClasspath()
    {
        List<Installable> installableList = new ArrayList<Installable>();
        Map<Class<?>, Object> classesWithScopes = requestHandler.getClassesWithScopes();

        if (!overriding)
        {
            Collection<Class<?>> classes = requestHandler.getClassesToBind();

            for (Object o : classes)
            {
                installableList.add(new Installable(o));
            }
            for (Object o : requestHandler.getModules())
            {
                installableList.add(new Installable(o));
            }
        } else
        {
            logger.debug("Installing overriding modules");
            for (Object o : requestHandler.getOverridingModules())
            {
                installableList.add(new Installable(o));
            }
        }

        Collections.sort(installableList, Collections.reverseOrder());

        Provider nullProvider = Providers.of(null);

        // We install modules and bind class in the right orders
        for (Installable installable : installableList)
        {
            Object installableInner = installable.inner;
            // Checking for Module
            if (UnitModule.class.isAssignableFrom(installableInner.getClass()))
            { // install module
                Object moduleObject = UnitModule.class.cast(installableInner).nativeModule();
                if (Module.class.isAssignableFrom(moduleObject.getClass()))
                {
                    logger.debug("installing module {}", moduleObject);
                    install(Module.class.cast(moduleObject));
                } else
                {
                    throw new KernelException("Can not install " + moduleObject + ". It is not a Guice Module");
                }
            }
            // Checking for class
            if (installableInner instanceof Class)
            { // bind object

                Class<?> classpathClass = Class.class.cast(installableInner);

                Object scope = classesWithScopes.get(classpathClass);

                if (!(classpathClass.isInterface() && withAnnotation(Nullable.class).apply(classpathClass)))
                {
                    if (scope == null)
                    {
                        logger.debug("binding {} with no scope.", classpathClass.getName());
                        bind(classpathClass);
                    } else
                    {
                        logger.debug("binding {} in scope {}.", classpathClass.getName(), scope.toString());
                        bind(classpathClass).in((Scope) scope);
                    }
                } else
                {
                    bind(classpathClass).toProvider(nullProvider);
                }

            }
        }
    }

    Long computeOrder(Class<?> moduleClass)
    {

        Long finalOrder = 0l;
        boolean reachAtLeastOnce = false;

        for (Annotation annotation : moduleClass.getAnnotations())
        {
            if (Matchers.annotatedWith(Concern.class).matches(annotation.annotationType()))
            {
                reachAtLeastOnce = true;
                Concern concern = annotation.annotationType().getAnnotation(Concern.class);
                switch (concern.priority())
                {
                    case HIGHEST:
                        finalOrder += (3L << 32) + concern.order();
                        break;
                    case HIGHER:
                        finalOrder += (2L << 32) + concern.order();
                        break;
                    case HIGH:
                        finalOrder += (1L << 32) + concern.order();
                        break;
                    case NORMAL:
                        finalOrder = (long) concern.order();
                        break;
                    case LOW:
                        finalOrder -= (1L << 32) + concern.order();
                        break;
                    case LOWER:
                        finalOrder -= (2L << 32) + concern.order();
                        break;
                    case LOWEST:
                        finalOrder -= (3L << 32) + concern.order();
                        break;
                    default:
                        break;
                }

                break;
            }
        }

        if (!reachAtLeastOnce)
        {
            finalOrder = (long) 0;
        }

        return finalOrder;
    }

    class Installable implements Comparable<Installable>
    {

        private Object inner;

        Installable(Object inner)
        {
            this.inner = inner;
        }

        @Override
        public int compareTo(Installable anInstallable)
        {
            Class<?> toCompare;
            Class<?> innerClass;


            // to compare inner is a class to bind
            if (anInstallable.inner instanceof Class)
            {
                toCompare = (Class<?>) anInstallable.inner;
            } else if (Module.class.isAssignableFrom(anInstallable.inner.getClass()))
            // inner is a module annotated
            {
                toCompare = anInstallable.inner.getClass();
            } else if (UnitModule.class.isAssignableFrom(anInstallable.inner.getClass()))
            // inner is a UnitModule, we get the class of the wrapper
            {
                toCompare = UnitModule.class.cast(anInstallable.inner).nativeModule().getClass();
            } else
            {
                throw new IllegalStateException("Object to compare is not a class nor a Module " + anInstallable);
            }

            // inner is a class to bind
            if (inner instanceof Class)
            {
                innerClass = (Class<?>) inner;
            } else if (Module.class.isAssignableFrom(inner.getClass()))
            // inner is a module annotated
            {
                innerClass = inner.getClass();
            } else if (UnitModule.class.isAssignableFrom(inner.getClass()))
            // inner is a UnitModule, we get the class of the wrapper
            {
                innerClass = UnitModule.class.cast(inner).nativeModule().getClass();
            } else
            {
                throw new IllegalStateException("Object to compare is not a class nor a Module " + this);
            }

            return computeOrder(innerClass).compareTo(computeOrder(toCompare));
        }

        @Override
        public String toString()
        {
            return inner.toString();
        }
    }

}
