package io.nuun.kernel.core.internal.utils;


import com.google.common.collect.Lists;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author Pierre Thirouin
 */
public class NuunReflectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(NuunReflectionUtils.class);

    @SuppressWarnings("unchecked")
    public static <T> T instantiateSilently(Class<?> aClass) {
        try {
            return (T) aClass.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("Error when instantiating class " + aClass, e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Error when instantiating class " + aClass, e);
        }
        return null;
    }

    public static Class<?> forNameSilent(String candidate) {
        try {
            return Class.forName(candidate);
        } catch (Throwable e) {
            LOGGER.debug("Fail to load class {}: {}", candidate, e.getMessage());
        }
        return null;
    }

    public static <T> Collection<Class<? extends T>> forNames(@Nullable Collection<String> names) {
        if (names == null || names.size() == 0) {
            return Lists.newArrayList();
        }
        return ReflectionUtils.forNames(names, new ClassLoader[]{NuunReflectionUtils.class.getClassLoader()});
    }
}
