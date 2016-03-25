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
package io.nuun.kernel.core.internal.injection;

import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.util.Providers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import static org.reflections.ReflectionUtils.withAnnotation;

public class ClassInstaller extends Installer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassInstaller.class);
    private final Class<?> classToBind;
    private final Object scope;

    public ClassInstaller(Class<?> aClass)
    {
        this(aClass, null);
    }
    public ClassInstaller(Class<?> aClass, Object scope)
    {
        this.classToBind = aClass;
        this.scope = scope;
    }

    @Override
    protected Class<?> getOriginalClass()
    {
        return classToBind;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void install(Binder binder)
    {
        if (!classToBind.isInterface() && !withAnnotation(Nullable.class).apply(classToBind))
        {
            if (scope == null)
            {
                LOGGER.trace("Binding {} with no scope.", classToBind.getName());
                binder.bind(classToBind);
            } else
            {
                LOGGER.trace("Binding {} in scope {}.", classToBind.getName(), scope.toString());
                binder.bind(classToBind).in((Scope) scope);
            }
        } else
        {
            LOGGER.trace("Binding {} to a provider of null.", classToBind.getName());
            Provider nullProvider = Providers.of(null);
            binder.bind(classToBind).toProvider(nullProvider);
        }
    }
}
