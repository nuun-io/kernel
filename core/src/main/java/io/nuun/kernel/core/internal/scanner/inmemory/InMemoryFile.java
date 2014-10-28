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
