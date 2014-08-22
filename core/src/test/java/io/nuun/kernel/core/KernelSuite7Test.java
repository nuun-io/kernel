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
package io.nuun.kernel.core;

import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.core.pluginsit.dummy7.DummyPlugin7_A;
import io.nuun.kernel.core.pluginsit.dummy7.DummyPlugin7_B;

import org.junit.After;
import org.junit.Test;

import com.google.inject.Key;
import com.google.inject.name.Names;

public class KernelSuite7Test
{

    private Kernel underTest;
    
    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error()
    {
        underTest = Kernel.createKernel() //
                .withoutSpiPluginsLoader() //
                .withPlugins( //
                        new DummyPlugin7_A(), //
                        new DummyPlugin7_B() //
                ) //
                .build(); //
        underTest.init();
        underTest.start();
        
        String resa = underTest.getMainInjector().getInstance( Key.get(String.class, Names.named("dep7a")) );
        assertThat(resa).isNotNull();
        assertThat(resa).isEqualTo("dep7aOVER");
    }
    
    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error_()
    {
        underTest = Kernel.createKernel() //
                .withoutSpiPluginsLoader() //
                .withPlugins( //
                        new DummyPlugin7_A(), //
                        new DummyPlugin7_B() //
                        ) //
                        .build(); //
        underTest.init();
        underTest.start();
        
        String resa = underTest.getMainInjector().getInstance( Key.get(String.class, Names.named("dep7b")) );
        assertThat(resa).isNotNull();
        assertThat(resa).isEqualTo("dep7bOVER");
        
    }


    @After
    public void stopKernel()
    {
        underTest.stop();
    }

}
