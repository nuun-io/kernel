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
package io.nuun.kernel.tests.internal.dsl.holder;

import java.lang.annotation.Annotation;

import io.nuun.kernel.tests.ut.Wildcard;

import org.kametic.specifications.Specification;

import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class GlobalHolder implements InjecteeHolder, InjectedHolder , ScopedHolder
{
    // 1 - injectees
    protected Class<?> injecteeClass;
    protected Key<?> injecteeKey;
    protected TypeLiteral<?> injecteeTypeLiteral;
    protected Wildcard injecteeWildcard;
    protected Integer injecteeTimes;
    
    // 2 - injected-s
    protected Object injectedInstance;
    protected Specification<Object> injectedSpecification;
    protected Class<?> injectedClass;
    protected Wildcard injectedWildcard;
    protected Integer injectedTimes;
    
    // 3 - annotation
    protected Class<? extends Annotation> scopeAnnotation;
    protected Scope scope;
    protected Boolean isEagerSingleton;
    protected Integer scopeTimes;

    ////////////////////////////////////////////////////

    @Override
    public void setSpecification(Specification<Object> injectedSpecification)
    {
        this.injectedSpecification = injectedSpecification;
    }

    @Override
    public void setInjecteeClass(Class<?> injecteeClass)
    {
        this.injecteeClass = injecteeClass;
    }

    @Override
    public void setInjecteeKey(Key<?> injecteeKey)
    {
        this.injecteeKey = injecteeKey;
    }

    @Override
    public void setInjecteeTypeLiteral(TypeLiteral<?> injecteeTypeLiteral)
    {
        this.injecteeTypeLiteral = injecteeTypeLiteral;
    }
    
    @Override
    public void setInjecteeWildcard(Wildcard injecteeWildcard)
    {
        this.injecteeWildcard = injecteeWildcard;
    }
    
    @Override
    public void setInjecteeTimes(Integer injecteeTimes)
    {
        this.injecteeTimes = injecteeTimes;
    }

    ////////////////////////////////////////////////////
    
    @Override
    public void setInstance(Object injectedInstance)
    {
        this.injectedInstance = injectedInstance;
    }

    @Override
    public void setClass(Class<?> injectedClass)
    {
        this.injectedClass = injectedClass;
    }
    
    @Override
    public void setWildcard(Wildcard injectedWildcard)
    {
        this.injectedWildcard = injectedWildcard;
    }

    @Override
    public void setInjectedTimes(Integer injectedTimes)
    {
        this.injectedTimes = injectedTimes;
    }
    
    ////////////////////////////////////////////////////
    
    @Override
    public void setScopeAnnotation(Class<? extends Annotation> scopeAnnotation)
    {
        this.scopeAnnotation = scopeAnnotation;
    }

    @Override
    public void setScope(Scope scope)
    {
        this.scope = scope;
    }

    @Override
    public void setEagerSingleton()
    {
        isEagerSingleton  = Boolean.TRUE;
    }
    
    @Override
    public void setScopeTimes(Integer scopeTimes)
    {
        this.scopeTimes = scopeTimes;
    }
    


}
