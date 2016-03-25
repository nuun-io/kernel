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
 * Object Graph is the Nuun Kernel API element abstraction for injector part of the Dependency Injection engine.
 *
 * @author epo.jemba{@literal @}kametic.com
 */
public interface ObjectGraph
{
    /**
     * @return the raw object graph provider.
     */
    Object get();

    /**
     * Cast or Convert the current object graph injector.
     * <p>
     * Will send an IllegalStateException if can not convert or cast.
     *
     * @param targetType the target type
     * @return the ObjectGraph object casted as T type or adapted as T.
     */
    <T>  T as(Class<T> targetType);
}
