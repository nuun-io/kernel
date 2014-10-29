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
package io.nuun.kernel.tests.ut.sample.instance;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import io.nuun.kernel.tests.ut.sample.AbstractTestPlugin;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class InstancePlugin extends AbstractTestPlugin
{

    @Override
    protected Class<?> underTestModuleClass()
    {
        return InstanceModule.class;
    }
    
    public static class InstanceModule extends AbstractModule
    {

        @Override
        protected void configure()
        {
            bind(InstanceService.class).toInstance(new InstanceServiceImpl("empty"));
            bind(InstanceService.class). annotatedWith(Names.named("one")).toInstance(new InstanceServiceImpl("one"));
        }
        
    }


}
