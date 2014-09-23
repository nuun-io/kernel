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

import io.nuun.kernel.api.inmemory.InMemoryClasspathAbstractContainer;
import io.nuun.kernel.api.inmemory.InMemoryClasspathClass;
import io.nuun.kernel.api.inmemory.InMemoryClasspathDirectory;
import io.nuun.kernel.api.inmemory.InMemoryClasspathJar;
import io.nuun.kernel.api.inmemory.InMemoryClasspathResource;
import io.nuun.kernel.core.internal.scanner.inmemory.builder.ClasspathBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author epo.jemba@kametic.com
 */
public abstract class InMemoryClasspathBuilder implements ClasspathBuilder
{

    private final Map<String, InMemoryClasspathAbstractContainer<?>> entries;
    InMemoryClasspathAbstractContainer<?> currentContainer = null;

    public InMemoryClasspathBuilder()
    {
        entries = new HashMap<String, InMemoryClasspathAbstractContainer<?>>();
    }

//    @Override
//    public InMemoryClasspathAbstractContainer<?> entry(String container)
//    {
//        return null;
//    }
//
//    @Override
//    public Collection<InMemoryClasspathAbstractContainer<?>> entries()
//    {
//        return null;
//    }
    
    protected void jar(String name)
    {
        if (! entries.containsKey(name))
        {
            currentContainer = InMemoryClasspathJar.create(name);
            entries.put(name, currentContainer);
        }
        else
        {
            currentContainer = entries.get(name);
        }
    }
    
    protected void directory(String name)
    {
        if (! entries.containsKey(name))
        {
            currentContainer = InMemoryClasspathDirectory.create(name);
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
        
        currentContainer.add(InMemoryClasspathResource.res(base, name));
    }
    
    protected void class_ (Class<?> candidate)
    {
        if (currentContainer == null)
        {
            throw new IllegalStateException("currentContainer can not be null. please use resource");
        }
        
        currentContainer.add ( new InMemoryClasspathClass(candidate) );
    }
    
    
    /**
     * 
     * 
     */
    @Override
    public abstract void configure();


}
