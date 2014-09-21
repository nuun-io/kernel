/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
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

import io.nuun.kernel.api.inmemory.InMemoryClasspath;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathScannerDisk;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathStrategy;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathScannerInMemory;

import java.net.URL;
import java.util.Set;


public class ClasspathScannerFactory
{
    public ClasspathScanner create(ClasspathStrategy classpathStrategy, Set<URL> additionalClasspath , String... packageRoot)
    {
        ClasspathScannerDisk classpathScannerDisk = new ClasspathScannerDisk(classpathStrategy, packageRoot);
        classpathScannerDisk.setAdditionalClasspath(additionalClasspath);
        return classpathScannerDisk;
    }
    
    public ClasspathScanner createInMemory (InMemoryClasspath inMemoryClasspath  , String... packageRoot) {
    	ClasspathScannerInMemory classpathScannerInMemory = new ClasspathScannerInMemory( inMemoryClasspath , packageRoot);
    	
    	return classpathScannerInMemory;
    }

    @Deprecated
    public ClasspathScanner create(ClasspathStrategy classpathStrategy, String... packageRoot)
    {
        return new ClasspathScannerDisk(classpathStrategy, packageRoot);
    }

    @Deprecated
    public ClasspathScanner create(ClasspathStrategy classpathStrategy, boolean reachAbstractClass ,  String packageRoot,String... packageRoots )
    {
        return new ClasspathScannerDisk(classpathStrategy, reachAbstractClass , packageRoot ,packageRoots);
    }

}
