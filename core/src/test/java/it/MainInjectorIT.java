package it;

import com.google.inject.Injector;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.core.NuunCore;
import it.fixture.injection.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Tests the main injector built by the kernel.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class MainInjectorIT {

    /**
     * Tests that the interface bound by the two plugins are really bound.
     */
    @Test
    public void test_main_injector() {
        KernelConfiguration kernelConfig = NuunCore.newKernelConfiguration()
                .withoutSpiPluginsLoader()
                .addPlugin(InjectionPlugin1.class)
                .addPlugin(InjectionPlugin2.class);

        Kernel kernel = NuunCore.createKernel(kernelConfig);
        kernel.init();
        kernel.start();
        Injector injector = kernel.objectGraph().as(Injector.class);

        InjectableInterface1 injectableInterface1 = injector.getInstance(InjectableInterface1.class);
        Assertions.assertThat(injectableInterface1).isNotNull();
        Assertions.assertThat(injectableInterface1).isInstanceOf(InjecteeImplementation1.class);

        InjectableInterface2 injectableInterface2 = injector.getInstance(InjectableInterface2.class);
        Assertions.assertThat(injectableInterface2).isNotNull();
        Assertions.assertThat(injectableInterface2).isInstanceOf(InjecteeImplementation2.class);
    }
}
