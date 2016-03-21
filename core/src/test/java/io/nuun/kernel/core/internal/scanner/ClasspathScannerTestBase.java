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

import io.nuun.kernel.api.annotations.KernelModule;
import io.nuun.kernel.core.internal.scanner.sample.*;
import io.nuun.kernel.core.pluginsit.dummy7.Module7;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public abstract class ClasspathScannerTestBase
{
	protected AbstractClasspathScanner underTest;
	    
	@Before
	public void init() {
		underTest = createUnderTest();
	}
	
	protected abstract AbstractClasspathScanner createUnderTest ();
	  
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
        assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class, Bean6.class);
    }

    @Test
    public void classpathscanner_should_retrieve_properties_tst ()
    {
        Collection<String> scanClasspathForAnnotation = underTest.scanResources("tst-.*\\.properties");

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(2);
        assertThat(scanClasspathForAnnotation).containsOnly("META-INF/properties/tst-one.properties", "META-INF/properties/tst-two.properties");
    }
    
    @Test
    public void classpathscanner_should_retrieve_subtype ()
    {
        Collection<Class<?>> scanClasspathSubType = underTest.scanTypesAnnotatedBy(KernelModule.class);

        assertThat(scanClasspathSubType).isNotNull();
        assertThat(scanClasspathSubType).hasSize(2);
        assertThat(scanClasspathSubType).containsOnly(MyModule1.class, MyModule4.class);
    }
    
    
    @Test
    public void classpathscanner_should_ignore_Ignore_classtype_based ()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanTypesAnnotatedBy(ScanMarkerSample2.class);

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class);
    }

    @Test
    public void classpathscanner_should_ignore_Ignore_classnamed_based ()
    {
        Collection<Class<?>> scanClasspathForAnnotation = underTest.scanTypesAnnotatedBy(".*MarkerSample2");

        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class);
    }
}
