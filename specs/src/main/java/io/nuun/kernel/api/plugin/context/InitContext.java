/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.api.plugin.context;

import io.nuun.kernel.api.di.UnitModule;
import org.kametic.specifications.Specification;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The holder class containing all the data available at the {@code init} step.
 * 
 * @author Epo Jemba
 */
@SuppressWarnings("rawtypes")
public interface InitContext
{

    Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass();

    Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass();
    
    Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex();

    Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass();

    Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex();

    Map<String, Collection<String>> mapPropertiesFilesByPrefix();

    Map<String, String> kernelParams();

    String kernelParam(String key);

    Collection<Class<?>> classesToBind();

    List<UnitModule> moduleResults();
    
    List<UnitModule> moduleOverridingResults();

    Collection<String> propertiesFiles();

    Map<String, Collection<Class<?>>> scannedTypesByRegex();

    Map<String, Collection<String>> mapResourcesByRegex();

    Map<Specification, Collection<Class<?>>> scannedTypesBySpecification();
    
    /**
     * Returns plugin instances required by the current plugin.
     * The plugin's init phase will be executed after theirs.
     * 
     * @return the instances of the plugin declared required by the method Plugin.pluginDependenciesRequired()
     */
    @Deprecated
    Collection<?> pluginsRequired();
    
    /**
     * Returns instances of the plugins that become dependent on this plugin.
     * The plugin's init phase will be executed before theirs.
     * 
     * @return dependent plugins
     */
    @Deprecated
    Collection<?> dependentPlugins();

    List<?> dependencies();

    <T> T dependency(Class<T> dependencyClass);
    /**
     * The current initialization round.
     * 
     * @return the current round number
     */
    int roundNumber();

}
