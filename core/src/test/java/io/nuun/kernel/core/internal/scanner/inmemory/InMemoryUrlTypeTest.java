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

import static org.fest.assertions.Assertions.assertThat;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class InMemoryUrlTypeTest {

	InMemoryUrlType underTest ;
	private InMemoryFactory factory;
	
	
	@Before
	public void init () {
		List<? extends  InMemoryFile<?>> fs = Lists.newArrayList();
		
		Map<String,List<? extends  InMemoryFile<?>>> m =  Maps.newHashMap();
		
		m.put("zobd", fs  );
		
		underTest = new InMemoryUrlType(m);
		factory = new InMemoryFactory();
	}
	
	@Test
	public void testMatches() throws Exception {
		
		URL inMemo1 = factory.createInMemoryResource("toto.txt");
		
		assertThat(  underTest.matches(inMemo1)).isTrue();
		
		
	}

	@Test
	public void testCreateDir() {
		
	}

}
