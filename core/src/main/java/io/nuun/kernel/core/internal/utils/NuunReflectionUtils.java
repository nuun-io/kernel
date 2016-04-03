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
package io.nuun.kernel.core.internal.utils;


import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.PluginException;
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
    public static <T> T instantiateOrFail(Class<?> aClass) {
        try {
            return (T) aClass.newInstance();
        } catch (InstantiationException e) {
            throw new PluginException(e);
        } catch (IllegalAccessException e) {
            throw new PluginException(e);
        }
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
