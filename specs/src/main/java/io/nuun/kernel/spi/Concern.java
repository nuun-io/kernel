/**
 * This file is part of Nuun IO Kernel Specs.
 *
 * Nuun IO Kernel Specs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Specs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Specs.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.spi;

import java.lang.annotation.*;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Concern
{

    int order() default 0;

    String name();

    Priority priority() default Priority.NORMAL;

    enum Priority
    {
        LOWEST(-(3L << 32)),
        LOWER(-(2L << 32)),
        LOW(-(1L << 32)),
        NORMAL(0),
        HIGH(1L << 32),
        HIGHER(2L << 32),
        HIGHEST(3L << 32);

        private long value;

        Priority(long value)
        {
            this.value = value;
        }

        public long value() {
            return value;
        }
    }

}
