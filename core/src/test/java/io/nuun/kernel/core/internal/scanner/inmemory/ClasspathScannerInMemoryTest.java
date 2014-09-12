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
package io.nuun.kernel.core.internal.scanner.inmemory;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import io.nuun.kernel.api.inmemory.InMemoryClassEntry;
import io.nuun.kernel.api.inmemory.SimpleInMemoryClasspath;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner.Callback;
import io.nuun.kernel.core.internal.scanner.ClasspathScanner.CallbackResources;



import io.nuun.kernel.core.internal.scanner.sample.Bean1;
import io.nuun.kernel.core.internal.scanner.sample.Bean3;
import io.nuun.kernel.core.internal.scanner.sample.ScanMarkerSample;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class ClasspathScannerInMemoryTest {

	private ClasspathScannerInMemory underTest;
	private SimpleInMemoryClasspath classpath;
    
	TestCallback cb;
    TestCallbackResources cbr;
    
	
    static class TestCallback implements Callback
    {
        public Collection<Class<?>> scanResult;

        @Override
        public void callback(Collection<Class<?>> scanResult)
        {
            this.scanResult = scanResult;
            
        }
    }
    static class TestCallbackResources implements CallbackResources
    {
        public Collection<String> scanResult;
        
        @Override
        public void callback(Collection<String> scanResult)
        {
            this.scanResult = scanResult;
            
        }
    }
	
	
	@Before
	public void init ()
	{
		cb = new TestCallback();
		cbr = new TestCallbackResources();
		
		classpath = new SimpleInMemoryClasspath();
		underTest = new ClasspathScannerInMemory(classpath);
	}
    @Test
    public void classpathscanner_should_retrieve_type_with_annotation ()
    {
        classpath.reset();
        classpath
         .add(new InMemoryClassEntry( Bean1.class ) )
         .add(new InMemoryClassEntry( Bean3.class ) )
         ;
    	//
    	underTest.scanClasspathForAnnotation(ScanMarkerSample.class , cb);
        underTest.doClasspathScan();
        Collection<Class<?>> scanClasspathForAnnotation = cb.scanResult;
         
         assertThat(scanClasspathForAnnotation).isNotNull();
         assertThat(scanClasspathForAnnotation).hasSize(2);
         assertThat(scanClasspathForAnnotation).containsOnly(Bean1.class , Bean3.class);
    }
	
	

}
