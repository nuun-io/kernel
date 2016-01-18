/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
