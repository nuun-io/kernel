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
package io.nuun.kernel.tests.ut.sample.dummy;

import io.nuun.kernel.core.AbstractPlugin;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
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
                getProvider(Service1Impl.class);
                
                bind(Service1Impl.class);
                
                bind(Service2Impl.class).in(Scopes.SINGLETON);
                
                bind(String.class).toInstance("Yoloh");
                
//                bind(Service1.class).to(Service1Impl.class).asEagerSingleton(); // LinkedKey
                bind(Service1.class).toInstance(new Service1Impl());              // InstanceBinding
                String name = "un";
                bind(Key.get(Service1.class, Names.named(name))).toProvider(Service1Provider.class); // ProviderKey
                name = "deux";
                bind(Key.get(Service1.class, Names.named(name))).toProvider(Providers.guicify(new Service1Provider(name))); // ProviderInstance
                name = "trois";
                bind(Service4.class).toProvider(new Service4Provider());// ProviderKey
                
//                binder().requireAtInjectOnConstructors();
                
                binder().requireExplicitBindings();
                
                

            }
            
            // ProviderInstanceBinding
            @Provides
            public Service2 provideService2()
            {
                return null;
            }
        };
    }

}
