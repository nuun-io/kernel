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
package io.nuun.kernel.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KernelOptions
{
    public static final KernelOption<List<String>> ROOT_PACKAGES = new KernelOption<>("root.packages");
    public static final KernelOption<Integer> THREAD_COUNT = new KernelOption<>("thread.count");
    public static final KernelOption<Boolean> PRINT_SCAN_WARN = new KernelOption<>("scan.warn.disable");
    public static final KernelOption<Boolean> ENABLE_REFLECTION_LOGGER = new KernelOption<>("reflection.logger.disable");
    public static final KernelOption<Boolean> SCAN_PLUGIN = new KernelOption<>("plugin.scan.disable");
    public static final KernelOption<ClasspathScanMode> CLASSPATH_SCAN_MODE = new KernelOption<>("classpath.scan.mode");
    public static final KernelOption<DependencyInjectionMode> DEPENDENCY_INJECTION_MODE = new KernelOption<>("dependency.injection.mode");

    private final Map<String, Object> options = new HashMap<>();

    public KernelOptions()
    {
        set(ROOT_PACKAGES, new ArrayList<>());
        set(THREAD_COUNT, defaultThreadCount());
        set(PRINT_SCAN_WARN, true);
        set(ENABLE_REFLECTION_LOGGER, false);
        set(SCAN_PLUGIN, true);
        set(CLASSPATH_SCAN_MODE, ClasspathScanMode.NOMINAL);
        set(DEPENDENCY_INJECTION_MODE, DependencyInjectionMode.PRODUCTION);
    }

    public <T> KernelOptions set(KernelOption<T> option, T value)
    {
        options.put(option.getName(), value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(KernelOption<T> option)
    {
        return (T) options.get(option.getName());
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : options.entrySet())
        {
            stringBuilder.append(entry.getKey()).append(": ");
            if (entry.getValue() != null)
            {
                stringBuilder.append(entry.getValue().toString());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private int defaultThreadCount()
    {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (availableProcessors <= 2) {
            return availableProcessors;
        } else {
            return Math.min(4, availableProcessors / 2);
        }
    }
}
