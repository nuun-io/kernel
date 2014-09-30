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
package io.nuun.kernel.api.config;

/**
 * Kernel modes are the mode in which the internal dependency injection framework should work.
 * <li>
 *    <ul> Production Mode
 *    <ul> Dev Mode
 *    <ul> Tools Mode
 * </li>
 * 
 * They were inspired by Guice Stage.
 * 
 * @author epo.jemba@kametic.com
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
