package it;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.NuunCore;
import it.fixture.dependencies.*;
import org.junit.Test;

/**
 * Tests dependencies between plugins.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class PluginDependencyIT {

    /**
     * WithDependentDepsPlugin throws an exception if DependentPlugin1 is initialized before it.
     */
    @Test
    public void test_dependent_plugin() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .addPlugin(WithDependentDepsPlugin.class)
                .addPlugin(DependentPlugin1.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();
    }

    /**
     * The kernel should throws an exception if the dependent plugin is not present.
     */
    @Test(expected = KernelException.class)
    public void test_dependent_plugin_with_missing_dependency() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .addPlugin(WithDependentDepsPlugin.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();
    }

    /**
     * WithRequiredDepsPlugin throws an exception if RequiredPlugin1 is initialized after it.
     */
    @Test
    public void test_required_plugin() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .addPlugin(WithRequiredDepsPlugin.class)
                .addPlugin(RequiredPlugin1.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();
    }

    /**
     * The kernel should throws an exception if the required plugin is not present.
     */
    @Test(expected = KernelException.class)
    public void test_required_plugin_with_missing_dependency() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .addPlugin(WithRequiredDepsPlugin.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();
    }

    /**
     * WithDependentDepsPlugin throws an exception if DependentPlugin1 is initialized before it.
     */
    @Test
    public void test_required_facet() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .addPlugin(Facet1Plugin.class)
                .addPlugin(WithRequiredFacetPlugin.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();
    }
}
