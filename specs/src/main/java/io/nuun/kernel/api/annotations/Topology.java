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
package io.nuun.kernel.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Topology is a java interface annotated with @Topology. It is the configuration unit of your application
 * of your application. It will describe the bindings, AOP and other configurations. Topologies can compare
 * with spring javaconfig, but topologies are interfaces not POJO. Topologies are declaratives not
 * imperatives, they do not need to be unit tested (who is unit testing its spring java config class anyway ;p
 * ) We follow an easy to read convention to indicate what to bind, on what key with which qualifiers etc etc.
 * 
 * @author epo.jemba@kametic.com (Epo Jemba)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.TYPE
})
public @interface Topology
{
    /**
     * @return an array properties source
     */
    String[] propertySources() default "";
}
