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
package io.nuun.kernel.spi.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD ,  ElementType.ANNOTATION_TYPE })
public @interface NuunProperty {
    String value();
    boolean mandatory() default true;
    String defaultValue() default "";
    byte defaultByteValue() default 0;
    short defaultShortValue() default 0;
    int defaultIntValue() default 0;
    long defaultLongValue() default 0;
    float defaultFloatValue() default 0;
    double defaultDoubleValue() default 0.0;
    boolean defaultBooleanValue() default false; 
    Class<? extends NuunConfigurationConverter<?>> converter() default NuunDummyConverter.class;
}