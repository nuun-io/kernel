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
package io.nuun.kernel.core.internal.scanner.reflections;

import io.nuun.kernel.core.internal.scanner.AbstractClasspathScanner;
import io.nuun.kernel.core.internal.scanner.ClasspathScannerTestBase;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathScannerDisk;
import io.nuun.kernel.core.internal.scanner.disk.ClasspathStrategy;
import io.nuun.kernel.core.internal.scanner.sample.MyModule2;


public class ClasspathScannerReflectionsTest extends ClasspathScannerTestBase
{

    @Override
    protected AbstractClasspathScanner createUnderTest()
    {
    	return new ClasspathScannerDisk(new ClasspathStrategy(), true, null, 1, "META-INF.properties", MyModule2.class.getPackage().getName());
    }

}
