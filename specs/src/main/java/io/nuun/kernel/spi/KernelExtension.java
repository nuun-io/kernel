/**
 * This file is part of Nuun IO Kernel Specs.
 *
 * Nuun IO Kernel Specs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Specs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Specs.  If not, see <http://www.gnu.org/licenses/>.
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
 * @author Pierre Thirouin {@literal <pierre.thirouin@ext.mpsa.com>}
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
     * Notifies the given extension that the plugins are injected.
     *
     * @param extendedPlugins the plugin to notify
     */
    public void injected(Collection<T> extendedPlugins);

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
