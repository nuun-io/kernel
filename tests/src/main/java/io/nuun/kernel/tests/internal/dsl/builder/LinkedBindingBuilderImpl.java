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

import io.nuun.kernel.tests.internal.dsl.holder.InjectedHolder;
import io.nuun.kernel.tests.internal.dsl.holder.ScopedHolder;
import io.nuun.kernel.tests.internal.dsl.holder.TimesHolder;
import io.nuun.kernel.tests.ut.assertor.dsl.LinkedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.ScopedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.TimedScopedBindingBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.TimesBuilder;
import io.nuun.kernel.tests.ut.assertor.dsl.Wildcard;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
 *
 */
public class LinkedBindingBuilderImpl<T>  extends ScopedBindingBuilderImpl implements LinkedBindingBuilder<T>
{
    
    protected InjectedHolder injectedHolder;

    public LinkedBindingBuilderImpl(InjectedHolder injectedHolder)
    {
        super( injectedHolder.as(ScopedHolder.class));
        this.injectedHolder = injectedHolder;
    }
    
    @Override
    public void toInstance(T instance)
    {
        injectedHolder.setInstance(instance);
    }
    @Override
    public TimesBuilder toInstance(Wildcard wildcard)
    {
        injectedHolder.setWildcard(wildcard);
        return new TimesBuilderImpl(injectedHolder.as(TimesHolder.class));
    }

    @Override
    public ScopedBindingBuilder<Void> to(Class<? extends T> implementation)
    {
        injectedHolder.setClass(implementation);
        return new ScopedBindingBuilderImpl((ScopedHolder) this.injectedHolder  );
    }

    @Override
    public TimedScopedBindingBuilder to(Wildcard wildcard)
    {
        injectedHolder.setWildcard(wildcard);
        return  new TimedScopedBindingBuilderImpl((ScopedHolder) this.injectedHolder );
    }

}
