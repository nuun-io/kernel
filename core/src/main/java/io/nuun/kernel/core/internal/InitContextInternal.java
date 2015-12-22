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
package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.context.InitContext;
import org.kametic.specifications.Specification;

import java.lang.annotation.Annotation;
import java.util.*;

public class InitContextInternal implements InitContext
{
    private final Map<String, String> kernelParams;
    private final RequestHandler requestHandler;
    private final int roundNumber;
    private final DependencyProvider dependencyProvider;
    private final Class<? extends Plugin> pluginClass;

    public InitContextInternal(Map<String, String> kernelParams, RequestHandler requestHandler, int roundNumber,
                               DependencyProvider dependencyProvider, Class<? extends Plugin> pluginClass)
    {
        this.kernelParams = kernelParams;
        this.requestHandler = requestHandler;
        this.roundNumber = roundNumber;
        this.dependencyProvider = dependencyProvider;
        this.pluginClass = pluginClass;
    }

    @Override
    public int roundNumber()
    {
        return roundNumber;
    }

    @Override
    public Map<String, String> kernelParams()
    {
        return kernelParams;
    }

    @Override
    public String kernelParam(String key)
    {
        return kernelParams.get(key);
    }

    @Override
    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass()
    {
        return requestHandler.scannedSubTypesByParentClass();
    }

    @Override
    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass()
    {
        return requestHandler.scannedSubTypesByAncestorClass();
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex()
    {
        return requestHandler.scannedSubTypesByParentRegex();
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedTypesByRegex()
    {
        return requestHandler.scannedTypesByRegex();
    }

    @Override
    public Map<Specification, Collection<Class<?>>> scannedTypesBySpecification()
    {
        return requestHandler.scannedTypesBySpecification();
    }

    @Override
    public Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass()
    {
        return requestHandler.scannedClassesByAnnotationClass();
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex()
    {
        return requestHandler.scannedClassesByAnnotationRegex();
    }

    @Override
    public Map<String, Collection<String>> mapPropertiesFilesByPrefix()
    {
        return requestHandler.getPropertiesFilesByPrefix();
    }

    @Override
    public Map<String, Collection<String>> mapResourcesByRegex()
    {
        return requestHandler.getResourcesByRegex();
    }

    @Override
    public Collection<Class<?>> classesToBind()
    {
        return requestHandler.getClassesToBind();
    }

    @Override
    public List<UnitModule> moduleResults()
    {
        return requestHandler.getModules();
    }

    @Override
    public List<UnitModule> moduleOverridingResults()
    {
        return requestHandler.getOverridingModules();
    }

    @Override
    public Collection<String> propertiesFiles()
    {
        return requestHandler.getPropertyFiles();
    }

    @Override
    public Collection<? extends Plugin> pluginsRequired()
    {
        return dependencyProvider.getRequiredPluginsOf(pluginClass);
    }

    @Override
    public Collection<? extends Plugin> dependentPlugins()
    {
        return dependencyProvider.getDependentPluginsOf(pluginClass);
    }

    @Override
    public List<?> dependencies()
    {
        return dependencyProvider.getDependenciesOf(pluginClass);
    }

    @Override
    public <T> List<T> dependencies(Class<T> dependencyClass)
    {
        return dependencyProvider.getFacets(pluginClass, dependencyClass);
    }

    @Override
    public <T> T dependency(Class<T> dependencyClass)
    {
        return dependencyProvider.getFacet(pluginClass, dependencyClass);
    }

}
