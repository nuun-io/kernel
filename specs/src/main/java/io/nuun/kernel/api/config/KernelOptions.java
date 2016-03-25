package io.nuun.kernel.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KernelOptions
{
    public static final KernelOption<List<String>> ROOT_PACKAGES = new KernelOption<List<String>>("root.packages");
    public static final KernelOption<Boolean> PRINT_SCAN_WARN = new KernelOption<Boolean>("scan.warn.disable");
    public static final KernelOption<Boolean> ENABLE_REFLECTION_LOGGER = new KernelOption<Boolean>("reflection.logger.disable");
    public static final KernelOption<Boolean> SCAN_PLUGIN = new KernelOption<Boolean>("plugin.scan.disable");
    public static final KernelOption<ClasspathScanMode> CLASSPATH_SCAN_MODE = new KernelOption<ClasspathScanMode>("classpath.scan.mode");
    public static final KernelOption<DependencyInjectionMode> DEPENDENCY_INJECTION_MODE = new KernelOption<DependencyInjectionMode>("dependency.injection.mode");

    private final Map<String, Object> options = new HashMap<String, Object>();

    public KernelOptions()
    {
        set(ROOT_PACKAGES, new ArrayList<String>());
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
}
