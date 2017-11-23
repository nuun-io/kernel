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

import java.util.function.Function;

public class MultiBinding extends MetaBinding
{

    public final MultiKind                       kind;
    public final Class<? extends Function<?, ?>> keyResolver;
	public final Object value;

    public MultiBinding(Object key, MultiKind kind)
    {
        super(key);
        this.kind = kind;
        this.value = null;
        this.keyResolver = null;
    }

    public MultiBinding(Object keyKey, Object keyValue , MultiKind kind, Class<? extends Function<?, ?>> keyResolver)
    {
        super(keyKey);
		this.value = keyValue;
        this.kind = kind;
        this.keyResolver = keyResolver;
    }    

    
    public static enum MultiKind
    {
        LIST, SET, MAP, NONE
    }
}
