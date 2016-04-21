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
package io.nuun.kernel.core.internal.scanner;

import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.inmemory.Classpath;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathScannerDisk;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathStrategy;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathScannerInMemory;
import io.nuun.kernel.core.internal.scanner.inmemory.InMemoryMultiThreadClasspath;

import java.net.URL;
import java.util.List;
import java.util.Set;


public class ClasspathScannerFactory
{
    private ClasspathScanMode classpathScanMode;

    public ClasspathScannerFactory(ClasspathScanMode classpathScanMode)
    {
        this.classpathScanMode = classpathScanMode;
    }

    public ClasspathScanner create(ClasspathStrategy classpathStrategy, Set<URL> additionalClasspath, List<String> packageRoots) {
        String[] packageRootArray = new String[packageRoots.size()];
        packageRoots.toArray(packageRootArray);

        switch (classpathScanMode)
        {
            case NOMINAL:
                return createNominal(classpathStrategy, additionalClasspath, packageRootArray);
            case IN_MEMORY:
                return createInMemory(packageRootArray);
            default:
                throw new UnsupportedOperationException();
        }
    }

    private ClasspathScanner createNominal(ClasspathStrategy classpathStrategy, Set<URL> additionalClasspath, String... packageRoot)
    {
        return new ClasspathScannerDisk(classpathStrategy, additionalClasspath, packageRoot);
    }

    private ClasspathScanner createInMemory(String... packageRoot)
    {
        Classpath classpath = InMemoryMultiThreadClasspath.INSTANCE;
        return new ClasspathScannerInMemory(classpath, packageRoot);
    }

}
