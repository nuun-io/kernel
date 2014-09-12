package io.nuun.kernel.core.internal.scanner.inmemory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;


public class InMemoryHandler extends URLStreamHandler
{

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		
		return null;
	}
	
}