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
package io.nuun.kernel.tests.it.fixtures;

import com.google.common.base.Predicate;
import com.google.inject.Module;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.tests.it.fixtures.TestExecutor.TestExecutor2;

class GivenWhenThenInternal implements FixtureConfiguration, TestExecutor, TestExecutor2, ResultValidator
{

    public GivenWhenThenInternal()
    {
    }
    
    @Override
    public TestExecutor given(Class<? extends Plugin> pluginClass)
    {
        return null;
    }
    
    @Override
    public ResultValidator expectModule(Predicate<? extends Module> predicate)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultValidator expectBinding(Predicate<? extends Module> predicate)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Flow1 with(Class<?> class_)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Flow1 with(String base, String resource)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultValidator whenUsing(ClasspathBuilder classpath)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TestExecutor2 whenUsing(Class<?> class_)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TestExecutor2 whenUsing(String base, String resource)
    {
        // TODO Auto-generated method stub
        return null;
    }



}
