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
package io.nuun.kernel.tests.ut.fixtures;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.di.ModuleValidation;
import io.nuun.kernel.core.NuunCore;
import io.nuun.kernel.core.internal.KernelCore;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.core.internal.scanner.inmemory.InMemoryMultiThreadClasspath;
import io.nuun.kernel.tests.ut.fixtures.TestExecutor.TestExecutorWith;
import io.nuun.kernel.tests.ut.fixtures.TestExecutor.TestExecutotFlow;

import org.kametic.specifications.Specification;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

public class GivenWhenThenInternal implements FixtureConfiguration, TestExecutor, TestExecutorWith, TestExecutotFlow, ResultValidator
{

    KernelCore                               kernel = null;
    private Class<? extends Plugin>          pluginClass;
    private ClasspathBuilder                 classpath;
    private Injector                         injector;
    private ModuleValidation validation;

    public GivenWhenThenInternal()
    {
        validation = new ModuleValidation()
        {

            @Override
            public boolean canHandle(Class<?> injectionDefinition)
            {
                return true;
            }

            @Override
            public void validate(UnitModule unitModule)
            {
                Module m = Module.class.cast(unitModule.get());
                
                Visitor v = new Visitor();
                
                for (Element e : Elements.getElements(m))
                {
                    e.acceptVisitor(v);
                }
            }
        };
    }

    static class Visitor extends DefaultElementVisitor<Void>
    {
        @Override
        protected Void visitOther(Element element)
        {
            System.out.println(" => " + element);
            return null;
        }
    }

    @Override
    public TestExecutor given(Class<? extends Plugin> pluginClass)
    {
        this.pluginClass = pluginClass;
        return this;
    }

    @Override
    public ResultValidator whenUsing(ClasspathBuilder classpath)
    {
        this.classpath = classpath;
        createAndStartKernel();
        return this;
    }

    @Override
    public TestExecutorWith whenClasspathIs(Class<?> class_)
    {
        return this;
    }

    @Override
    public TestExecutorWith whenClasspathIs(String base, String resource)
    {
        return this;
    }

    @Override
    public TestExecutotFlow with(Class<?> class_)
    {
        return this;
    }

    @Override
    public TestExecutotFlow with(String base, String resource)
    {
        return this;
    }

    @Override
    public ResultValidator then(Specification<? extends UnitModule> predicate)
    {
        return this;
    }


    //

    @SuppressWarnings("unchecked")
    private void createAndStartKernel()
    {
        kernel = (KernelCore) NuunCore.createKernel( //
                NuunCore.newKernelConfiguration() //
                        .plugins(pluginClass) //
                        .classpathScanMode(ClasspathScanMode.IN_MEMORY) //
                        .moduleValidation(validation)
                //
                );

        InMemoryMultiThreadClasspath.INSTANCE.reset();
        classpath.configure();
        kernel.init();
        kernel.start();
        injector = kernel.getObjectGraph().as(Injector.class);

    }

}
