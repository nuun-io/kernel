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

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.vfs.Vfs;

import io.nuun.kernel.api.inmemory.InMemoryClassEntry;
import io.nuun.kernel.api.inmemory.InMemoryClasspath;
import io.nuun.kernel.api.inmemory.InMemoryClasspathEntry;
import io.nuun.kernel.api.inmemory.InMemoryResourceEntry;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.scanner.reflections.ClasspathScannerReflections;

/**
 * @author epo.jemba@kametic.com
 */
public class ClasspathScannerInMemory extends ClasspathScannerReflections
{

    private final InMemoryClasspath inMemoryClasspath;
    private final static Map<String, List<? extends InMemoryFile<?>>> fs = new HashMap<String, List<? extends InMemoryFile<?>>>();

    static
    {
        Vfs.addDefaultURLTypes(new InMemoryUrlType(fs ));
    }
    
    public ClasspathScannerInMemory(InMemoryClasspath inMemoryClasspath, String... packageRoot)
    {
        super(null, packageRoot);
        this.inMemoryClasspath = inMemoryClasspath;
    }

    @Override
    public void doClasspathScan()
    {
        Scanner[] scanners = getScanners();

        ConfigurationBuilder configurationBuilder = configurationBuilder().setScanners(scanners);

        InMemoryFactory factory = new InMemoryFactory();

        for (InMemoryClasspathEntry i : inMemoryClasspath.entries())
        {
            if (i instanceof InMemoryClassEntry)
            {
                try
                {
                    configurationBuilder.addUrls(factory.createInMemoryClass(((InMemoryClassEntry) i).entryClass()));
                }
                catch (MalformedURLException e)
                {
                    throw new KernelException("Malformed exception", e);
                }
            }
            else if (i instanceof InMemoryResourceEntry)
            {
                try
                {
                    configurationBuilder.addUrls(factory.createInMemoryResource(i.name()));
                }
                catch (MalformedURLException e)
                {
                    throw new KernelException("Malformed exception", e);
                }
            }
        }

        Reflections reflections = new Reflections(configurationBuilder);

        executeScan(reflections, inMemoryClasspath);

        for (ScannerCommand command : commands)
        {
            command.execute(reflections);
        }
    }

    private void executeScan(Reflections reflections, InMemoryClasspath classpath)
    {

    }

    /*
     * private final List<ScannerCommand> commands; interface ScannerCommand { void execute(InMemoryClasspath
     * inMemoryClasspath); } public ClasspathScannerInMemory() { super(true);// we reach abstract class as
     * well commands = new ArrayList<ClasspathScannerInMemory.ScannerCommand>(); } private void
     * queue(ScannerCommand command) { commands.add(command); }
     * @Override public void scanClasspathForAnnotation(final Class<? extends Annotation> annotationType,
     * final Callback callback) { queue( new ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { } } ); }
     * @Override public void scanClasspathForAnnotationRegex(final String annotationTypeRegex, Callback
     * callback) { queue(new ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { // Multimap<String, String>
     * multimap = store.get(TypeAnnotationsScanner.class); // // List<String> key = new ArrayList<String>();
     * // for (String loopKey : multimap.keySet()) // { // if (loopKey.matches(annotationTypeRegex)) // { //
     * key.add(loopKey); // } // } // // Collection<Class<?>> typesAnnotatedWith = new HashSet<Class<?>>(); //
     * // for (String k : key) // { // Collection<String> collectionOfString = multimap.get(k); //
     * typesAnnotatedWith.addAll(toClasses(collectionOfString)); // } // callback
     * .callback(postTreatment(typesAnnotatedWith)); } }); }
     * @Override public void scanClasspathForMetaAnnotation(final Class<? extends Annotation> annotationType,
     * final Callback callback) { queue(new ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { Collection<Class<?>>
     * typesAnnotatedWith = Sets.newHashSet(); for (AbstractInMemoryClasspathEntry entry :
     * inMemoryClasspath.entries()) { if (entry instanceof InMemoryClasspathEntry) { Class<?> klass =
     * InMemoryClasspathEntry.class.cast(entry).entryClass(); if (annotationType != null && klass != null &&
     * AssertUtils.hasAnnotationDeep(klass, annotationType) && !klass.isAnnotation()) {
     * typesAnnotatedWith.add(klass); } } } callback.callback(postTreatment(typesAnnotatedWith)); } }); }
     * @Override public void scanClasspathForMetaAnnotationRegex(final String metaAnnotationRegex, final
     * Callback callback) { queue(new ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { Collection<Class<?>>
     * typesAnnotatedWith = Sets.newHashSet(); for (AbstractInMemoryClasspathEntry entry :
     * inMemoryClasspath.entries()) { { if (entry instanceof InMemoryClasspathEntry) { Class<?> klass =
     * InMemoryClasspathEntry.class.cast(entry).entryClass(); if ( metaAnnotationRegex != null && klass !=
     * null&& AssertUtils.hasAnnotationDeepRegex(klass, metaAnnotationRegex) && ! klass.isAnnotation() ) {
     * typesAnnotatedWith.add(klass); } } } callback.callback(postTreatment(typesAnnotatedWith)); } }}); }
     * @Override public void scanClasspathForSubTypeClass(Class<?> subType, Callback callback) { queue(new
     * ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { } }); }
     * @Override public void scanClasspathForTypeRegex(String typeRegex, Callback callback) { queue(new
     * ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { } }); }
     * @Override public void scanClasspathForSubTypeRegex(String typeRegex, Callback callback) { queue(new
     * ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { } }); }
     * @Override public void scanClasspathForResource(String pattern, CallbackResources callback) { queue(new
     * ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { } }); }
     * @Override public void scanClasspathForSpecification(Specification<Class<?>> specification, Callback
     * callback) { queue(new ScannerCommand() {
     * @Override public void execute(InMemoryClasspath inMemoryClasspath) { } }); }
     * @Override public void doClasspathScan() { }
     */
}
