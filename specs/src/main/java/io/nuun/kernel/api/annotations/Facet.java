package io.nuun.kernel.api.annotations;

import java.lang.annotation.*;

/**
 * The facet annotation is used to mark interfaces exposing a plugin API.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Facet {
}
