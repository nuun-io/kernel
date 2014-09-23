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
package io.nuun.kernel.core.internal.scanner.inmemory;

import io.nuun.kernel.api.inmemory.Classpath;
import io.nuun.kernel.api.inmemory.ClasspathAbstractContainer;
import io.nuun.kernel.api.inmemory.ClasspathClass;
import io.nuun.kernel.api.inmemory.ClasspathDirectory;
import io.nuun.kernel.api.inmemory.ClasspathJar;
import io.nuun.kernel.api.inmemory.ClasspathResource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author epo.jemba@kametic.com
 */
public abstract class ClasspathConfiguration implements Classpath
{

    private final Map<String, ClasspathAbstractContainer<?>> entries;
    protected ClasspathAbstractContainer<?>                  currentContainer = null;

    public ClasspathConfiguration()
    {
        entries = new HashMap<String, ClasspathAbstractContainer<?>>();
    }

    protected void jar(String name)
    {
        if (!entries.containsKey(name))
        {
            currentContainer = ClasspathJar.create(name);
            entries.put(name, currentContainer);
        }
        else
        {
            currentContainer = entries.get(name);
        }
    }

    protected void directory(String name)
    {
        if (!entries.containsKey(name))
        {
            currentContainer = ClasspathDirectory.create(name);
            entries.put(name, currentContainer);
        }
        else
        {
            currentContainer = entries.get(name);
        }
    }

    protected void resource(String base, String name)
    {
        if (currentContainer == null)
        {
            throw new IllegalStateException("currentContainer can not be null. please use resource");
        }

        currentContainer.add(ClasspathResource.res(base, name));
    }

    protected void class_(Class<?> candidate)
    {
        if (currentContainer == null)
        {
            throw new IllegalStateException("currentContainer can not be null. please use resource");
        }

        currentContainer.add(new ClasspathClass(candidate));
    }

    /**
     * 
     * 
     */
    public abstract void configure();
    
    @Override
    public Collection<ClasspathAbstractContainer<?>> entries() {
        
        return entries.values();
    }
    
    @Override
    public ClasspathAbstractContainer<?> entry(String container)
    {
        return entries.get(container);
    }

}
