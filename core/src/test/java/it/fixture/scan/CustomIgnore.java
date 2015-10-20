package it.fixture.scan;

import io.nuun.kernel.api.annotations.Ignore;

import java.lang.annotation.*;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@Ignore
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface CustomIgnore {
}
