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

import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.ConfigurationBuilder;

import io.nuun.kernel.core.internal.scanner.reflections.ClasspathScannerReflections;
import io.nuun.kernel.core.internal.scanner.reflections.ClasspathStrategy;


/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class ClasspathScannerInMemory extends ClasspathScannerReflections {
	
	
	private final Classpath classpath;

	public ClasspathScannerInMemory(ClasspathStrategy classpathStrategy, String[] packageRoots_ , Classpath classpath ) {
		super(classpathStrategy, packageRoots_);
		this.classpath = classpath;
	}
	
	@Override
	public void doClasspathScan() {
		 Scanner[] scanners = getScanners();
	        
	        ConfigurationBuilder configurationBuilder = configurationBuilder().setScanners ( scanners ) ;
	        
	        Reflections reflections = new Reflections(configurationBuilder);
	        
	        for(  ScannerCommand command : commands)
	        {
	            command.execute(reflections);
	        }
	}


/*
	private final List<ScannerCommand> commands;

	interface ScannerCommand {
		void execute(Classpath classpath);
	}

	public ClasspathScannerInMemory() {
		super(true);// we reach abstract class as well

		commands = new ArrayList<ClasspathScannerInMemory.ScannerCommand>();
	}

	private void queue(ScannerCommand command) {
		commands.add(command);
	}

	@Override
	public void scanClasspathForAnnotation(final Class<? extends Annotation> annotationType, final Callback callback) {
		queue(

		new ScannerCommand() {

			@Override
			public void execute(Classpath classpath) {
		

			}
		}

		);
	}

	@Override
	public void scanClasspathForAnnotationRegex(final String annotationTypeRegex, Callback callback) {
		queue(new ScannerCommand() {

			@Override
			public void execute(Classpath classpath) {

                  
//                  Multimap<String, String> multimap = store.get(TypeAnnotationsScanner.class);
//
//                  List<String> key = new ArrayList<String>();
//                  for (String loopKey : multimap.keySet())
//                  {
//                      if (loopKey.matches(annotationTypeRegex))
//                      {
//                          key.add(loopKey);
//                      }
//                  }
//
//                  Collection<Class<?>> typesAnnotatedWith = new HashSet<Class<?>>();
//
//                  for (String k : key)
//                  {
//                      Collection<String> collectionOfString = multimap.get(k);
//                      typesAnnotatedWith.addAll(toClasses(collectionOfString));
//                  }
//                  callback .callback(postTreatment(typesAnnotatedWith));
			}
		});

	}

	
	@Override
	public void scanClasspathForMetaAnnotation(final Class<? extends Annotation> annotationType, final Callback callback) {
		queue(new ScannerCommand() {
			
			@Override
			public void execute(Classpath classpath) {
				Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();

				for (ClasspathEntry entry : classpath.entries()) {
					if (entry instanceof ClassEntry) {
						Class<?> klass = ClassEntry.class.cast(entry).entryClass();
						if (annotationType != null && klass != null && AssertUtils.hasAnnotationDeep(klass, annotationType) && !klass.isAnnotation()) {
							typesAnnotatedWith.add(klass);
						}
					}
				}
				callback.callback(postTreatment(typesAnnotatedWith));
			}
		});

	}
	
	
	@Override
	public void scanClasspathForMetaAnnotationRegex(final String metaAnnotationRegex, final Callback callback) {
		queue(new ScannerCommand() {

			@Override
			public void execute(Classpath classpath) {
	                
	                Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();
	                
	                for (ClasspathEntry entry : classpath.entries()) {
	                {
	                	if (entry instanceof ClassEntry) {
	                		Class<?> klass = ClassEntry.class.cast(entry).entryClass();
	                		if ( metaAnnotationRegex != null && klass != null&&  AssertUtils.hasAnnotationDeepRegex(klass, metaAnnotationRegex) && ! klass.isAnnotation() )
	                		{
	                			typesAnnotatedWith.add(klass);
	                		}
	                		
	                	}
	                }
	                callback.callback(postTreatment(typesAnnotatedWith));
			}
		}});
	}

	@Override
	public void scanClasspathForSubTypeClass(Class<?> subType, Callback callback) {
		queue(new ScannerCommand() {
			
			@Override
			public void execute(Classpath classpath) {
				
			}
		});

	}

	@Override
	public void scanClasspathForTypeRegex(String typeRegex, Callback callback) {
		queue(new ScannerCommand() {
			
			@Override
			public void execute(Classpath classpath) {
				
			}
		});

	}

	@Override
	public void scanClasspathForSubTypeRegex(String typeRegex, Callback callback) {
		queue(new ScannerCommand() {
			
			@Override
			public void execute(Classpath classpath) {
				
			}
		});

	}

	@Override
	public void scanClasspathForResource(String pattern, CallbackResources callback) {
		queue(new ScannerCommand() {
			
			@Override
			public void execute(Classpath classpath) {
				
			}
		});

	}

	@Override
	public void scanClasspathForSpecification(Specification<Class<?>> specification, Callback callback) {
		queue(new ScannerCommand() {
			
			@Override
			public void execute(Classpath classpath) {
				
			}
		});

	}



	@Override
	public void doClasspathScan() {

	}
*/
}
