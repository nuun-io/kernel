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
package io.nuun.kernel.core.internal.concerns;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.core.internal.Fixture;
import io.nuun.kernel.core.internal.concerns.sample.CachePlugin;
import io.nuun.kernel.core.internal.concerns.sample.LogPlugin;
import io.nuun.kernel.core.internal.concerns.sample.SecurityPlugin;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ConcernTest
{

    static Kernel underTest;
    private static List<String> list;

    @BeforeClass
    public static void init()
    {
        list = new ArrayList<String>();
        underTest = Fixture.startKernel(Fixture.config()
                .plugins(
                        new InternalPlugin(),
                        new CachePlugin(list),
                        new LogPlugin(list),
                        new SecurityPlugin(list)
                )
        );
    }

    static class MyObj
    {

        void triggerMethod(List<String> list)
        {
            list.add("fire");
        }
    }

    public static class InternalPlugin extends AbstractPlugin
    {

        @Override
        public String name()
        {
            return "nominal plugin";
        }

        @Override
        public Object nativeUnitModule()
        {
            return new Module();
        }
    }

    public static class Module extends AbstractModule
    {

        @Override
        protected void configure()
        {
            bind(MyObj.class);
        }
    }

    @Test
    public void test()
    {

        MyObj obj = underTest.objectGraph().as(Injector.class).getInstance(MyObj.class);
        obj.triggerMethod(list);
        Assertions.assertThat(list).hasSize(7);
        Assertions.assertThat(list).containsExactly("pre security", "pre cache", "pre log", "fire", "post log", "post cache", "post security");
    }

    @AfterClass
    public static void clear()
    {
        if (underTest.isStarted())
        {
            underTest.stop();
        }
    }

}
