/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com> Licensed under the GNU LESSER GENERAL PUBLIC LICENSE,
 * Version 3, 29 June 2007; or any later version you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.gnu.org/licenses/lgpl-3.0.txt Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */
package io.nuun.kernel.core;

import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.api.ClasspathScanMode;
import io.nuun.kernel.api.inmemory.Classpath;
import io.nuun.kernel.api.inmemory.ClasspathJar;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.core.internal.scanner.inmemory.InMemoryMultiThreadClasspath;
import io.nuun.kernel.core.pluginsit.dummy5.DescendantFromClass;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;
import io.nuun.kernel.core.pluginsit.dummy5.GrandParentClass;
import io.nuun.kernel.core.pluginsit.dummy5.ParentClass;

import org.junit.Before;
import org.junit.Test;

/**
 * @author epo.jemba@kametic.com
 */
public class KernelSuite8Test
{
    private Kernel    underTest;

    @Before
    public void init()
    {
        ClasspathBuilder cpb = new ClasspathBuilder()
        {
            @Override
            public void configure()
            {
                jar("test.jar");
                class_(GrandParentClass.class);
                class_(DescendantFromClass.class);
                class_(ParentClass.class);
            }
        };

        /*
         * 
          collection = scannedSubTypesByAncestorClass.get(GrandParentClass.class);
        
        assertThat(collection).isNotEmpty();
        assertThat(collection).hasSize(2);
        assertThat(collection).containsOnly(DescendantFromClass.class , ParentClass.class);
         */
        cpb.configure();
    }

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error()
    {
        underTest = Kernel.createKernel().withClasspathScanMode(ClasspathScanMode.IN_MEMORY , null).withoutSpiPluginsLoader() //
                .withPlugins(new DummyPlugin5() //
                ) //
                .build(); //
        underTest.init();
        underTest.start();

        // String resa = underTest.getMainInjector().getInstance( Key.get(String.class, Names.named("dep7a"))
        // );
        // assertThat(resa).isNotNull();
        // assertThat(resa).isEqualTo("dep7aOVER");

    }

}
