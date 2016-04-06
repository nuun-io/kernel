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

import com.google.inject.AbstractModule;
import io.nuun.kernel.spi.Concern;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.nuun.kernel.spi.Concern.Priority.HIGH;
import static org.assertj.core.api.Assertions.assertThat;

public class ModuleInstallerTest
{

    private UnitModuleInstaller greaterInstaller;
    private UnitModuleInstaller lowerInstaller;

    @Before
    public void setUp() throws Exception
    {
        MyModule module = new MyModule();
        greaterInstaller = new UnitModuleInstaller(ModuleEmbedded.wrap(module));
        MyModule2 module2 = new MyModule2();
        lowerInstaller = new UnitModuleInstaller(ModuleEmbedded.wrap(module2));
    }

    @Test
    public void testInstallerOrder()
    {
        assertThat(greaterInstaller.order()).isEqualTo(HIGH.value() + 20);
        assertThat(lowerInstaller.order()).isEqualTo(0);
    }

    @Test
    public void testComparison()
    {
        assertThat(greaterInstaller).isGreaterThan(lowerInstaller);
    }

    @HighConcern
    static class MyModule extends AbstractModule
    {
        @Override
        protected void configure()
        {

        }
    }

    @Concern(name = "high", priority = HIGH, order = 20)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface HighConcern
    {
    }

    static class MyModule2 extends AbstractModule
    {
        @Override
        protected void configure()
        {

        }
    }
}
