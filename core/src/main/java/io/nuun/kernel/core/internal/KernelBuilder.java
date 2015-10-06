package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.config.DependencyInjectionMode;

public interface KernelBuilder
{

    Kernel build();

    public static interface KernelBuilderWithPluginAndContext
            extends KernelBuilderWithContainerContext, KernelBuilderWithPlugins, KernelModeContext
    {
    }

    public static interface KernelModeContext extends KernelBuilder
    {
        KernelBuilderWithPluginAndContext withDependencyInjectionMode(DependencyInjectionMode dependencyInjectionMode);

        KernelBuilderWithPluginAndContext withClasspathScanMode(ClasspathScanMode classpathScanMode, Object scanConfigurationObject);
    }

    public static interface KernelBuilderWithContainerContext extends KernelBuilder
    {
        KernelBuilderWithPlugins withContainerContext(Object containerContext);
    }

    public static interface KernelBuilderWithPlugins extends KernelBuilder
    {
        KernelBuilderWithPluginAndContext withPlugins(Class<? extends Plugin>... aClass);

        KernelBuilderWithPluginAndContext withPlugins(Plugin... plugins);

        KernelBuilderWithPluginAndContext withoutSpiPluginsLoader();

    }
}
