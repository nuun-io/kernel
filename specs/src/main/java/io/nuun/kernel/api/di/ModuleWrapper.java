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
package io.nuun.kernel.api.di;

/**
 * An encapsulation of the bindings definition of the actual dependency engine.
 * <p>
 * The encapsulated object, can be a <strong>Guice Module</strong>, and
 * <strong>ApplicationContext</strong>, <strong> PicoContainer </strong>, etc
 * </p>
 *
 * @author epo.jemba{@literal @}kametic.com
 */
public interface ModuleWrapper
{
    /**
     * Returns a module which can be handled by the Nuun's internal DI engine.
     *
     * @return the native module
     */
    Object nativeModule();
    
    /**
     * Cast the current object module.
     * 
     * @param targetType the target type
     * @throws IllegalStateException if can not convert or cast
     * @return the ObjectGraph object casted as T type or adapted as T.
     */
    <T> T as(Class<T> targetType);
}
