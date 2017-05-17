/**
 * This file is part of Nuun IO Kernel Specs.
 *
 * Nuun IO Kernel Specs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Specs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Specs.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.spi.topology;

import java.lang.annotation.Annotation;

@SuppressWarnings("rawtypes")
public abstract class Binding {

    public final Class key;
    public final Class qualifierClass;
    public final Annotation qualifierAnno;
    public final Object injected;

    public Binding(Class key, Class qualifier, Object injected) {
        this.key = key;
        this.qualifierClass = qualifier;
        this.qualifierAnno = null;
        this.injected = injected;
    }

    public Binding(Class key, Annotation qualifier, Object injected) {
        this.key = key;
        this.qualifierAnno = qualifier;
        this.qualifierClass = null;
        this.injected = injected;
    }

    public Binding(Class key, Object injected) {
        this(key, (Annotation) null, injected);
    }

    public String name() {
        return this.getClass().getSimpleName();
    }
}
