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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.reflections.adapters.MetadataAdapter;
import org.reflections.vfs.Vfs.File;

/**
 *
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class MetadataAdapterInMemory implements MetadataAdapter<Class<?>, Field, Method> {

	@Override
	public String getClassName(Class<?> cls) {
		
		return cls.getName();
	}

	@Override
	public String getSuperclassName(Class<?> cls) {
		
		return cls.getSuperclass().getName();
	}

	@Override
	public List<String> getInterfacesNames(Class<?> cls) {
		List<String> itfNames = new ArrayList<String>();
		
		for (Class<?> c : cls.getInterfaces()) {
			itfNames.add(c.getName());
		}
		return itfNames;
	}

	@Override
	public List<Field> getFields(Class<?> cls) {
		List<Field> fl = new ArrayList<Field>();
		
		for (Field f : cls.getDeclaredFields()) {
			fl.add(f);
		}
		
		return fl;
	}

	@Override
	public List<Method> getMethods(Class<?> cls) {
		List<Method> fm = new ArrayList<Method>();
		
		for (Method f : cls.getDeclaredMethods()) {
			fm.add(f);
		}

		return fm;
	}

	@Override
	public String getMethodName(Method method) {
		
		return method.getName();
	}

	@Override
	public List<String> getParameterNames(Method method) {
		List<String> parNames = new ArrayList<String>();
		int i = 0;
		for (Class<?> c : method.getParameterTypes()) {
			parNames.add("" + i);
			i++;
		}
		
		return parNames;
	}

	@Override
	public List<String> getClassAnnotationNames(Class<?> aClass) {
		List<String> fm = new ArrayList<String>();
		
		
		
		return null;
	}

	@Override
	public List<String> getFieldAnnotationNames(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMethodAnnotationNames(Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getParameterAnnotationNames(Method method, int parameterIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturnTypeName(Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFieldName(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getOfCreateClassObject(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethodModifier(Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethodKey(Class<?> cls, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethodFullKey(Class<?> cls, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPublic(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}
