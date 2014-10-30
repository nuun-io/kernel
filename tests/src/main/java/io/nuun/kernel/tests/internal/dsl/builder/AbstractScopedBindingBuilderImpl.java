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
import io.nuun.kernel.tests.ut.assertor.dsl.ScopedBindingBuilder;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
 *
 */
public abstract class AbstractScopedBindingBuilderImpl<B> implements ScopedBindingBuilder<B>
{
    
    protected ScopedHolder scopedHolder;

    public AbstractScopedBindingBuilderImpl(ScopedHolder scopedHolder)
    {
        this.scopedHolder = scopedHolder;
    }
    
    @Override
    public B in(Class<? extends Annotation> scopeAnnotation)
    {
        scopedHolder.setScopeAnnotation(scopeAnnotation);
        return doReturnScope();
    }

    @Override
    public B in(Scope scope)
    {
        scopedHolder.setScope(scope);
        return doReturnScope();
    }

    @Override
    public B asEagerSingleton()
    {
        scopedHolder.setEagerSingleton();
        return doReturnScope();
    }
    
    abstract protected B doReturnScope();

}
