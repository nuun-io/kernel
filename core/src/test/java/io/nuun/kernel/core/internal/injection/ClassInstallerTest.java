/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.core.internal.injection;

import com.google.inject.Scopes;
import io.nuun.kernel.spi.Concern;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.nuun.kernel.spi.Concern.Priority.HIGHER;
import static io.nuun.kernel.spi.Concern.Priority.LOWER;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassInstallerTest
{

    private ClassInstaller greaterInstaller;
    private ClassInstaller lowerInstaller;

    @Before
    public void setUp() throws Exception
    {
        greaterInstaller = new ClassInstaller(ClassToBind.class, Scopes.SINGLETON);
        lowerInstaller = new ClassInstaller(ClassToBind2.class);
    }

    @Test
    public void testInstallerOrder() {
        assertThat(greaterInstaller.order()).isEqualTo(HIGHER.value() + 20);
        assertThat(lowerInstaller.order()).isEqualTo(LOWER.value() - 20);
    }

    @Test
    public void testComparison() {
        assertThat(greaterInstaller).isGreaterThan(lowerInstaller);
    }

    @Concern(name="higher" , priority= HIGHER, order = 20)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    public @interface HigherConcern
    {
    }

    @HigherConcern
    static class ClassToBind {
    }

    @Concern(name="lower" , priority= LOWER, order = -20)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    public @interface LowerConcern
    {
    }

    @LowerConcern
    static class ClassToBind2 {
    }
}
