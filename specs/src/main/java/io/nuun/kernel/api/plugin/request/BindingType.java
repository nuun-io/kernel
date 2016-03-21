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
package io.nuun.kernel.api.plugin.request;

public enum BindingType
{
    /**
     * Binds the Implementation to itself. Equals binder.bind(Implementation.class);
     */
    IMPLEMENTATION,
    /**
     * Binds the Implementation to all implemented Interfaces. Equals: for(Interface interface:
     * implementedInterfaces) binder.bind(interface).to(implementation);
     */
    INTERFACES,
    /**
     * Binds the Implementation to the extended Super-Class. Equals:
     * binder.bind(superclass).to(implementation);
     */
    SUPER,
    /**
     * Binds the Implementation to the Classes specifed by @To(to={}) Equals: for(Class{@literal<?>} class: toClasses)
     * binder.bind(class).to(implementation);
     */
    CUSTOM
}
