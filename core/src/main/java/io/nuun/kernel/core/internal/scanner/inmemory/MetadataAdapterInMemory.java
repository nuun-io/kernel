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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.reflections.adapters.MetadataAdapter;
import org.reflections.vfs.Vfs.File;

import com.google.common.base.Joiner;

/**
 *
 * 
 * @author epo.jemba{@literal @}kametic.com
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
		for (@SuppressWarnings("unused") Class<?> c : method.getParameterTypes()) {
			parNames.add("" + i);
			i++;
		}
		
		return parNames;
	}

	@Override
	public List<String> getClassAnnotationNames(Class<?> aClass) {
		List<String> fm = new ArrayList<String>();
		
		for (Annotation anno : aClass.getAnnotations()) {
			fm.add(anno.annotationType().getName());
		}
		
		return fm;
	}

	@Override
	public List<String> getFieldAnnotationNames(Field field) {
		
		List<String> fm = new ArrayList<String>();
		
		for (Annotation anno : field.getAnnotations()) {
			fm.add(anno.annotationType().getName());
		}
		
		return fm;
	}

	@Override
	public List<String> getMethodAnnotationNames(Method method) {
		List<String> fm = new ArrayList<String>();
		
		for (Annotation anno : method.getAnnotations()) {
			fm.add(anno.annotationType().getName());
		}
		
		return fm;
	}

	@Override
	public List<String> getParameterAnnotationNames(Method method, int parameterIndex) {
		
		List<String> fm = new ArrayList<String>();
		
		if (parameterIndex  < method.getParameterAnnotations().length ) {
			for (Annotation anno : method.getAnnotations()) {
				fm.add(anno.annotationType().getName());
			}
		}
				
		return fm;
	}

	@Override
	public String getReturnTypeName(Method method) {
		String name = null;
		
		Class<?> returnType = method.getReturnType();
		
		if ( returnType != null) {
			name = returnType.getName();
		}
				
		return name;
	}

	@Override
	public String getFieldName(Field field) {
		
		if (field != null) {
			return field.getName();
		}
		else {
			return null;
		}
	}

	@Override
	public Class<?> getOfCreateClassObject(File file) throws Exception {
		
		if (file instanceof InMemoryClass)
		{
			InMemoryClass imf = (InMemoryClass) file;
			return imf.getContent();
		}
		
		return null;
	}

	@Override
	public String getMethodModifier(Method method) {
	     int accessFlags = method.getModifiers();
	        return Modifier.isPrivate(accessFlags)  ? "private" :
	        	   Modifier.isProtected(accessFlags)? "protected" :
	        	   Modifier.isPublic(accessFlags)   ? "public" : "";
	}

	@Override
	public String getMethodKey(Class<?> cls, Method method) {

		return method.getName() + "(" + Joiner.on(", ").join(getParameterNames(method)) + ")";
		
	}

	@Override
	public String getMethodFullKey(Class<?> cls, Method method) {
		
		return getClassName(cls) + "." + getMethodKey(cls, method);
	}

	@Override
	public boolean isPublic(Object o) {
		Integer accessFlag =
				  o instanceof Class ? ((Class<?>) o).getModifiers() :
		                o instanceof Field ? ((Field) o).getModifiers() :
		                o instanceof Method ? ((Method) o).getModifiers() : null;
		                		
		return Modifier.isPublic(accessFlag);
	}

	@Override
	public boolean acceptsInput(String file) {
		return true;
	}
}
