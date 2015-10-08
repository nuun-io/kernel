package it.fixture.scan;

import java.lang.annotation.*;

/**
 * Used for test fixtures. Indicates classes to scan.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface ToScan {
}
