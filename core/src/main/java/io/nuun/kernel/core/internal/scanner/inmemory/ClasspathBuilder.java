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
package io.nuun.kernel.core.internal.scanner.inmemory;

import io.nuun.kernel.api.inmemory.ClasspathAbstractContainer;
import io.nuun.kernel.api.inmemory.ClasspathClass;
import io.nuun.kernel.api.inmemory.ClasspathDirectory;
import io.nuun.kernel.api.inmemory.ClasspathJar;
import io.nuun.kernel.api.inmemory.ClasspathResource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public abstract class ClasspathBuilder
{

    private InMemoryClasspath globalClasspath = InMemoryClasspath.INSTANCE;
    private final Map<String, ClasspathAbstractContainer<?>> entries;
    protected ClasspathAbstractContainer<?>                  currentContainer = null;

    public ClasspathBuilder()
    {
        entries = new HashMap<>();
    }

    protected void addJar(String name)
    {
        if (!entries.containsKey(name))
        {
            currentContainer = ClasspathJar.create(name);
            entries.put(name, currentContainer);
            globalClasspath.add(currentContainer);
        }
        else
        {
            currentContainer = entries.get(name);
        }
    }

    protected void addDirectory(String name)
    {
        if (!entries.containsKey(name))
        {
            currentContainer = ClasspathDirectory.create(name);
            entries.put(name, currentContainer);
            globalClasspath.add(currentContainer);
        }
        else
        {
            currentContainer = entries.get(name);
        }
    }

    protected void addResource(String base, String name)
    {
        if (currentContainer == null)
        {
            throw new IllegalStateException("currentContainer can not be null. please use directory() or jar()");
        }

        currentContainer.add(ClasspathResource.res(base, name));
    }

    protected void addClass(Class<?> candidate)
    {
        if (currentContainer == null)
        {
            throw new IllegalStateException("currentContainer can not be null. please use directory() or jar()");
        }

        currentContainer.add(new ClasspathClass(candidate));
    }
    
    /**
     * 
     * 
     */
    public abstract void configure();


}
