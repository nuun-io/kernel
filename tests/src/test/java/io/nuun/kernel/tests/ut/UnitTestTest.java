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
package io.nuun.kernel.tests.ut;

import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.tests.Fixtures;
import io.nuun.kernel.tests.ut.fixtures.FixtureConfiguration;
import io.nuun.kernel.tests.ut.fixtures.MapElementVisitor;
import io.nuun.kernel.tests.ut.sample.SamplePlugin;
import io.nuun.kernel.tests.ut.sample.Service1;
import io.nuun.kernel.tests.ut.sample.Service1Impl;
import io.nuun.kernel.tests.ut.sample.Service1Provider;

import org.junit.Before;
import org.junit.Test;
import org.kametic.specifications.AbstractSpecification;
import org.kametic.specifications.Specification;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

/**
 * @author epo.jemba@kametic.com
 */
public class UnitTestTest
{

    private FixtureConfiguration newGivenWhenThenFixture;

    @Before
    public void init()
    {
        newGivenWhenThenFixture = Fixtures.newGivenWhenThenFixture();
    }

    @Test
    public void checkFixture()
    {
        newGivenWhenThenFixture //
                .given(SamplePlugin.class) // dependence ; required
                .whenUsing(new ClasspathBuilder()
                {
                    @Override
                    public void configure()
                    {
                        addJar("test.jar");
                        addClass(Service1.class);
                        addClass(Service1Impl.class);
                        addClass(Service1Provider.class);
                    }
                }).then(bindingAreOk());
                
                ; /*.then().unitModule()
                  .contains(Module)
                  .containsExactly(Module2)
                  .contains(Element);
                  
                
                
                
                Binder binder = null;
                {
                  binder.bind(Service1.class).to(Service1Impl.class);
                } // une fois
                {
                    binder.bind(Service1.class);
                } // 1 fois
                
                binding(Service1.class).to(Service1Impl.class).isPresent()
//                }).then(bindingAreOk())
                     .and(zerezrzer()) ; */
    }

    protected Specification<UnitModule> bindingAreOk()
    {
        return new AbstractSpecification<UnitModule>()
        {
            @Override
            public boolean isSatisfiedBy(UnitModule candidate)
            {
                if (candidate == null)
                {
                    return false;
                }
                if (!Module.class.isAssignableFrom(candidate.nativeModule().getClass()))
                {
                    return false;
                }

                Module module = candidate.as(Module.class);
                MapElementVisitor visitor = new MapElementVisitor();
                for (Element element : Elements.getElements(Stage.DEVELOPMENT, module))
                {
                    element.acceptVisitor(visitor);
                }
                
                System.out.println("" + visitor.getStore().keySet());

                return false;
            }
        };
    }

}
