/**
 * This file is part of Nuun IO Kernel Tests.
 *
 * Nuun IO Kernel Tests is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Tests is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Tests.  If not, see <http://www.gnu.org/licenses/>.
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
