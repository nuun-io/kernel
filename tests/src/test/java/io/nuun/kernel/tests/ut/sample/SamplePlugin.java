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
package io.nuun.kernel.tests.ut.sample;

import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.core.AbstractPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class SamplePlugin extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "sample-plugin";
    }
    
    @Override
    public Object nativeUnitModule()
    {
        return new AbstractModule()
        {
            

            @Override
            protected void configure()
            {

                bind(Service1.class).to(Service1Impl.class);
                String name = "un";
                bind(Key.get(Service1.class, Names.named(name))).toProvider(Providers.guicify(new Service1Provider(name)));
                name = "deux";
                bind(Key.get(Service1.class, Names.named(name))).toProvider(Providers.guicify(new Service1Provider(name)));
                name = "trois";
                bind(Key.get(Service1.class, Names.named(name))).toProvider(Providers.guicify(new Service1Provider(name)));

            }
        };
    }

}
