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
package io.nuun.kernel.core.pluginsit.dummy6;

import io.nuun.kernel.core.AbstractPlugin;

public class DummyPlugin6_D extends AbstractPlugin
{
    private boolean internal = false;

    @Override
    public String name()
    {
        return "dummy-6-D";
    }

    public boolean isInternal()
    {
        return internal;
    }

    public void setInternal(boolean internal)
    {
        this.internal = internal;
    }

    @Override
    public String toString()
    {
        return "D";
    }

}
