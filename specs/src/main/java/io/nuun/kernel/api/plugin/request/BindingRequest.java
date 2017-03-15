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

import java.util.function.Predicate;

/**
 * @author Epo Jemba
 *
 */
public class BindingRequest {

    public final RequestType requestType;
    public final Object requestedObject;
    public final Predicate<Class<?>> predicate;
    public Object requestedScope;
    public Object requestedConstraint;

    public BindingRequest(RequestType requestType, Object keyRequested) {
        this(requestType, keyRequested, null, null);
    }

    public BindingRequest(RequestType requestType, Object requestedScope, Predicate<Class<?>> predicate) {
        this(requestType, null, requestedScope, predicate);
    }

    public BindingRequest(RequestType requestType, Object keyRequested, Object requestedScope, Predicate<Class<?>> classPredicate) {
        this.requestType = requestType;
        this.requestedObject = keyRequested;
        this.requestedScope = requestedScope;
        this.predicate = classPredicate;
    }

}
