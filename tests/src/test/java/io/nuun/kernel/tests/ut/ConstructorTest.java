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
package io.nuun.kernel.tests.ut;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.tests.ut.assertor.ModuleAssertor;
import io.nuun.kernel.tests.ut.sample.constructor.ConstructorPlugin;
import io.nuun.kernel.tests.ut.sample.constructor.ConstructorService;
import io.nuun.kernel.tests.ut.sample.constructor.Payload;

import java.util.List;

import com.google.common.collect.Lists;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class ConstructorTest extends UnitTestTest
{

    @Override
    Class<? extends Plugin> underTestClass()
    {
        return ConstructorPlugin.class;
    }

    @Override
    
    List<Class<?>> classpath ()
    {
        return  Lists.<Class<?>>newArrayList(ConstructorService.class,Payload.class);
    }

    @Override
    ModuleAssertor moduleAssertor()
    {
        return new ModuleAssertor()
        {
            
            @Override
            public void configure()
            {
            }
        } ;
    }
    
    

}
