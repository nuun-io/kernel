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

import io.nuun.kernel.api.annotations.Ignore;
import io.nuun.kernel.core.KernelException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import io.nuun.kernel.core.internal.scanner.inmemory.InMemoryUrlType;
import org.reflections.vfs.Vfs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 *
 *
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public abstract class AbstractClasspathScanner implements ClasspathScanner
{

      static
      {
          Vfs.setDefaultURLTypes(Arrays.asList(
				  new InMemoryUrlType(),
                  Vfs.DefaultUrlTypes.jarFile,
                  Vfs.DefaultUrlTypes.jarUrl,
                  Vfs.DefaultUrlTypes.directory,
                  Vfs.DefaultUrlTypes.jarInputStream
          ));
      }


      private final boolean           reachAbstractClass;


	  public AbstractClasspathScanner(boolean reachAbstractClass) {
		this.reachAbstractClass = reachAbstractClass;
	  }


	  protected Collection<Class<?>> postTreatment(Collection<Class<?>> set)
	    {

	        // Sanity Check : throw a KernelException if one of the returned classes is null
	        for (Class<?> class1 : set)
	        {
	            if (null == class1)
	            {
	                throw new KernelException("Scanned classes results can not be null. Please check Integrity of the classes.");
	            }
	        }

	        Collection<Class<?>> filtered = Collections2.filter(set, new IgnorePredicate(reachAbstractClass));

	        return filtered;

	    }
	  
	  static class IgnorePredicate implements Predicate<Class<?>>
	    {

	        Logger                logger = LoggerFactory.getLogger(AbstractClasspathScanner.IgnorePredicate.class);

	        private final boolean reachAbstractClass;

	        public IgnorePredicate(boolean reachAbstractClass)
	        {
	            this.reachAbstractClass = reachAbstractClass;
	        }

	        @Override
	        public boolean apply(Class<?> clazz)
	        {

	            logger.trace("Checking {} for Ignore", clazz.getName());

	            boolean toKeep = true;

	            if (Modifier.isAbstract(clazz.getModifiers()) && !reachAbstractClass && !clazz.isInterface())
	            {
	                toKeep = false;
	            }

	            for (Annotation annotation : clazz.getAnnotations())
	            {
	                logger.trace("Checking annotation {} for Ignore", annotation.annotationType().getName());
	                if (annotation.annotationType().equals(Ignore.class) || annotation.annotationType().getName().endsWith("Ignore"))
	                {
	                    toKeep = false;
	                }
	                logger.trace("Result tokeep = {}.", toKeep);
	                if (!toKeep)
	                {
	                    break;
	                }
	            }
	            return toKeep;
	        }
	    }

}
