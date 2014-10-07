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
/**
 * 
 */
package io.nuun.kernel.core.pluginsit.dummy23;

import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;


/**
 * @author Epo Jemba
 * 
 */
public class DummyPlugin2 extends AbstractPlugin
{

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#name()
     */
    @Override
    public String name()
    {
        return "dummyPlugin2";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#pluginsRequired()
     */
    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    @Override
    public Collection<Class<? extends Plugin>> requiredPlugins()
    {
        return (Collection) collectionOf(DummyPlugin3.class);
    }
    
    
    @Override
    public InitState init(InitContext initContext)
    {
        assertThat( initContext.pluginsRequired() ).isNotNull();
        assertThat( initContext.pluginsRequired() ).hasSize(1);
        assertThat( initContext.pluginsRequired().iterator().next().getClass() ).isEqualTo(DummyPlugin3.class);
        return InitState.INITIALIZED;
    }

}
