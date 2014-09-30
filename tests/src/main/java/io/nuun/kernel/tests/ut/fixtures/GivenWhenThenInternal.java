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
import io.nuun.kernel.core.Kernel;
import io.nuun.kernel.core.internal.scanner.inmemory.ClasspathBuilder;
import io.nuun.kernel.tests.ut.fixtures.TestExecutor.TestExecutorWith;
import io.nuun.kernel.tests.ut.fixtures.TestExecutor.TestExecutotFlow;

import com.google.common.base.Predicate;
import com.google.inject.Module;

public class GivenWhenThenInternal implements FixtureConfiguration, TestExecutor, TestExecutorWith, TestExecutotFlow , ResultValidator
{

    Kernel kernel = null;
    private Class<? extends Plugin> pluginClass;
    private ClasspathBuilder classpath;
    
    public GivenWhenThenInternal()
    {
    }
    
    @Override
    public TestExecutor given(Class<? extends Plugin> pluginClass)
    {
        this.pluginClass = pluginClass;
        return this;
    }
    
    @Override
    public ResultValidator expectModule(Predicate<? extends Module> predicate)
    {
        return null;
    }

    @Override
    public ResultValidator expectBinding(Predicate<? extends Module> predicate)
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
    public ResultValidator whenUsing(ClasspathBuilder classpath)
    {
        this.classpath = classpath;
//        kernel = Kernel.createKernel(null)
        return this;
    }

    @Override
    public TestExecutorWith whenUsing(Class<?> class_)
    {
        return this;
    }

    @Override
    public TestExecutorWith whenUsing(String base, String resource)
    {
        return this;
    }



}
