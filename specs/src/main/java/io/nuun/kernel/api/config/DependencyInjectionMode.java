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
package io.nuun.kernel.api.config;

/**
 * Kernel modes are the mode in which the internal dependency injection framework should work.
 * <ul>
 *    <li> Production Mode </li>
 *    <li> Dev Mode </li>
 *    <li> Tools Mode </li>
 * </ul>
 * 
 * They were inspired by Guice Stage.
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public enum DependencyInjectionMode {
	
	
	  /**
	   * <strong> taken from Guice</strong>
	   * We're running in a tool (an IDE plugin for example). We need binding meta data but not a
	   * functioning Injector. Do not inject members of instances. Do not load eager singletons. Do as
	   * little as possible so our tools run nice and snappy. Injectors created in this stage cannot
	   * be used to satisfy injections.
	   */
	  TOOL,

	  /**
	   * <strong> taken from Guice</strong>
	   * We want fast startup times at the expense of runtime performance and some up front error
	   * checking.
	   */
	  DEVELOPMENT,

	  /**
	   * <strong> taken from Guice</strong>
	   * We want to catch errors as early as possible and take performance hits up front.
	   */
	  PRODUCTION
	

}
