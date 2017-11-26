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
package io.nuun.kernel.spi.topology.binding;

import java.util.Collection;
import java.util.function.Function;

public class MultiBinding extends MetaBinding
{

    public final MultiKind                       kind;
    public final Class<? extends Function<?, ?>> keyResolver;
    public final Object                          keyKey;
    //
    public Collection<Class<?>>                  classes;

    public MultiBinding(Object key, MultiKind kind)
    {
        super(key);
        this.kind = kind;
        this.keyKey = null;
        this.keyResolver = null;
    }

    public MultiBinding(Object key, Object keyKey, MultiKind kind, Class<? extends Function<?, ?>> keyResolver)
    {
        super(key);
        this.keyKey = keyKey;
        this.kind = kind;
        this.keyResolver = keyResolver;
    }

    public static enum MultiKind
    {
        SET, MAP, NONE
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kind == null) ? 0 : kind.hashCode());
        result = prime * result + ((keyKey == null) ? 0 : keyKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MultiBinding other = (MultiBinding) obj;
        if (kind != other.kind)
            return false;
        if (keyKey == null)
        {
            if (other.keyKey != null)
                return false;
        }
        else if (!keyKey.equals(other.keyKey))
            return false;
        return true;
    }

}
