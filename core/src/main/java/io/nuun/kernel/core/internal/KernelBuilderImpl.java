package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.config.DependencyInjectionMode;

class KernelBuilderImpl implements KernelBuilder.KernelBuilderWithPluginAndContext
{

    private KernelCore kernel;

    /**
     *
     */
    public KernelBuilderImpl(String... keyValues)
    {

    }

    @Override
    public Kernel build()
    {
        return null;
    }

    @Override
    public KernelBuilderWithPlugins withContainerContext(Object containerContext)
    {

        kernel.addContainerContext(containerContext);
        return this;

    }

    @Override
    public KernelBuilderWithPluginAndContext withPlugins(Class<? extends Plugin>... klass)
    {
        kernel.addPlugins(klass);
        return this;
    }

    @Override
    public KernelBuilderWithPluginAndContext withPlugins(Plugin... plugin)
    {
        kernel.addPlugins(plugin);
        return this;
    }

    @Override
    public KernelBuilderWithPluginAndContext withoutSpiPluginsLoader()
    {
        kernel.spiPluginDisabled();
        return this;
    }

    @Override
    public KernelBuilderWithPluginAndContext withDependencyInjectionMode(DependencyInjectionMode dependencyInjectionMode)
    {
        kernel.dependencyInjectionMode(dependencyInjectionMode);
        return this;
    }

    @Override
    public KernelBuilderWithPluginAndContext withClasspathScanMode(ClasspathScanMode classpathScanMode, Object scanConfigurationObject)
    {
        kernel.classpathScanMode(classpathScanMode);
        return this;
    }

}
