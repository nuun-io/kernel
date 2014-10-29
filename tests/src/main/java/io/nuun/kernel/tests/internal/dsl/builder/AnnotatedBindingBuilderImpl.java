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

import io.nuun.kernel.tests.internal.dsl.holder.AnnotatedHolder;
import io.nuun.kernel.tests.internal.dsl.holder.InjectedHolder;
import io.nuun.kernel.tests.ut.assertor.dsl.AnnotatedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.LinkedBindingBuilder;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class AnnotatedBindingBuilderImpl<T> extends LinkedBindingBuilderImpl<T> implements AnnotatedBindingBuilder<T>
{

    public AnnotatedBindingBuilderImpl(AnnotatedHolder annotatedHolder)
    {
        super( annotatedHolder.as(InjectedHolder.class));
    }
    
    @Override
    public LinkedBindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType)
    {
        return new LinkedBindingBuilderImpl<T>(injectedHolder);
        
    }
    
    @Override
    public LinkedBindingBuilder<T> annotatedWith(Annotation annotation)
    {
        return new LinkedBindingBuilderImpl<T>(injectedHolder);
    }

}
