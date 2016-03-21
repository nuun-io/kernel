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
package io.nuun.kernel.core.internal.scanner.inmemory;

import java.io.IOException;
import java.io.InputStream;

import org.reflections.vfs.Vfs.File;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public abstract class InMemoryFile<T> implements File {
	
	protected T content;

	public InMemoryFile(T content) {
		this.content = content;
	}

	@Override
	public abstract String getName();

	@Override
	public abstract String getRelativePath() ;

	@Override
	public InputStream openInputStream() throws IOException {
		throw new UnsupportedOperationException("InMemoryfile does not provide inputstream");
	}

	public T getContent() {
		return content;
	}

}
