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
