/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
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
