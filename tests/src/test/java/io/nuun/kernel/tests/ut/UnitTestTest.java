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

import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.tests.Fixtures;
import io.nuun.kernel.tests.ut.fixtures.FixtureConfiguration;
import io.nuun.kernel.tests.ut.sample.SamplePlugin;
import io.nuun.kernel.tests.ut.sample.Service1;
import io.nuun.kernel.tests.ut.sample.Service1Impl;
import io.nuun.kernel.tests.ut.sample.Service1Provider;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
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
    public void checkFixture ()
    {
        newGivenWhenThenFixture //
        .given(SamplePlugin.class) //
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
        }) .then( null );
    }

}
