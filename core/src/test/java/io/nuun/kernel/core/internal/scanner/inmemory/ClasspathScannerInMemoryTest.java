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

import io.nuun.kernel.api.inmemory.ClasspathClass;
import io.nuun.kernel.api.inmemory.ClasspathDirectory;
import io.nuun.kernel.api.inmemory.ClasspathJar;
import io.nuun.kernel.core.internal.scanner.AbstractClasspathScanner;
import io.nuun.kernel.core.internal.scanner.ClasspathScannerTestBase;
import io.nuun.kernel.core.internal.scanner.sample.*;

import static io.nuun.kernel.api.inmemory.ClasspathResource.res;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class ClasspathScannerInMemoryTest extends ClasspathScannerTestBase
{

    private InMemoryClasspath classpath = InMemoryClasspath.INSTANCE;

    @SuppressWarnings("unchecked")
    @Override
    protected AbstractClasspathScanner createUnderTest()
    {
        classpath.reset();
        classpath
                .add(ClasspathDirectory.create("default")
                        .add(res("META-INF/properties", "tst-one.properties"))
                        .add(res("META-INF/properties", "tst-two.properties"))
                )
                .add(ClasspathJar.create("app.jar")
                        .add(new ClasspathClass(Bean1.class))
                        .add(new ClasspathClass(Bean2.class))
                        .add(new ClasspathClass(Bean3.class))
                        .add(new ClasspathClass(Bean6.class))
                )
                .add(ClasspathJar.create("modules.jar")
                        .add(new ClasspathClass(MyModule1.class))
                        .add(new ClasspathClass(MyModule4.class))
                );

        return new ClasspathScannerInMemory(classpath, 1);
    }
}
