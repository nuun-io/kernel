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
package io.nuun.kernel.core;

import io.nuun.kernel.api.ClasspathScanMode;
import io.nuun.kernel.api.inmemory.ClasspathJar;
import io.nuun.kernel.api.inmemory.SimpleClasspath;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class KernelSuite8Test {
	  private Kernel underTest;
	    
	  private SimpleClasspath classpath = SimpleClasspath.INSTANCE;
	  
	  
	  @Before
	  public void init ()
	  {
		  classpath.reset();
		  classpath.add(
				  ClasspathJar.create("test.jar")
				     .add( InMe )
				  
				  
				  );
	  }
	  
	  
	  
	    @Test
	    public void dependee_plugins_that_misses_should_be_source_of_error()
	    {
	        underTest = Kernel.createKernel()
	        		.withClasspathScanMode(ClasspathScanMode.IN_MEMORY, null)
	                .withoutSpiPluginsLoader() //
	                .withPlugins(
	                        new DummyPlugin5() //
	                ) //
	                .build(); //
	        underTest.init();
	        underTest.start();
	        
//	        String resa = underTest.getMainInjector().getInstance( Key.get(String.class, Names.named("dep7a")) );
//	        assertThat(resa).isNotNull();
//	        assertThat(resa).isEqualTo("dep7aOVER");
	        
	    }
	    
}
