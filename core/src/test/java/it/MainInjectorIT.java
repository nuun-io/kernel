package it;

import com.google.inject.Injector;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.core.internal.Fixture;
import it.fixture.injection.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        Kernel kernel = Fixture.startKernel(Fixture.config()
                .addPlugin(InjectionPlugin1.class)
                .addPlugin(InjectionPlugin2.class));

        Injector injector = kernel.objectGraph().as(Injector.class);

        InjectableInterface1 injectableInterface1 = injector.getInstance(InjectableInterface1.class);
        assertThat(injectableInterface1).isNotNull();
        assertThat(injectableInterface1).isInstanceOf(InjecteeImplementation1.class);

        InjectableInterface2 injectableInterface2 = injector.getInstance(InjectableInterface2.class);
        assertThat(injectableInterface2).isNotNull();
        assertThat(injectableInterface2).isInstanceOf(InjecteeImplementation2.class);
    }
}
