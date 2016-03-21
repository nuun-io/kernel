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

import io.nuun.kernel.core.internal.utils.AssertUtils;

import java.lang.annotation.Annotation;

import org.reflections.scanners.AbstractScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaAnnotationScanner extends AbstractScanner
{

    Logger logger = LoggerFactory.getLogger(MetaAnnotationScanner.class);
            
    private final Class<? extends Annotation> annotationType;

    private final String metaAnnotationRegex;

    public MetaAnnotationScanner(Class<? extends Annotation> annotationType)
    {
        this.annotationType = annotationType;
        metaAnnotationRegex = null;
    }
    
    public MetaAnnotationScanner(String  metaAnnotationRegex)
    {
        this.metaAnnotationRegex = metaAnnotationRegex;
        annotationType = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void scan(Object cls)
    {
        final String className = getMetadataAdapter().getClassName(cls);
        try
        {
            Class<?> klass = Class.forName(className);
            
            if ( annotationType != null &&  AssertUtils.hasAnnotationDeep(klass, annotationType) && ! klass.isAnnotation() )
            {
                getStore().put(annotationType.getName(), className);
            }
            
            if ( metaAnnotationRegex != null &&  AssertUtils.hasAnnotationDeepRegex(klass, metaAnnotationRegex) && ! klass.isAnnotation() )
            {
                getStore().put(metaAnnotationRegex, className);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}
