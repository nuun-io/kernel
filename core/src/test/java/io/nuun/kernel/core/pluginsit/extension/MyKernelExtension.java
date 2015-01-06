/*
 * Copyright (C) 2014 Kametic <pierre.thirouin@gmail.com>
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

package io.nuun.kernel.core.pluginsit.extension;

import io.nuun.kernel.spi.KernelExtension;

import java.util.Collection;

/**
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 *         05/01/2015
 */
public class MyKernelExtension implements KernelExtension<MyExtensionInterface> {

    public int count = 0;

    @Override
    public void initializing(Collection<MyExtensionInterface> extensions) {
        count += 1;
    }

    @Override
    public void initialized(Collection<MyExtensionInterface> extensions) {
        count += 10;
    }

    @Override
    public void starting(Collection<MyExtensionInterface> extensions) {
        count += 100;
    }

    @Override
    public void started(Collection<MyExtensionInterface> extensions) {
        count += 1000;
    }

    @Override
    public void stopping(Collection<MyExtensionInterface> extensions) {
        count += 10000;
    }

    @Override
    public void stopped(Collection<MyExtensionInterface> extensions) {
        count += 100000;
    }
}
