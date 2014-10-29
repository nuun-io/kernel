/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.tests.internal.dsl.holder;

import io.nuun.kernel.tests.ut.assertor.dsl.Wildcard;

import org.kametic.specifications.Specification;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
 *
 */
public interface InjectedHolder extends HolderBase
{
    
    void setInstance(Object injectedInstance);
    
    void setSpecification(Specification<Object> injectedSpecification);
    
    void setClass(Class<?> injectedClass) ;
    
    void setWildcard(Wildcard injectedWildcard);
    
    void setInjectedTimes(Integer injectedTimes);

}
