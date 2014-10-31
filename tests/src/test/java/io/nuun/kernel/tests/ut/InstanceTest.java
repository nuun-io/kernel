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
import io.nuun.kernel.tests.ut.assertor.dsl.wildcard.Wildcard;
import io.nuun.kernel.tests.ut.sample.instance.InstancePlugin;
import io.nuun.kernel.tests.ut.sample.instance.InstanceService;
import io.nuun.kernel.tests.ut.sample.instance.InstanceServiceImpl;

import java.util.Arrays;
import java.util.List;

import com.google.inject.name.Names;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class InstanceTest extends UnitTestTest
{

    @Override
    Class<? extends Plugin> underTestClass()
    {
        return InstancePlugin.class;
    }

    @Override
    List<Class<?>> classpath ()
    {
        return Arrays.<Class<?>>asList(InstanceService.class,InstanceServiceImpl.class);

    }

    @Override
    ModuleAssertor moduleAssertor()
    {
        return new ModuleAssertor()
        {
            @Override
            public void configure()
            {
                assertBind(InstanceService.class).toInstance(new InstanceServiceImpl("empty"));
                assertBind(InstanceService.class).toInstance(ANY).once();
                assertBind(InstanceService.class).annotatedWith(Names.named("one")).toInstance(new InstanceServiceImpl("one"));
                assertBind(InstanceService.class).annotatedWith(Names.named("one")).toInstance(Wildcard.ANY).twice();
            }
        } ;
    }
    
    

}
