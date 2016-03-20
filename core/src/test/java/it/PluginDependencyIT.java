package it;

import io.nuun.kernel.core.KernelException;
import it.fixture.dependencies.*;
import org.junit.Test;

import static io.nuun.kernel.core.internal.Fixture.config;
import static io.nuun.kernel.core.internal.Fixture.initKernel;

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
        initKernel(config()
                .addPlugin(WithDependentDepsPlugin.class)
                .addPlugin(DependentPlugin1.class));
    }

    /**
     * The kernel should throws an exception if the dependent plugin is not present.
     */
    @Test(expected = KernelException.class)
    public void test_dependent_plugin_with_missing_dependency() {
        initKernel(config().addPlugin(WithDependentDepsPlugin.class));
    }

    /**
     * WithRequiredDepsPlugin throws an exception if RequiredPlugin1 is initialized after it.
     */
    @Test
    public void test_required_plugin() {
        initKernel(config()
                .addPlugin(WithRequiredDepsPlugin.class)
                .addPlugin(RequiredPlugin1.class));
    }

    /**
     * The kernel should throws an exception if the required plugin is not present.
     */
    @Test(expected = KernelException.class)
    public void test_required_plugin_with_missing_dependency() {
        initKernel(config().addPlugin(WithRequiredDepsPlugin.class));
    }

    /**
     * WithDependentDepsPlugin throws an exception if DependentPlugin1 is initialized before it.
     */
    @Test
    public void test_required_facet() {
        initKernel(config()
                .addPlugin(Facet1Plugin.class)
                .addPlugin(WithRequiredFacetPlugin.class));
    }
}
