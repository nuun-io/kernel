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

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class WithRequiredDepsPlugin extends AbstractPlugin {

    public static final String NAME = "with-required-deps";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public InitState init(InitContext initContext) {
        boolean isInitialized = false;

        for (Object plugin : initContext.pluginsRequired()) {
            if (plugin instanceof RequiredPlugin1){
                isInitialized = ((RequiredPlugin1) plugin).isInitialized();
            }
        }

        if (!isInitialized) {
            throw new IllegalStateException("RequiredPlugin1 should not be initialized before WithRequiredDepsPlugin");
        }

        return InitState.INITIALIZED;
    }

    @Override
    public Collection<Class<?>> requiredPlugins() {
        return Lists.<Class<?>>newArrayList(RequiredPlugin1.class);
    }

}
