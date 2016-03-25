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
package io.nuun.kernel.core.internal;

import com.google.inject.Injector;
import io.nuun.kernel.api.di.ObjectGraph;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.core.internal.injection.ObjectGraphEmbedded;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Epo Jemba
 * 
 */
@Singleton
public class ContextInternal implements Context
{

    public final Injector mainInjector;

    /**
     * Constructor.
     *
     * @param mainInjector the guice main injector
     */
    @Inject
    public ContextInternal(Injector mainInjector)
    {
        this.mainInjector = mainInjector;
    }

    @Override
    public ObjectGraph applicationObjectGraph() {
        return new ObjectGraphEmbedded(mainInjector);
    }
}
