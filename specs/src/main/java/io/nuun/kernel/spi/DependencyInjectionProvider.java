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
package io.nuun.kernel.spi;

import io.nuun.kernel.api.di.UnitModule;

/**
 * This provider interface is an SPI to change the dependency injection engine used by Nuun.
 */
public interface DependencyInjectionProvider
{

    /**
     * Checks whether the dependency injection provider supports the given unit module.
     *
     * @param bindingDefinitions an object containing binding definitions
     * @return true if the provider supports this unit module, false otherwise
     */
    boolean canHandle(Class<?> bindingDefinitions);

    /**
     * Converts a unit module from a dependency injection provider into a Nuun unit module.
     * <p>
     * Internally Nuun will use a Guice unit module.
     * </p>
     *
     * @param bindingDefinitions an object containing binding definitions
     *
     * @return a Nuun unit module
     */
    UnitModule convert(Object bindingDefinitions);

    /**
     * Returns a bridge from kernel to module with this DI.
     * For example a FactoryBean in the Spring world.
     *
     * @return an object able to deal with all kernel dependencies
     */
    Object kernelDIProvider();

}
