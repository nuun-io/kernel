package io.nuun.kernel.tests.it.annotations;

import java.lang.annotation.*;

/**
 * This annotation can be used on integration tests to specify kernel parameters.
 *
 * @author Pierre Thirouin {@literal <pierre.thirouin@gmail.com>}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithParams {

    /**
     * List of kernel parameters as key/value pairs.
     * <pre>
     * {@literal @}WithParams("key1", "val1", "key2", "val2")
     * </pre>
     *
     * @return key/value pairs
     */
    String[] value();
}
