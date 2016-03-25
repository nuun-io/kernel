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
