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
package it.fixture.dependencies;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class WithRequiredFacetPlugin extends AbstractPlugin
{

    public static final String NAME = "with-required-deps";

    @Override
    public String name()
    {
        return NAME;
    }


    @Override
    public InitState init(InitContext initContext)
    {

        Facet1 facet1 = initContext.dependency(Facet1.class);

        if (!facet1.isInitialized())
        {
            throw new IllegalStateException("Facet1 should be initialized before WithRequiredFacetPlugin");
        }

        List<?> dependencies = initContext.dependencies();
        if (dependencies.size() != 1)
        {
            throw new IllegalStateException("WithRequiredFacetPlugin should have only one dependency");
        }

        List<Facet1> facet1s = initContext.dependencies(Facet1.class);
        if (facet1s.size() != 1)
        {
            throw new IllegalStateException("WithRequiredFacetPlugin should have only one dependency");
        }

        return InitState.INITIALIZED;
    }

    @Override
    public Collection<Class<?>> requiredPlugins()
    {
        return Lists.<Class<?>>newArrayList(Facet1.class);
    }
}
