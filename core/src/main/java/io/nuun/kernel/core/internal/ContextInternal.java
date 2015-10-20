/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.core.internal;

import com.google.inject.Injector;
import io.nuun.kernel.api.di.ObjectGraph;
import io.nuun.kernel.api.plugin.context.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Epo Jemba
 * 
 */
@Singleton
class ContextInternal implements Context
{

    public final Injector mainInjector;

    /**
     * Constructor.
     *
     * @param mainInjector the guice main injector
     */
    @Inject
    public ContextInternal(Injector mainInjector)
    {
        this.mainInjector = mainInjector;
    }

    @Override
    public ObjectGraph applicationObjectGraph() {
        return new ObjectGraphEmbedded(mainInjector);
    }
}
