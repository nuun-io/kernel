/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.core.internal.scanner.sample;

import io.nuun.kernel.core.pluginsit.dummy1.Bean6;
import io.nuun.kernel.core.pluginsit.dummy1.Bean9;
import io.nuun.kernel.core.pluginsit.dummy1.DummyService;

import javax.inject.Inject;


public class HolderForPlugin
{

//    @NuunProperty("value1")
//    public String       value;

    @Inject
    public DummyService dummyService;

    @Inject
    public Bean6        bean6;
    
    @Inject
    public Bean9        bean9;
}