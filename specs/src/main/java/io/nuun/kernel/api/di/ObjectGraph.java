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
package io.nuun.kernel.api.di;

/**
 * 
 * Object Graph is the Nuun Kernel API element abstraction for injector part of the Dependency Injection engine. 
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public interface ObjectGraph
{
    /**
     * 
     * 
     * @return the raw object graph provider.
     */
    Object get();
    
    /**
     * Cast or Convert the current objet graph injector.
     * <p>
     * Will send an IllegaStateException if can not convert or cast.
     * 
     * @param targetType
     * @return the ObjectGraph object casted as T type or adapted as T.
     */
    <T>  T as(Class<T> targetType);
}
