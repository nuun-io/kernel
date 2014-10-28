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

import io.nuun.kernel.api.inmemory.ClasspathAbstractElement;
import io.nuun.kernel.api.inmemory.ClasspathClass;
import io.nuun.kernel.api.inmemory.ClasspathResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.reflections.vfs.Vfs;
import org.reflections.vfs.Vfs.Dir;
import org.reflections.vfs.Vfs.File;
import org.reflections.vfs.Vfs.UrlType;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class InMemoryUrlType implements UrlType
{

	public InMemoryUrlType( ) {
	}

	@Override
	public boolean matches(URL url) throws Exception
	{
		return url != null && url.getProtocol().equalsIgnoreCase("inmemory");
	}

	@Override
	public Dir createDir(URL url) throws Exception {
		String path = url.getPath();
		if (path.startsWith("/"))
		{
			path = path.substring(1);
		}
		return new InMemoryVfsDir(path);
	}
	
	class InMemoryVfsDir implements Dir
	{
		InMemoryMultiThreadClasspath classpath = InMemoryMultiThreadClasspath.INSTANCE;
		private String path;

		public InMemoryVfsDir(String path)
		{
			this.path = path;
		}
		
		@Override
		public String getPath()
		{
			return path;
		}
		
		
		@Override
		public Iterable<File> getFiles()
		{

			// TODO ne renvoyer que l'entry de l'url donné en paramêtre
			
			List<File> files = new ArrayList<Vfs.File>();
		    
			for ( ClasspathAbstractElement<?> entry : classpath.entry(path).entries() )
			{
				if (entry instanceof ClasspathClass)
				{
					files.add(new InMemoryClass(((ClasspathClass) entry).getType()));
				}
				if (entry instanceof ClasspathResource)
				{
					files.add(new InMemoryResource(   ( (ClasspathResource) entry).internalRepresentation()      ));
				}
			}

			
			return files;
			
		}
		
		@Override
		public void close() {
			
		}
	}

}
