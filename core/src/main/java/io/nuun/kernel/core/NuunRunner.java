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
package io.nuun.kernel.core;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.annotations.EntryPoint;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

public class NuunRunner
{

    public static NuunRunnerDsl entrypoint(Class<? extends Runnable> entrypointClass)
    {
        return new NuunRunnerDsl(entrypointClass);
    }

    public static class NuunRunnerDsl
    {

        private Class<?> entrypointClass;

        public NuunRunnerDsl(Class<? extends Runnable> entrypointClass)
        {
            this.entrypointClass = entrypointClass;
        }

        public void execute(String... args)
        {
            String rootFromEntryPoint = rootPackage();

            final Kernel kernel = NuunCore.createKernel(NuunCore.newKernelConfiguration().rootPackages(rootFromEntryPoint).addPlugin(new AbstractPlugin()
            {

                @Override
                public String name()
                {
                    return "entrypoint-plugin";
                }

                @Override
                public Object nativeUnitModule()
                {
                    return new AbstractModule()
                    {
                        @Override
                        protected void configure()
                        {
                            bind(entrypointClass);
                        }
                    };
                }

            }).containerContext(args));

            kernel.init();

            kernel.start();

            Runnable r = (Runnable) kernel.objectGraph().as(Injector.class).getInstance(entrypointClass);

            Runtime.getRuntime().addShutdownHook(new Thread()
            {
                @Override
                public void run()
                {
                    kernel.stop();
                }
            });

            r.run();

        }

        private String rootPackage()
        {
            EntryPoint ep = entrypointClass.getAnnotation(EntryPoint.class);
            String root = ep.packageScan();
            if (root != null && root.length() > 0)
            {
                return root;
            }
            return entrypointClass.getPackage().getName();
        }
    }
}

/* 
*/