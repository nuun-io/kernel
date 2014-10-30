/**
 * Copyright (C) 2014 Kametic <epo.jemba{@literal @}kametic.com>
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
package io.nuun.kernel.tests.internal.dsl.builder;

import java.lang.annotation.Annotation;

import com.google.inject.Scope;

import io.nuun.kernel.tests.internal.dsl.holder.AnnotatedHolder;
import io.nuun.kernel.tests.internal.dsl.holder.InjectedHolder;
import io.nuun.kernel.tests.ut.assertor.dsl.AnnotatedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.LinkedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.ScopedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.TimedLinkedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.Wildcard;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class AnnotatedBindingBuilderImpl<T,B> extends LinkedBindingBuilderImpl<T,B> implements AnnotatedBindingBuilder<T,B>
{

    public AnnotatedBindingBuilderImpl(AnnotatedHolder annotatedHolder)
    {
        super( annotatedHolder.as(InjectedHolder.class));
    }
    
    @Override
    public LinkedBindingBuilder<T,B> annotatedWith(Class<? extends Annotation> annotationType)
    {
        injectedHolder.as(AnnotatedHolder.class).setAnnotatedWith(annotationType);
        return new LinkedBindingBuilderImpl<T,B>(injectedHolder);
        
    }
    
    @Override
    public LinkedBindingBuilder<T,B> annotatedWith(Annotation annotation)
    {
        injectedHolder.as(AnnotatedHolder.class).setAnnotatedWith(annotation);
        return new LinkedBindingBuilderImpl<T,B>(injectedHolder);
    }


    @Override
    public TimedLinkedBindingBuilder<T> annotatedWith(Wildcard wildcard)
    {
        return null;
    }


}
