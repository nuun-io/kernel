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

import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.api.annotations.KernelModule;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner.Callback;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner.CallbackResources;
import io.nuun.kernel.core.internal.scanner.sample.Bean1;
import io.nuun.kernel.core.internal.scanner.sample.Bean2;
import io.nuun.kernel.core.internal.scanner.sample.Bean3;
import io.nuun.kernel.core.internal.scanner.sample.Bean6;
import io.nuun.kernel.core.internal.scanner.sample.MyModule1;
import io.nuun.kernel.core.internal.scanner.sample.MyModule4;
import io.nuun.kernel.core.internal.scanner.sample.ScanMarkerSample;
import io.nuun.kernel.core.internal.scanner.sample.ScanMarkerSample2;
import io.nuun.kernel.core.pluginsit.dummy7.Module7;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public abstract class ClasspathScannerTestBase
{
	protected AbstractClasspathScanner underTest;

	protected TestCallback cb;
	protected TestCallbackResources cbr;

	protected static class TestCallback implements Callback
	{
		public Collection<Class<?>> scanResult;

		@Override
		public void callback(Collection<Class<?>> scanResult)
		{
			this.scanResult = scanResult;

		}
	}

	protected static class TestCallbackResources implements CallbackResources
	{
		public Collection<String> scanResult;

		@Override
		public void callback(Collection<String> scanResult)
		{
			this.scanResult = scanResult;

		}
	}
	    
	@Before
	public void init() {
		
		
		underTest = createUnderTest();
		

		cb = new TestCallback();
		cbr = new TestCallbackResources();
	}
	
	protected abstract AbstractClasspathScanner createUnderTest ();
	
	
	  
    @Test
    public void classpathscanner_should_retrieve_type_with_annotation ()
    {
        underTest.scanClasspathForAnnotation(ScanMarkerSample.class , cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation = cb.scanResult;
         
         assertThat(scanClasspathForAnnotation).isNotNull();
         assertThat(scanClasspathForAnnotation).hasSize(2);
         assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class);
    }
    
    @Test
    public void classpathscanner_should_retrieve_type_with_annotation_name ()
    {
        underTest.scanClasspathForAnnotationRegex(".*ScanMarkerSample",cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation =cb.scanResult ;
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(3);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class, Bean6.class);
    }

    @Test
    public void classpathscanner_should_retrieve_properties_tst ()
    {
//        underTest.setAdditionalClasspath( ClasspathHelper.forPackage("") );
        underTest.scanClasspathForResource("tst-.*\\.properties" , cbr);
        underTest.doClasspathScan();
        Collection<String> scanClasspathForAnnotation = cbr.scanResult;
         
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(2);
	    assertThat(scanClasspathForAnnotation).containsOnly("META-INF/properties/tst-one.properties" , "META-INF/properties/tst-two.properties");
    }
    
    @Test
    public void classpathscanner_should_retrieve_subtype ()
    {
        underTest.scanClasspathForAnnotation( KernelModule.class ,cb);
        underTest.doClasspathScan();
        Collection<Class<? >> scanClasspathSubType = cb.scanResult;
        
        assertThat(scanClasspathSubType).isNotNull();
        assertThat(scanClasspathSubType).hasSize(3);
        assertThat(scanClasspathSubType).containsOnly( MyModule1.class , Module7.class , MyModule4.class );
    }
    
    
    @Test
    public void classpathscanner_should_ignore_Ignore_classtype_based ()
    {
        underTest.scanClasspathForAnnotation(ScanMarkerSample2.class,cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation = cb.scanResult;
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class );
    }

    @Test
    public void classpathscanner_should_ignore_Ignore_classnamed_based ()
    {
        underTest.scanClasspathForAnnotationRegex(".*MarkerSample2",cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation = cb.scanResult;
        
        assertThat(scanClasspathForAnnotation).isNotNull();
        assertThat(scanClasspathForAnnotation).hasSize(1);
        assertThat(scanClasspathForAnnotation).containsOnly(Bean2.class );
    }
	
}
