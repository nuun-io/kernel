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
package io.nuun.kernel.tests.internal.dsl;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.di.ModuleValidation;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.core.NuunCore;
import io.nuun.kernel.core.internal.KernelCore;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.core.internal.scanner.inmemory.InMemoryMultiThreadClasspath;
import io.nuun.kernel.tests.internal.ElementMap;
import io.nuun.kernel.tests.ut.assertor.ElementDelta;
import io.nuun.kernel.tests.ut.assertor.ModuleAssertor;
import io.nuun.kernel.tests.ut.assertor.ModuleDiff;
import io.nuun.kernel.tests.ut.assertor.dsl.AssertBuilder;
import io.nuun.kernel.tests.ut.fixture.FixtureConfiguration;
import io.nuun.kernel.tests.ut.fixture.ThenBuilder;
import io.nuun.kernel.tests.ut.fixture.WhenBuilder;
import io.nuun.kernel.tests.ut.fixture.WhenBuilder.WhenBuilderMore;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

/**
 * 
 * @author epo.jemba@kametic.com
 * @author pierre.thirouin@gmail.com
 *
 */
public class GivenWhenThenInternal implements FixtureConfiguration, WhenBuilder, ThenBuilder , WhenBuilderMore , AssertBuilder
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
                Module m = Module.class.cast(unitModule.nativeModule());
                
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
    public WhenBuilder given(Class<? extends Plugin> pluginClass)
    {
        this.pluginClass = pluginClass;
        return this;
    }

    
    
    @Override
    public WhenBuilderMore whenUsing(ClasspathBuilder classpath)
    {
        this.classpath = classpath;
        createAndStartKernel();
        return this;
    }
    
    @Override
    public WhenBuilderMore withLoaded(Class<? extends Plugin> pluginClass)
    {
        return this;
    }
    
    @Override
    public WhenBuilderMore withLoaded(Plugin plugin)
    {
        return this;
    }

    @Override
    public AssertBuilder then()
    {
        return this;
    }
    
    @Override
    public AssertBuilder assertModule(ModuleAssertor assertor)
    {
        UnitModule unitModule = kernel.unitModule(pluginClass);
        assertor.configure();
        ModuleDiff moduleDiff = new ModuleDiff(unitModule.as(Module.class), assertor);
        ElementMap<ElementDelta> diff = moduleDiff.diff();
        
        System.out.println(assertor.globalHolders().toString());
        
        if ( ! diff.isEmpty()) {
            throw new AssertionError("Oups , ça marche pas !");
        }
        
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
//                        .moduleValidation(validation)
                //
                );

        InMemoryMultiThreadClasspath.INSTANCE.reset();
        classpath.configure();
        kernel.init();
        kernel.start();
        injector = kernel.objectGraph().as(Injector.class);

    }





}
