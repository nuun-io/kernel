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

import static io.nuun.kernel.api.inmemory.InMemoryClasspathResource.res;
import io.nuun.kernel.api.inmemory.InMemoryClasspathClass;
import io.nuun.kernel.api.inmemory.InMemoryClasspathDirectory;
import io.nuun.kernel.api.inmemory.InMemoryClasspathJar;
import io.nuun.kernel.api.inmemory.SimpleInMemoryClasspath;
import io.nuun.kernel.core.internal.scanner.AbstractClasspathScanner;
import io.nuun.kernel.core.internal.scanner.ClasspathScannerTestBase;
import io.nuun.kernel.core.internal.scanner.sample.Bean1;
import io.nuun.kernel.core.internal.scanner.sample.Bean2;
import io.nuun.kernel.core.internal.scanner.sample.Bean3;
import io.nuun.kernel.core.internal.scanner.sample.Bean6;
import io.nuun.kernel.core.internal.scanner.sample.MyModule1;
import io.nuun.kernel.core.internal.scanner.sample.MyModule4;
import io.nuun.kernel.core.pluginsit.dummy7.Module7;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class ClasspathScannerInMemoryTest  extends ClasspathScannerTestBase
{

	private SimpleInMemoryClasspath classpath = SimpleInMemoryClasspath.INSTANCE;
	
	@SuppressWarnings("unchecked")
	@Override
	protected AbstractClasspathScanner createUnderTest() {
		
        classpath.reset();
        classpath
         .add (
        		 InMemoryClasspathDirectory.create ("default")
        		 
	                 .add(res("META-INF/properties" , "tst-one.properties" ) )
	                 .add(res("META-INF/properties" , "tst-two.properties") )
        		 )
         .add (
        		 InMemoryClasspathJar.create ("app.jar")
        		 
	        		 .add(new InMemoryClasspathClass(Bean1.class ))
	        		 .add(new InMemoryClasspathClass(Bean2.class ))
	        		 .add(new InMemoryClasspathClass(Bean3.class ))
	        		 .add(new InMemoryClasspathClass(Bean6.class ))
        		 
        		 )
         .add (
        		 InMemoryClasspathJar.create ("modules.jar")
        		 
	        		 .add(new InMemoryClasspathClass(MyModule1.class ))
	        		 .add(new InMemoryClasspathClass(MyModule4.class ))
	        		 .add(new InMemoryClasspathClass(Module7.class ))
        		 
        		 )
         ;
		
        
		
		return new ClasspathScannerInMemory(classpath);
		
	}
}
