/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.core.internal.concerns;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.api.plugin.AbstractPlugin;
import io.nuun.kernel.core.NuunCore;
import io.nuun.kernel.core.internal.concerns.sample.CachePlugin;
import io.nuun.kernel.core.internal.concerns.sample.LogPlugin;
import io.nuun.kernel.core.internal.concerns.sample.SecurityPlugin;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

public class ConcernTest
{

    static Kernel underTest;
    private static List<String> list;
    
    @BeforeClass
    public static void init()
    {
        list = new ArrayList<String>();
        
        KernelConfiguration newKernelConfiguration = NuunCore.newKernelConfiguration();
        newKernelConfiguration.withoutSpiPluginsLoader().plugins(new InternalPlugin() , new CachePlugin(list ) , new LogPlugin(list) , new SecurityPlugin(list));
        
        underTest = NuunCore.createKernel(newKernelConfiguration);
        underTest.init();
        underTest.start();
    }
    
    static class MyObj
    {
        
        void triggerMethod(List<String> list)
        {
            list.add("fire");
        }
    }
    
    public static class InternalPlugin extends AbstractPlugin
    {
        
    	@Override
    	public String name ()
    	{
    		return "nominal plugin";
    	}

    	@Override
        public Object dependencyInjectionDef()
        {
            return new Module();
        }
    }
    
    public static class Module extends AbstractModule
    {
        
        @Override
        protected void configure()
        {
            bind(MyObj.class);
        }
    }
    
    @Test
    public void test()
    {
        
        MyObj obj = underTest.getObjectGraph().as(Injector.class).getInstance(MyObj.class);
        obj.triggerMethod(list);
        Assertions.assertThat(list).hasSize(7);
        Assertions.assertThat(list).containsExactly("pre security" , "pre cache" , "pre log", "fire" , "post log",  "post cache"  ,  "post security");
    }
    
    @AfterClass
    public static void clear()
    {
        if ( underTest.isStarted()) {
			underTest.stop();
		}
    }

}
