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
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathScannerDisk;

import java.net.MalformedURLException;
import java.util.Iterator;

import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.vfs.Vfs;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class ClasspathScannerInMemory extends ClasspathScannerDisk
{
    private final Classpath classpath;

    public ClasspathScannerInMemory(Classpath classpath, String... packageRoot)
    {
        super(null, packageRoot);
        this.classpath = classpath;
        actualInitReflections();
    }

    // Its too soon to create reflections in the constructor
    private void actualInitReflections() {
        ConfigurationBuilder configurationBuilder = configurationBuilder()
                .setScanners(getScanners())
                .setMetadataAdapter(new MetadataAdapterInMemory());

        InMemoryFactory factory = new InMemoryFactory();
        for (ClasspathAbstractContainer<?> i : this.classpath.entries())
        {
            String name = i.name();
            try
            {
                configurationBuilder.addUrls(factory.createInMemoryResource(name));
            }
            catch (MalformedURLException e)
            {
                throw new KernelException("Malformed URL Exception", e);
            }
        }
        reflections = new Reflections(configurationBuilder);
    }

    @Override
    protected void initializeReflections() {
        // override the Reflections initialization in the super constructor.
    }
}
