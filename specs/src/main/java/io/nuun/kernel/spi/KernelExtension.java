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

package io.nuun.kernel.spi;

import java.util.Collection;

/**
 * Kernel extensions provide an SPI to enhanced the kernel behavior.
 * <p>
 * To create a kernel extension, create a class which implements the {@code KernelExtension} interface. And a marker
 * interface associated to the extension as follows:
 * </p>
 * <pre>
 * class MyFeatureExtension implements KernelExtension{@literal <MyFeature>} {...}
 *
 * interface MyFeature {...}
 * </pre>
 * <p>
 * Then, create a plugin implementing the extension marker.
 * </p>
 * <pre>
 * class MyPlugin extends AbstractPlugin implements MyFeature {...}
 * </pre>
 * <p>
 * The kernel extension will be called at each step of the kernel lifecycle (initializing, initialized, starting, etc.).
 * The list of plugin implementing the kernel extension marker will be passed to the kernel extension at each method call.
 * </p>
 * @param <T> the kernel extension marker to be implemented by plugins.
 * @author Pierre Thirouin <pierre.thirouin@ext.mpsa.com>
 */
public interface KernelExtension<T>
{
    /**
     * Notifies the given extension that the kernel is initializing.
     *
     * @param extendedPlugins the plugin to notify
     */
    public void initializing(Collection<T> extendedPlugins);

    /**
     * Notifies the given extension that the kernel is initialized.
     *
     * @param extendedPlugins the plugin to notify
     */
    public void initialized(Collection<T> extendedPlugins);

    /**
     * Notifies the given extension that the kernel is starting.
     *
     * @param extendedPlugins the plugin to notify
     */
    public void starting(Collection<T> extendedPlugins);

    /**
     * Notifies the given extension that the kernel is started.
     *
     * @param extendedPlugins the plugin to notify
     */
    public void started(Collection<T> extendedPlugins);

    /**
     * Notifies the given extension that the kernel is stopping.
     *
     * @param extendedPlugins the plugin to notify
     */
    public void stopping(Collection<T> extendedPlugins);

    /**
     * Notifies the given extension that the kernel is stopped.
     *
     * @param extendedPlugins the plugin to notify
     */
    public void stopped(Collection<T> extendedPlugins);
}
