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
package io.nuun.kernel.api.plugin.request.annotations;

import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javassist.expr.NewArray;

import org.kametic.specifications.AbstractSpecification;

/**
 * @author epo.jemba@kametic.com
 *
 */
public class InjectedPlugin extends TestPlugin
{
    final Object object = "Epo Jemba";

    @Override
    public String name()
    {
        return "injected-plugin";
    }
    
    class MySpecification extends AbstractSpecification<Class<?>> 
    {
        @Override
        public boolean isSatisfiedBy(Class<?> candidate)
        {
            return object != null;
        }
    }
    
    @KernelParams("param")
    String param;
    
    @Dependent
    Plugin1 plugin1;
    
    @Required
    Plugin2 plugin2;
    
    @KernelParams("param")
    String param1;
    
    @Scan(MySpecification.class) 
    Collection<Class<?>> interfaces;

    @Scan(MySpecification.class) @Round(1)
    Collection<Class<?>> implementation;
    
    @Override
    public InitState init(InitContext initContext)
    {
        return super.init(initContext);
    }

    @Override
    public UnitModule unitModule()
    {
        return null;
    }
    
    public void test() 
    {
        System.out.println("Constructors");
        System.out.println("Enclosing class " + MySpecification.class.getEnclosingClass());
        
        Constructor<?>[] constructors = MySpecification.class.getDeclaredConstructors();
        if (constructors != null)
        {
            for( Constructor<?>  constructor :  constructors) 
            {
               System.out.println( " => " + constructor.getParameterTypes().length ); 
               System.out.println( " => " + constructor.getParameterTypes()[0].getName() ); 
               
               MySpecification specification = null;
            try
            {
                specification = (MySpecification) constructor.newInstance(this);
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
               
               System.out.println( "s " + specification.isSatisfiedBy(getClass())); 
            }
        }
    }

}
