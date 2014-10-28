/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com> Licensed under the GNU LESSER GENERAL PUBLIC LICENSE,
 * Version 3, 29 June 2007; or any later version you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.gnu.org/licenses/lgpl-3.0.txt Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */
package io.nuun.kernel.api.inmemory;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class Resource
{

    public static final String PATTERN = "[a-zA-Z0-9\\-_\\.]+(/[a-zA-Z0-9\\-_\\.]+)*";

    private String             base;
    private String             name;

    public Resource(String base, String name)
    {
        this.base = base;
        this.name = name;
    }

    public String base()
    {
        return base;
    }

    public String name()
    {
        return name;
    }
}