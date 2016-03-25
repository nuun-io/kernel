/**
 * This file is part of Nuun IO Kernel Tests.
 *
 * Nuun IO Kernel Tests is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Tests is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Tests.  If not, see <http://www.gnu.org/licenses/>.
 */
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
