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

import static io.nuun.kernel.api.inmemory.ClasspathResource.res;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;

import io.nuun.kernel.api.annotations.KernelModule;
import io.nuun.kernel.api.inmemory.ClasspathClass;
import io.nuun.kernel.api.inmemory.ClasspathDirectory;
import io.nuun.kernel.api.inmemory.ClasspathJar;
import io.nuun.kernel.core.internal.scanner.AbstractClasspathScanner;
import io.nuun.kernel.core.internal.scanner.ClasspathScannerTestBase;
import io.nuun.kernel.core.internal.scanner.sample.Bean1;
import io.nuun.kernel.core.internal.scanner.sample.Bean2;
import io.nuun.kernel.core.internal.scanner.sample.Bean3;
import io.nuun.kernel.core.internal.scanner.sample.Bean6;
import io.nuun.kernel.core.internal.scanner.sample.MyModule1;
import io.nuun.kernel.core.internal.scanner.sample.MyModule4;
import io.nuun.kernel.core.internal.scanner.sample.ScanMarkerSample;
import io.nuun.kernel.core.internal.scanner.sample.ScanMarkerSample2;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class ClasspathScannerInMemoryTest extends ClasspathScannerTestBase
{

    private InMemoryMultiThreadClasspath classpath = InMemoryMultiThreadClasspath.INSTANCE;

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

        return new ClasspathScannerInMemory(classpath);
    }
    

    @Test
    public void classpathscanner_should_retrieve_type_with_annotation()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanTypesAnnotatedBy(ScanMarkerSample.class);

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(2);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class, Bean3.class);
    }

    @Test
    public void classpathscanner_should_retrieve_type_with_annotation_name()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanTypesAnnotatedBy(".*ScanMarkerSample");

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(3);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class, Bean3.class, Bean6.class);
    }

    @Test
    public void classpathscanner_should_retrieve_properties_tst()
    {
        Collection<String> scanClasspathForAnnotation = underTest.scanResources("tst-.*\\.properties");

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(2);
        assertThat(scanClasspathForAnnotation).containsOnly("META-INF/properties/tst-one.properties", "META-INF/properties/tst-two.properties");
    }

    @Test
    public void classpathscanner_should_retrieve_subtype()
    {
        Collection<Class<?>> scanClasspathSubType = underTest.scanTypesAnnotatedBy(KernelModule.class);

        assertThat(scanClasspathSubType).isNotNull();
        assertThat(scanClasspathSubType).hasSize(2);
        assertThat(scanClasspathSubType).containsOnly(MyModule1.class, MyModule4.class);
    }


    @Test
    public void classpathscanner_should_ignore_Ignore_classtype_based()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanTypesAnnotatedBy(ScanMarkerSample2.class);

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class);
    }

    @Test
    public void classpathscanner_should_ignore_Ignore_classnamed_based()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanTypesAnnotatedBy(".*MarkerSample2");

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class);
    }
    
    
}
