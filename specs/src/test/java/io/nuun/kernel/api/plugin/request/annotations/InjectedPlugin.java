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
package io.nuun.kernel.api.plugin.request.annotations;

import io.nuun.kernel.api.plugin.AbstractPlugin;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;

import java.util.Collection;

public class InjectedPlugin extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "injected-plugin";
    }
    
    
    @Dependent
    Plugin1 plugin1;
    
    @Required
    Plugin2 plugin2;
    
    @Request(Specs.kernel_param1)
    String param1;
    
    @Request(Specs.interface_services)
    Collection<Class<?>> interfaces;

    @Request(Specs.implem_services)
    Collection<Class<?>> implementation;
    
    @Override
    public InitState init(InitContext initContext)
    {
        return super.init(initContext);
    }
    

}
