
package io.nuun.kernel.tests.it.annotations;

import io.nuun.kernel.api.Plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on integration tests to specify manually which plugins Nuun will load.
 *
 * @author epo.jemba{@literal @}kametic.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithPlugins {

    Class<? extends Plugin>[] value();

}
