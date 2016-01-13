/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
 * <p/>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package io.nuun.kernel.api.plugin.request;

import org.kametic.specifications.Specification;


/**
 * @author Epo Jemba
 *
 */
public class BindingRequest {

    public final RequestType requestType;
    public final Object requestedObject;
    public final Specification<Class<?>> specification;
    public Object requestedScope;
    public Object requestedConstraint;

    public BindingRequest(RequestType requestType, Object keyRequested) {
        this(requestType, keyRequested, null, null);
    }

    public BindingRequest(RequestType requestType, Object requestedScope, Specification<Class<?>> specification) {
        this(requestType, null, requestedScope, specification);
    }

    public BindingRequest(RequestType requestType, Object keyRequested, Object requestedScope, Specification<Class<?>> specification) {
        this.requestType = requestType;
        this.requestedObject = keyRequested;
        this.requestedScope = requestedScope;
        this.specification = specification;
    }

}
