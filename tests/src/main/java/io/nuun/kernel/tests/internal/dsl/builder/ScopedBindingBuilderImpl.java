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
package io.nuun.kernel.tests.internal.dsl.builder;

import java.lang.annotation.Annotation;

import com.google.inject.Scope;

import io.nuun.kernel.tests.internal.dsl.holder.ScopedHolder;
import io.nuun.kernel.tests.ut.dsl.assertor.ScopedBindingBuilder;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class ScopedBindingBuilderImpl implements ScopedBindingBuilder
{
    
    protected ScopedHolder scopedHolder;

    public ScopedBindingBuilderImpl(ScopedHolder scopedHolder)
    {
        this.scopedHolder = scopedHolder;
    }
    
    @Override
    public void in(Class<? extends Annotation> scopeAnnotation)
    {
        scopedHolder.setScopeAnnotation(scopeAnnotation);
    }

    @Override
    public void in(Scope scope)
    {
        scopedHolder.setScope(scope);
    }

    @Override
    public void asEagerSingleton()
    {
        scopedHolder.setEagerSingleton();
    }

}
