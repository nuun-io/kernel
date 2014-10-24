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

import static io.nuun.kernel.tests.ut.assertor.dsl.Wildcard.ANY;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.tests.Fixtures;
import io.nuun.kernel.tests.internal.visitor.MapElementVisitor;
import io.nuun.kernel.tests.ut.assertor.ModuleAssertor;
import io.nuun.kernel.tests.ut.assertor.dsl.Wildcard;
import io.nuun.kernel.tests.ut.fixture.FixtureConfiguration;
import io.nuun.kernel.tests.ut.sample.SamplePlugin;
import io.nuun.kernel.tests.ut.sample.Service1;
import io.nuun.kernel.tests.ut.sample.Service1Impl;
import io.nuun.kernel.tests.ut.sample.Service1Provider;

import org.junit.Before;
import org.junit.Test;
import org.kametic.specifications.AbstractSpecification;
import org.kametic.specifications.Specification;

import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

/**
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
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
                .given(SamplePlugin.class)
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
                }) //
                .withLoaded(SamplePlugin.class)
                
                .then()
                .assertModule(new ModuleAssertor()
                {
                    @Override
                    public void configure()
                    {
                        assertBind(Key.get(Service1Impl.class));
                        assertBind(Key.get(Service1Impl.class)).asEagerSingleton();
                        assertBind(ANY).twice();
                        assertBind(Wildcard.ANY) .asEagerSingleton().once();
                        //
                        assertBind(Key.get(String.class)).toInstance("toto");
                        assertBind(Key.get(String.class)).to(Wildcard.ANY).once();
                        assertBind(Key.get(String.class)).to(ANY).asEagerSingleton().once();
                        //
                        assertBind(Key.get(String.class)).to(String.class).in(Scopes.SINGLETON);
                        assertBind(Key.get(String.class)).to(Wildcard.ANY).in(Scopes.SINGLETON).times(10);

                    }
                })
                ;
          
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
