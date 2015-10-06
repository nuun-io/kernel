package io.nuun.kernel.core.internal.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pierre Thirouin
 */
public class NuunReflectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(NuunReflectionUtils.class);

    public static <T> T silentNewInstance(Class<?> aClass)
    {
        T instance = null;

        try
        {
            //noinspection unchecked
            instance = (T) aClass.newInstance();
        }
        catch (InstantiationException e)
        {
            LOGGER.error("Error when instantiating class " + aClass, e);
        }
        catch (IllegalAccessException e)
        {
            LOGGER.error("Error when instantiating class " + aClass, e);
        }

        return instance;
    }
}
