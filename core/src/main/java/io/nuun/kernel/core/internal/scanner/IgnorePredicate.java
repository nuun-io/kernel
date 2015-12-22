package io.nuun.kernel.core.internal.scanner;

import com.google.common.base.Predicate;
import io.nuun.kernel.api.annotations.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

class IgnorePredicate implements Predicate<Class<?>> {

	private final boolean keepAbstractClasses;

	public IgnorePredicate(boolean keepAbstractClasses) {
		this.keepAbstractClasses = keepAbstractClasses;
	}

	@Override
	public boolean apply(Class<?> clazz) {
		boolean keepClass = true;

		if (!keepAbstractClasses && isAbstractClass(clazz)) {
			keepClass = false;
		}

		for (Annotation annotation : clazz.getAnnotations()) {
			if (annotation.annotationType().equals(Ignore.class) || annotation.annotationType().getAnnotation(Ignore.class) != null) {
				keepClass = false;
				break;
			}
		}
		return keepClass;
	}

	private boolean isAbstractClass(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface();
	}
}
