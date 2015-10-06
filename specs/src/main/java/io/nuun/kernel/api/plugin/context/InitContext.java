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

import io.nuun.kernel.api.Plugin;
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

    public abstract Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass();

    public abstract Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass();
    
    public abstract Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex();

    public abstract Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass();

    public abstract Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex();

    public abstract Map<String, Collection<String>> mapPropertiesFilesByPrefix();

    public abstract String kernelParam(String key);

    public abstract Collection<Class<?>> classesToBind();

    public abstract List<UnitModule> moduleResults();
    
    public abstract List<UnitModule> moduleOverridingResults();

    public abstract Collection<String> propertiesFiles();

    public abstract Map<String, Collection<Class<?>>> scannedTypesByRegex();

    public abstract Map<String, Collection<String>> mapResourcesByRegex();

    public abstract Map<Specification, Collection<Class<?>>> scannedTypesBySpecification();
    
    /**
     * Returns plugin instances required by the current plugin.
     * The plugin's init phase will be executed after theirs.
     * 
     * @return the instances of the plugin declared required by the method Plugin.pluginDependenciesRequired()
     */
    public abstract Collection<? extends Plugin> pluginsRequired();

    
    /**
     * Returns instances of the plugins that become dependent on this plugin.
     * The plugin's init phase will be executed before theirs.
     * 
     * @return dependent plugins
     */
    public abstract Collection<? extends Plugin> dependentPlugins();

    /**
     * The current initialization round.
     * 
     * @return the current round number
     */
    public abstract int roundNumber();

}
