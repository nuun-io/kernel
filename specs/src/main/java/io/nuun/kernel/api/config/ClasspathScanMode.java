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
 * ClasspathScanMode lets you configure the kernel regarding Classpath Scan.
 * 
 * @author epo.jemba{@literal @}kametic.com
 */
public enum ClasspathScanMode {
	
	/**
	 *  This mode is the nominal behaviour where the scanner
	 *  will reach information from the filesystem.
	 */
	NOMINAL,
	
	/**
	 * This mode tells the kernel to read its class and resources
	 * information from the memory. This mode will be use mainly
	 * for unit test.
	 */
	IN_MEMORY

}
