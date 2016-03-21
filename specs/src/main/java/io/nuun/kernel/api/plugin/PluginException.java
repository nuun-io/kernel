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
package io.nuun.kernel.api.plugin;


/**
 * A plugin exception is a {@code RuntimeException} which can be thrown
 * by a plugin when something wrong happened during its lifecycle.
 *
 * @author Epo Jemba
 */
public class PluginException extends RuntimeException
{

    public PluginException()
    {
    }

    public PluginException(String message , Object... params)
    {
        super( String.format(message, params) );
    }

    public PluginException(String message ,  Throwable cause)
    {
        super(message , cause);
    }

    public PluginException(String message ,  Throwable cause, Object... params)
    {
        super(String.format(message, params), cause);
    }
    
    
    
    private static final long serialVersionUID = -5031708737156896850L;
}
