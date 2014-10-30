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

import io.nuun.kernel.tests.ut.assertor.DslContent;
import io.nuun.kernel.tests.ut.assertor.dsl.Wildcard;

import org.kametic.specifications.Specification;

import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
 *
 */
public class GlobalHolder implements InjecteeHolder, AnnotatedHolder , InjectedHolder , ScopedHolder , TimesHolder , DslContent
{
    // 1 - injectees
    protected Class<?> injecteeClass;
    protected Key<?> injecteeKey;
    protected TypeLiteral<?> injecteeTypeLiteral;
    protected Wildcard injecteeWildcard;
    protected Integer injecteeTimes;
    //
    protected Annotation injecteeAnnotation;
    protected Class<? extends Annotation> injecteeAnnotationType;
    
    // 2 - injected-s
    protected Object injectedInstance;
    protected Specification<Object> injectedSpecification;
    protected Class<?> injectedClass;
    protected Wildcard injectedWildcard;
    protected Integer injectedTimes;
    
    // 3 - scope
    protected Class<? extends Annotation> scopeAnnotation;
    protected Scope scope;
    protected Boolean isEagerSingleton;
    protected Integer scopeTimes;
    
    // 4 - Times
    protected Integer times;

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
    public void setAnnotatedWith(Annotation annotation)
    {
        injecteeAnnotation = annotation;
    }
    
    @Override
    public void setAnnotatedWith(Class<? extends Annotation> annotationType)
    {
        injecteeAnnotationType = annotationType;
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
    
    ////////////////////////////////////////

    @Override
    public void setTimes(Integer times)
    {
        this.times = times;
    }
    
    @Override
    public <T> T as(Class<T> fromType)
    {
        return fromType.cast(this);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GlobalHolder [");
        builder.append("\nInjectee Info\n");
        if (injecteeClass != null)
        {
            builder.append("injecteeClass=").append(injecteeClass).append(", ");
        }
        if (injecteeKey != null)
        {
            builder.append("injecteeKey=").append(injecteeKey).append(", ");
        }
        if (injecteeTypeLiteral != null)
        {
            builder.append("injecteeTypeLiteral=").append(injecteeTypeLiteral).append(", ");
        }
        if (injecteeWildcard != null)
        {
            builder.append("injecteeWildcard=").append(injecteeWildcard).append(", ");
        }
        if (injecteeTimes != null)
        {
            builder.append("injecteeTimes=").append(injecteeTimes).append(", ");
        }
        builder.append("\nAnnotation Info\n");
        if (injecteeAnnotation != null)
        {
            builder.append("injecteeAnnotation=").append(injecteeAnnotation).append(", ");
        }
        if (injecteeAnnotationType != null)
        {
            builder.append("injecteeAnnotationType=").append(injecteeAnnotationType).append(", ");
        }
        builder.append("\nInjected Info\n");
        if (injectedInstance != null)
        {
            builder.append("injectedInstance=").append(injectedInstance).append(", ");
        }
        if (injectedSpecification != null)
        {
            builder.append("injectedSpecification=").append(injectedSpecification).append(", ");
        }
        if (injectedClass != null)
        {
            builder.append("injectedClass=").append(injectedClass).append(", ");
        }
        if (injectedWildcard != null)
        {
            builder.append("injectedWildcard=").append(injectedWildcard).append(", ");
        }
        if (injectedTimes != null)
        {
            builder.append("injectedTimes=").append(injectedTimes).append(", ");
        }
        builder.append("\nScope Info\n");
        if (scopeAnnotation != null)
        {
            builder.append("scopeAnnotation=").append(scopeAnnotation).append(", ");
        }
        if (scope != null)
        {
            builder.append("scope=").append(scope).append(", ");
        }
        if (isEagerSingleton != null)
        {
            builder.append("isEagerSingleton=").append(isEagerSingleton).append(", ");
        }
        if (scopeTimes != null)
        {
            builder.append("scopeTimes=").append(scopeTimes).append(", ");
        }
        builder.append("\nGlobal Times\n");
        if (times != null)
        {
            builder.append("times=").append(times);
        }
        builder.append("]");
        return builder.toString();
    }

    

}
