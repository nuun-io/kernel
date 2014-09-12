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

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.reflections.vfs.Vfs.Dir;
import org.reflections.vfs.Vfs.File;
import org.reflections.vfs.Vfs.UrlType;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class InMemoryUrlType implements UrlType
{
	
	private Map<String, List<? extends  InMemoryFile<?>>> fs;

	public InMemoryUrlType(Map<String, List<? extends  InMemoryFile<?>>> fs ) {
		this.fs = fs;
	}

	@Override
	public boolean matches(URL url) throws Exception
	{
		return url != null && url.getProtocol().equalsIgnoreCase("inmemory");
	}

	@Override
	public Dir createDir(URL url) throws Exception {
		return new InMemoryVfsDir(url.getPath());
	}
	
	class InMemoryVfsDir implements Dir
	{
		private String path;

		public InMemoryVfsDir(String path) {
			this.path = path;
			
		}
		
		@Override
		public String getPath() {
			return path;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Iterable<File> getFiles() {
			
			return (Iterable<File>) fs.get(path);
		}
		
		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}
	}
}
