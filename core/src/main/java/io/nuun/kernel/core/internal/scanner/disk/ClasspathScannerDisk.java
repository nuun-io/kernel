/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.core.internal.scanner.disk;

import static org.reflections.util.FilterBuilder.prefix;

import com.google.common.collect.Maps;
import io.nuun.kernel.api.assertions.AssertUtils;
import io.nuun.kernel.core.internal.scanner.AbstractClasspathScanner;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.kametic.specifications.Specification;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class ClasspathScannerDisk extends AbstractClasspathScanner
{

    Logger                          logger = LoggerFactory.getLogger(ClasspathScannerDisk.class);

    private final List<String>      packageRoots;
    private final ClasspathStrategy classpathStrategy;
    private Set<URL>                additionalClasspath;
    private Set<URL>                urls;

    protected final List<ScannerCommand> commands;

    public ClasspathScannerDisk(ClasspathStrategy classpathStrategy, String... packageRoots_)
    {
        this(classpathStrategy, true, null, packageRoots_);

    }

    public ClasspathScannerDisk(ClasspathStrategy classpathStrategy, boolean reachAbstractClass, String packageRoot, String... packageRoots_)
    {
        super(reachAbstractClass);
    	packageRoots = new LinkedList<String>();

        if (packageRoot != null)
        {
            packageRoots.add(packageRoot);
        }

        for (String packageRoot_ : packageRoots_)
        {
            packageRoots.add(packageRoot_);
        }
        
        this.classpathStrategy = classpathStrategy;
        commands = new ArrayList<ClasspathScannerDisk.ScannerCommand>();
    }

    protected interface ScannerCommand
    {
        void execute (Reflections reflections);
    }
    

    @Override
    public void scanClasspathForAnnotation(final Class<? extends Annotation> annotationType , final Callback callback)
    {
//        ConfigurationBuilder configurationBuilder = configurationBuilder();
//        Set<URL> computeUrls = computeUrls();
//        Reflections reflections = new Reflections(configurationBuilder.addUrls(computeUrls).setScanners(new TypeAnnotationsScanner()));

        ScannerCommand command = new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                Collection<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotationType);
                if (typesAnnotatedWith == null)
                {
                    typesAnnotatedWith = Collections.emptySet();
                }
                callback.callback(postTreatment(typesAnnotatedWith));
            }
        };
        
        queue(command);
                        
        
    }

    private void queue(ScannerCommand command)
    {
        commands.add(command);
    }

    @Override
    public void scanClasspathForMetaAnnotation(final Class<? extends Annotation> annotationType , final Callback callback)
    {
//        ConfigurationBuilder configurationBuilder = configurationBuilder();
//        Set<URL> computeUrls = computeUrls();
//        Reflections reflections = new Reflections(configurationBuilder.addUrls(computeUrls).setScanners(new TypesScanner()));
        
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                Multimap<String, String> multimap = reflections.getStore().get(TypeElementsScanner.class.getSimpleName());
                Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();
                for (String className : multimap.keys())
                {
                    Class<?> klass = toClass(className);
                    if (annotationType != null && klass != null && AssertUtils.hasAnnotationDeep(klass, annotationType) && !klass.isAnnotation())
                    {
                        typesAnnotatedWith.add(klass);
                    }
                }
                callback.callback(postTreatment(typesAnnotatedWith));
            }

        });
        
        
    }


    @Override
    public void scanClasspathForMetaAnnotationRegex(final String metaAnnotationRegex , final Callback callback)
    {
//        ConfigurationBuilder configurationBuilder = configurationBuilder();
//        Set<URL> computeUrls = computeUrls();
//        Reflections reflections = new Reflections(configurationBuilder.addUrls(computeUrls).setScanners(new TypesScanner()));
        
        
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                Multimap<String, String> multimap = reflections.getStore().get(TypeElementsScanner.class.getSimpleName());
                
                Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();
                
                for( String className : multimap.keys())
                {
                    Class<?> klass = toClass(className);
                    if ( metaAnnotationRegex != null && klass != null&&  AssertUtils.hasAnnotationDeepRegex(klass, metaAnnotationRegex) && ! klass.isAnnotation() )
//            if ( annotationType != null && klass != null &&  AssertUtils.hasAnnotationDeep(klass, annotationType) && ! klass.isAnnotation() )
                    {
                        typesAnnotatedWith.add(klass);
                    }
                }
                callback.callback(postTreatment(typesAnnotatedWith));
            }
        });
    }

    
    private Class<?> toClass(String candidate)
    {
        try
        {
            return  Class.forName(candidate);
        }
        catch (Throwable e)
        {
            logger.debug("String to Class : " + e.getMessage() );
            
        }
        return null;
    }
   

  

    @Override
    public void scanClasspathForAnnotationRegex(final String annotationTypeRegex, final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypeAnnotationsScanner()));

          queue(new ScannerCommand()
          {
              @Override
              public void execute(Reflections reflections)
              {
                  Store store = reflections.getStore();
                  
                  Multimap<String, String> multimap = store.get(TypeAnnotationsScanner.class.getSimpleName());

                  List<String> key = new ArrayList<String>();
                  for (String loopKey : multimap.keySet())
                  {
                      if (loopKey.matches(annotationTypeRegex))
                      {
                          key.add(loopKey);
                      }
                  }
                  
                  Collection<Class<?>> typesAnnotatedWith = new HashSet<Class<?>>();
                  
                  for (String k : key)
                  {
                      Collection<String> collectionOfString = multimap.get(k);
                      typesAnnotatedWith.addAll(toClasses(collectionOfString));
                  }
                  callback .callback(postTreatment(typesAnnotatedWith));
              }
              
          });
    }


    
    @Override
    public void scanClasspathForTypeRegex(final String typeName, final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                Store store = reflections.getStore();
                Multimap<String, String> multimap = store.get(TypeElementsScanner.class.getSimpleName());
                Collection<String> collectionOfString = new HashSet<String>();
                for (String loopKey : multimap.keySet())
                {
                    if (loopKey.matches(typeName))
                    {
                        collectionOfString.add(loopKey);
                    }
                }

                Collection<Class<?>> types = null;

                if (collectionOfString.size() > 0)
                {
                    types = toClasses(collectionOfString);
                }
                else
                {
                    types = Collections.emptySet();
                }
                callback.callback(postTreatment(types));

            }

        });


    }


    @Override
    public void scanClasspathForSpecification(final Specification<Class<?>> specification , final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));
          queue(new ScannerCommand()
          {
              @Override
              public void execute(Reflections reflections)
              {
                  Store store = reflections.getStore();
                  Multimap<String, String> multimap = store.get(TypeElementsScanner.class.getSimpleName());
                  Collection<String> collectionOfString = multimap.keySet();
                  
                  Collection<Class<?>> types = null;
                  Collection<Class<?>> filteredTypes = new HashSet<Class<?>>();
                  
                  // Convert String to classes
                  if (collectionOfString.size() > 0)
                  {
                      types = toClasses(collectionOfString);
                  }
                  else
                  {
                      types = Collections.emptySet();
                  }
                  
                  // Filter via specification
                  for (Class<?> candidate : types)
                  {
                      if (specification.isSatisfiedBy(candidate))
                      {
                          filteredTypes.add(candidate);
                      }
                  }
                  
                  callback.callback(postTreatment(filteredTypes));
                  
              }
              
          });


    }

    @SuppressWarnings({})
    @Override
    public void scanClasspathForSubTypeRegex(final String subTypeName , final Callback callback)
    {

//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));
        
        
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                // empty just add
            }

        });
        queue(new ScannerCommand()
        {
            @SuppressWarnings({
                    "rawtypes", "unchecked"
            })
            @Override
            public void execute(Reflections reflections)
            {
                Store store = reflections.getStore();
                Multimap<String, String> multimap = store.get(TypeElementsScanner.class.getSimpleName());

                Collection<String> collectionOfString = new HashSet<String>();

                for (String loopKey : multimap.keySet())
                {

                    if (loopKey.matches(subTypeName))
                    {
                        collectionOfString.add(loopKey);
                    }
                }

                Collection<Class<?>> types = null;

                if (collectionOfString.size() > 0)
                {
                    types = toClasses(collectionOfString);
                }
                else
                {
                    types = Collections.emptySet();
                }

                // Then find subclasses of types
                Collection<Class<?>> finalClasses = new HashSet<Class<?>>();
                for (Class<?> subType : types)
                {
                    // Collection<Class<?>> scanClasspathForSubTypeClass =
                    // scanClasspathForSubTypeClass(class1);
                    // ///////////////////////////////
                    Collection<?> typesAnnotatedWith = reflections.getSubTypesOf(subType);

                    if (typesAnnotatedWith == null)
                    {
                        typesAnnotatedWith = Collections.emptySet();
                    }
                    // ///////////////////////////////
                    finalClasses.addAll(postTreatment((Collection) typesAnnotatedWith));
                }

                // removed ignored already done
                callback.callback(finalClasses);
                // return (Collection) removeIgnore((Collection)types);

            }

        });

    }
    

    @Override
    public void scanClasspathForResource(final String pattern , final CallbackResources callback)
    {
//        Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(computeUrls()).setScanners(new ResourcesScanner()));
          queue(new ScannerCommand()
          {
              @Override
              public void execute(Reflections reflections)
              {
                  Set<String> resources = reflections.getResources(Pattern.compile(pattern));
                  callback.callback(resources);
                  
              }
              
          });
    }
//  queue(new ScannerCommand()
//  {
//      @Override
//      public void execute(Reflections reflections)
//      {
//
//      }
//
//      @Override
//      public Scanner scanner()
//      {
//          return
//      }
//
//  });

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public void scanClasspathForSubTypeClass(final Class<?> subType , final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new SubTypesScanner()));
      queue(new ScannerCommand()
      {
          @Override
          public void execute(Reflections reflections)
          {
              Collection<?> typesAnnotatedWith = reflections.getSubTypesOf(subType);
              
              if (typesAnnotatedWith == null)
              {
                  typesAnnotatedWith = Collections.emptySet();
              }
              
              callback.callback ( postTreatment((Collection) typesAnnotatedWith));
              
          }
          
      });

    }
    
    /**
     * Unique reflections object. Unique scan
     */
    @Override
    public void doClasspathScan()
    {
        ConfigurationBuilder configurationBuilder = configurationBuilder().addUrls(computeUrls()).setScanners ( getScanners() ) ;
        
        Reflections reflections = new Reflections(configurationBuilder);
        
        for(  ScannerCommand command : commands)
        {
            command.execute(reflections);
        }
    }

    protected Scanner[] getScanners()
    {
        return new Scanner[] {
                buildTypeElementsScanner(),
                new SubTypesScanner(),
                new TypeAnnotationsScanner(),
                new ResourcesScanner()
        };
    }

    public void setAdditionalClasspath(Set<URL> additionalClasspath)
    {
        this.additionalClasspath = additionalClasspath;
    }

    protected ConfigurationBuilder configurationBuilder()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        FilterBuilder fb = new FilterBuilder();

        for (String packageRoot : packageRoots)
        {
            fb.include(prefix(packageRoot));
        }
        

        cb.filterInputsBy(fb);

        return cb;
    }

    private Set<URL> computeUrls()
    {
        if (urls == null)
        {
            urls = new HashSet<URL>();

            switch (classpathStrategy.getStrategy())
            {
                case SYSTEM:
                    urls.addAll(ClasspathHelper.forJavaClassPath());
                    break;
                case CLASSLOADER:
                    urls.addAll(ClasspathHelper.forClassLoader());
                    break;
                case ALL:
                    urls.addAll(ClasspathHelper.forJavaClassPath());
                    urls.addAll(ClasspathHelper.forClassLoader());
                    break;
                case NONE:
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported classpath strategy " + classpathStrategy.toString());
            }

            if (classpathStrategy.isAdditional() && additionalClasspath != null)
            {
                urls.addAll(additionalClasspath);
            }
        }

        urls.addAll(ClasspathHelper.forManifest(urls));

        return urls;
    }

    private <T> Collection<Class<? extends T>> toClasses(Collection<String> names)
    {
        return ReflectionUtils.<T> forNames(names, new ClassLoader[]{ this.getClass().getClassLoader() });
    }

    private TypeElementsScanner buildTypeElementsScanner()
    {
        return new TypeElementsScanner().includeFields(false).includeMethods(false).includeAnnotations(false);
    }
}
