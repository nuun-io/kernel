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
 * This API element will give the opportunity to the kernel to assert DependencyInjectionDef aka Module object given by
 * plugins.
 * 
 * @author epo.jemba@kametic.com
 */
public interface ModuleValidation
{
    
    /**
     * 
     * @param moduleType
     * @return true if this validation object can handle this kind of class
     */
    boolean canHandle (Class<?> moduleType);
    
    /**
     * validate the given <code> dependencyInjectionDef </code>
     * 
     * @param moduleProvider
     */
    void validate(ModuleProvider moduleProvider);

}
