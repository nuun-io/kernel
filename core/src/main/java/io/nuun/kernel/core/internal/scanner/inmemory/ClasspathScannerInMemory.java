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

import io.nuun.kernel.api.inmemory.Classpath;
import io.nuun.kernel.api.inmemory.ClasspathAbstractContainer;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathScannerDisk;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class ClasspathScannerInMemory extends ClasspathScannerDisk
{
    private final Classpath classpath;
    private final Set<URL> urls = new HashSet<>();

    public ClasspathScannerInMemory(Classpath classpath, int coreCount, String... packageRoot)
    {
        super(null, null, coreCount, packageRoot);
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
        urls.addAll(configurationBuilder.getUrls());
        reflections = new Reflections(configurationBuilder);
    }

    @Override
    public Set<URL> getUrls()
    {
        return urls;
    }

    @Override
    protected void initializeReflections() {
        // override the Reflections initialization in the super constructor.
    }
}
