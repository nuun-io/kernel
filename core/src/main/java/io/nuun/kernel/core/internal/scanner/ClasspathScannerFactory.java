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
        ClasspathScannerDisk classpathScannerDisk = new ClasspathScannerDisk(classpathStrategy, packageRoot);
        classpathScannerDisk.setAdditionalClasspath(additionalClasspath);
        return classpathScannerDisk;
    }

    private ClasspathScanner createInMemory(String... packageRoot)
    {
        Classpath classpath = InMemoryMultiThreadClasspath.INSTANCE;
        return new ClasspathScannerInMemory(classpath, packageRoot);
    }

}
