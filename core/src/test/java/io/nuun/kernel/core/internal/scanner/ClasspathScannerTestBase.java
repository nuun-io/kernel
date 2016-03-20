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
