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
package io.nuun.kernel.core.internal.scanner.disk;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.nuun.kernel.core.internal.scanner.AbstractClasspathScanner;
import io.nuun.kernel.core.internal.utils.AssertUtils;
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

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static io.nuun.kernel.core.internal.utils.NuunReflectionUtils.forNameSilent;
import static io.nuun.kernel.core.internal.utils.NuunReflectionUtils.forNames;
import static org.reflections.util.FilterBuilder.prefix;

public class ClasspathScannerDisk extends AbstractClasspathScanner
{
    private final List<String> packageRoots;
    private final ClasspathStrategy classpathStrategy;
    private final Set<URL> additionalClasspath;
    private final int coreCount;
    private Set<URL> urls;
    protected Reflections reflections;

    public ClasspathScannerDisk(ClasspathStrategy classpathStrategy, Set<URL> additionalClasspath, int coreCount, String... packageRoots)
    {
        this(classpathStrategy, true, additionalClasspath, coreCount, packageRoots);
    }

    public ClasspathScannerDisk(ClasspathStrategy classpathStrategy, boolean reachAbstractClass, Set<URL> additionalClasspath, int coreCount, String... packageRoots)
    {
        super(reachAbstractClass);
        this.packageRoots = new LinkedList<>();
        Collections.addAll(this.packageRoots, packageRoots);
        this.classpathStrategy = classpathStrategy;
        this.additionalClasspath = additionalClasspath;
        this.coreCount = coreCount;
        initializeReflections();
    }

    protected void initializeReflections()
    {
        ConfigurationBuilder configurationBuilder = configurationBuilder()
                .addUrls(findClasspathUrls()).setScanners(getScanners());

        reflections = new Reflections(configurationBuilder);
    }

    protected ConfigurationBuilder configurationBuilder()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        if (coreCount > 1)
        {
            cb.setExecutorService(Executors.newFixedThreadPool(coreCount));
        }

        FilterBuilder fb = new FilterBuilder();

        for (String packageRoot : packageRoots)
        {
            fb.include(prefix(packageRoot));
        }

        cb.filterInputsBy(fb);

        return cb;
    }

    private Set<URL> findClasspathUrls()
    {
        if (urls == null)
        {
            urls = new HashSet<>();

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

    public Set<URL> getUrls()
    {
        return Collections.unmodifiableSet(urls);
    }

    @Override
    public Collection<Class<?>> scanTypes(final Predicate<Class<?>> predicate)
    {
        Store store = reflections.getStore();
        Multimap<String, String> multimap = store.get(TypeElementsScanner.class.getSimpleName());
        Collection<String> types = multimap.keySet();

        // Filter via predicate
        Collection<Class<?>> filteredTypes = new HashSet<>();
        for (Class<?> candidate : forNames(types))
        {
            if (predicate.test(candidate))
            {
                filteredTypes.add(candidate);
            }
        }
        return postTreatment(filteredTypes);
    }

    @Override
    public Collection<Class<?>> scanTypesAnnotatedBy(final Class<? extends Annotation> annotationType)
    {
        return postTreatment(reflections.getTypesAnnotatedWith(annotationType));
    }

    @Override
    public Collection<Class<?>> scanTypes(final String typeRegex)
    {
        Store store = reflections.getStore();
        Multimap<String, String> multimap = store.get(TypeElementsScanner.class.getSimpleName());
        Collection<String> collectionOfString = new HashSet<>();
        for (String loopKey : multimap.keySet())
        {
            if (loopKey.matches(typeRegex))
            {
                collectionOfString.add(loopKey);
            }
        }

        return postTreatment(forNames(collectionOfString));
    }

    @Override
    public Collection<Class<?>> scanTypesAnnotatedBy(final String annotationTypeRegex)
    {
        Store store = reflections.getStore();

        Multimap<String, String> multimap = store.get(TypeAnnotationsScanner.class.getSimpleName());

        List<String> key = new ArrayList<>();
        for (String loopKey : multimap.keySet())
        {
            if (loopKey.matches(annotationTypeRegex))
            {
                key.add(loopKey);
            }
        }

        Collection<Class<?>> typesAnnotatedWith = new HashSet<>();

        for (String k : key)
        {
            Collection<String> collectionOfString = multimap.get(k);
            typesAnnotatedWith.addAll(forNames(collectionOfString));
        }
        return postTreatment(typesAnnotatedWith);
    }

    @Override
    public Collection<Class<?>> scanTypesMetaAnnotated(final Class<? extends Annotation> annotationType)
    {
        Multimap<String, String> multimap = reflections.getStore().get(TypeElementsScanner.class.getSimpleName());
        Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();
        for (String className : multimap.keys())
        {
            Class<?> aClass = forNameSilent(className);
            if (annotationType != null && aClass != null && AssertUtils.hasAnnotationDeep(aClass, annotationType) && !aClass.isAnnotation())
            {
                typesAnnotatedWith.add(aClass);
            }
        }
        return postTreatment(typesAnnotatedWith);
    }

    @Override
    public Collection<Class<?>> scanTypesMetaAnnotated(final String metaAnnotationRegex)
    {
        Multimap<String, String> multimap = reflections.getStore().get(TypeElementsScanner.class.getSimpleName());
        Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();
        for (String className : multimap.keys())
        {
            Class<?> aClass = forNameSilent(className);
            if (metaAnnotationRegex != null && aClass != null && AssertUtils.hasAnnotationDeepRegex(aClass, metaAnnotationRegex) && !aClass.isAnnotation())
            {
                typesAnnotatedWith.add(aClass);
            }
        }
        return postTreatment(typesAnnotatedWith);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Class<?>> scanSubTypesOf(final Class<?> subType)
    {
        return postTreatment((Collection) reflections.getSubTypesOf(subType));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Class<?>> scanSubTypesOf(final String subTypeName)
    {
        Store store = reflections.getStore();
        Multimap<String, String> multimap = store.get(TypeElementsScanner.class.getSimpleName());

        Collection<String> types = new HashSet<>();

        for (String loopKey : multimap.keySet())
        {
            if (loopKey.matches(subTypeName))
            {
                types.add(loopKey);
            }
        }

        // Then find subclasses of types
        Collection<Class<?>> finalClasses = new HashSet<>();
        for (Class<?> subType : forNames(types))
        {
            finalClasses.addAll(postTreatment((Collection) reflections.getSubTypesOf(subType)));
        }

        // removed ignored already done
        return finalClasses;
    }

    @Override
    public Set<String> scanResources(final String pattern)
    {
        return reflections.getResources(Pattern.compile(pattern));
    }

    protected Scanner[] getScanners()
    {
        return new Scanner[]{
                buildTypeElementsScanner(),
                new SubTypesScanner(),
                new TypeAnnotationsScanner(),
                new ResourcesScanner()
        };
    }

    private TypeElementsScanner buildTypeElementsScanner()
    {
        return new TypeElementsScanner().includeFields(false).includeMethods(false).includeAnnotations(false);
    }
}
