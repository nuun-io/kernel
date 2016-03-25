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
