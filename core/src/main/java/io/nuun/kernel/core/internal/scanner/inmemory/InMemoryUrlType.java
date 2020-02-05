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

import io.nuun.kernel.api.inmemory.ClasspathAbstractElement;
import io.nuun.kernel.api.inmemory.ClasspathClass;
import io.nuun.kernel.api.inmemory.ClasspathResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		InMemoryClasspath classpath = InMemoryClasspath.INSTANCE;
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
			
			List<File> files = new ArrayList<>();

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
