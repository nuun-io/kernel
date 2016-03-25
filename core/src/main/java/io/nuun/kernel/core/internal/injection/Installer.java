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
import com.google.inject.matcher.Matchers;
import io.nuun.kernel.spi.Concern;

import java.lang.annotation.Annotation;

public abstract class Installer implements Comparable<Installer>
{
    @Override
    public int compareTo(Installer other)
    {
        return order().compareTo(other.order());
    }

    public Long order()
    {
        for (Annotation annotation : getOriginalClass().getAnnotations())
        {
            if (Matchers.annotatedWith(Concern.class).matches(annotation.annotationType()))
            {
                Concern concern = annotation.annotationType().getAnnotation(Concern.class);
                return concern.priority().value() + concern.order();
            }
        }
        return 0L;
    }

    protected abstract void install(Binder binder);

    protected abstract Class<?> getOriginalClass();

}
