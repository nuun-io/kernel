/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com> Licensed under the GNU LESSER GENERAL PUBLIC LICENSE,
 * Version 3, 29 June 2007; or any later version you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.gnu.org/licenses/lgpl-3.0.txt Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */
package io.nuun.kernel.core;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.core.pluginsit.dummy5.DescendantFromClass;
import io.nuun.kernel.core.pluginsit.dummy5.DummyPlugin5;
import io.nuun.kernel.core.pluginsit.dummy5.GrandParentClass;
import io.nuun.kernel.core.pluginsit.dummy5.ParentClass;
import org.junit.Before;
import org.junit.Test;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class KernelSuite8Test
{

    @Before
    public void init()
    {
        ClasspathBuilder cpb = new ClasspathBuilder()
        {
            @Override
            public void configure()
            {
                addJar("test.jar");
                addClass(GrandParentClass.class);
                addClass(DescendantFromClass.class);
                addClass(ParentClass.class);
            }
        };

        cpb.configure();
    }

    @Test
    public void dependee_plugins_that_misses_should_be_source_of_error()
    {

        Kernel underTest = createKernel(

                newKernelConfiguration()
                        .rootPackages("io.nuun.kernel")
                        .classpathScanMode(ClasspathScanMode.IN_MEMORY)
                        .withoutSpiPluginsLoader()
                        .plugins(new DummyPlugin5())
        );

        underTest.init();
        underTest.start();
    }
}
