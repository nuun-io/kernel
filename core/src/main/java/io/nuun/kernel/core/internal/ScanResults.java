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

import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.request.RequestType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.kametic.specifications.Specification;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

import static java.util.Collections.*;

public class ScanResults
{

    private final List<UnitModule> childModules = new ArrayList<UnitModule>();
    private final List<UnitModule> childOverridingModules = new ArrayList<UnitModule>();

    private final Set<Class<?>> classesToBind = new HashSet<Class<?>>();
    private final Map<Class<?>, Collection<Class<?>>> mapSubTypes = new HashMap<Class<?>, Collection<Class<?>>>();
    private final Map<Class<?>, Collection<Class<?>>> mapAncestorTypes = new HashMap<Class<?>, Collection<Class<?>>>();
    private final Map<String, Collection<Class<?>>> mapSubTypesByName = new HashMap<String, Collection<Class<?>>>();
    private final Map<String, Collection<Class<?>>> mapTypesByName = new HashMap<String, Collection<Class<?>>>();
    private final Map<Specification, Collection<Class<?>>> mapTypesBySpecification = new HashMap<Specification, Collection<Class<?>>>();
    private final Map<Class<? extends Annotation>, Collection<Class<?>>> mapAnnotationTypes = new HashMap<Class<? extends Annotation>, Collection<Class<?>>>();
    private final Map<String, Collection<Class<?>>> mapAnnotationTypesByName = new HashMap<String, Collection<Class<?>>>();
    private final Map<String, Collection<String>> propertyFilesByPrefix = new HashMap<String, Collection<String>>();
    private final Map<String, Collection<String>> resourcesByRegex = new HashMap<String, Collection<String>>();
    private final Collection<String> propertyFiles = new HashSet<String>();
    private final Set<URL> urls = new HashSet<URL>();

    protected static class Key
    {
        private final RequestType type;
        private final Object key;

        public Key(RequestType type, Object key)
        {
            this.type = type;
            this.key = key;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Key)
            {
                Key key = (Key) obj;
                return new EqualsBuilder().append(type, key.type).append(this.key, key.key).isEquals();
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return new HashCodeBuilder().append(type).append(key).toHashCode();
        }
    }

    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass()
    {
        return unmodifiableMap(mapSubTypes);
    }

    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass()
    {
        return unmodifiableMap(mapAncestorTypes);
    }

    public Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex()
    {
        return unmodifiableMap(mapSubTypesByName);
    }

    public Map<String, Collection<Class<?>>> scannedTypesByRegex()
    {
        return unmodifiableMap(mapTypesByName);
    }

    public Map<Specification, Collection<Class<?>>> scannedTypesBySpecification()
    {
        return unmodifiableMap(mapTypesBySpecification);
    }

    public Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass()
    {
        return unmodifiableMap(mapAnnotationTypes);
    }

    public Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex()
    {
        return unmodifiableMap(mapAnnotationTypesByName);
    }

    public Map<String, Collection<String>> getPropertiesFilesByPrefix()
    {
        return unmodifiableMap(propertyFilesByPrefix);
    }

    public Map<String, Collection<String>> getResourcesByRegex()
    {
        return unmodifiableMap(resourcesByRegex);
    }

    public Collection<Class<?>> getClassesToBind()
    {
        return unmodifiableCollection(classesToBind);
    }

    public List<UnitModule> getModules()
    {
        return unmodifiableList(childModules);
    }

    public List<UnitModule> getOverridingModules()
    {
        return unmodifiableList(childOverridingModules);
    }

    public Collection<String> getPropertyFiles()
    {
        return unmodifiableCollection(propertyFiles);
    }

    public Set<URL> getUrls()
    {
        return urls;
    }

    public void addUrls(Set<URL> urls) {
        this.urls.addAll(urls);
    }

    public void addClassesToBind(Collection<Class<?>> classesToBind) {
        this.classesToBind.addAll(classesToBind);
    }

    public void addChildModule(UnitModule module)
    {
        childModules.add(module);
    }

    public void addChildOverridingModule(UnitModule module)
    {
        childOverridingModules.add(module);
    }

    public void addSubtypes(Class<?> parentType, Collection<Class<?>> subtypes) {
        mapSubTypes.put(parentType, subtypes);
    }

    public void addAncestorTypes(Class<?> parentType, Collection<Class<?>> subtypes) {
        mapAncestorTypes.put(parentType, subtypes);
    }

    public void addSubTypesByName(String typeName, Collection<Class<?>> subtypes) {
        mapSubTypesByName.put(typeName, subtypes);
    }

    public void addTypesByName(String typeName, Collection<Class<?>> subtypes) {
        mapTypesByName.put(typeName, subtypes);
    }

    public void addTypesBySpecification(Specification<?> specification, Collection<Class<?>> subtypes) {
        mapTypesBySpecification.put(specification, subtypes);
    }

    public void addAnnotationTypes(Class<? extends Annotation> annotationClass, Collection<Class<?>> subtypes) {
        mapAnnotationTypes.put(annotationClass, subtypes);
    }

    public void addAnnotationTypesByName(String annotationName, Collection<Class<?>> subtypes) {
        mapAnnotationTypesByName.put(annotationName, subtypes);
    }

    public void addResourcesByRegex(String regex, Set<String> urls) {
        resourcesByRegex.put(regex, urls);
    }

    public void addPropertyFiles(Set<String> propertyFiles) {
        propertyFiles.addAll(propertyFiles);
    }

    public void addPropertyFilesByPrefix(String prefix, Set<String> propertyFiles) {
        propertyFilesByPrefix.put(prefix, propertyFiles);
    }
}
