package io.nuun.kernel.core.internal.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import io.nuun.kernel.spi.Concern;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.nuun.kernel.spi.Concern.Priority.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JMockit.class)
public class ModuleInstallerTest
{

    private UnitModuleInstaller greaterInstaller;
    private UnitModuleInstaller lowerInstaller;
    private MyModule module;
    private MyModule2 module2;

    @Before
    public void setUp() throws Exception
    {
        module = new MyModule();
        greaterInstaller = new UnitModuleInstaller(ModuleEmbedded.wrap(module));
        module2 = new MyModule2();
        lowerInstaller = new UnitModuleInstaller(ModuleEmbedded.wrap(module2));
    }

    @Test
    public void testInstallModule(@Mocked final Binder binder) throws Exception
    {
        new Expectations() {{
           binder.install(module);
        }};
        greaterInstaller.install(binder);
    }

    @Test
    public void testInstallModule2(@Mocked final Binder binder) throws Exception
    {
        new Expectations() {{
            binder.install(module2);
        }};
        lowerInstaller.install(binder);
    }

    @Test
    public void testInstallerOrder() {
        assertThat(greaterInstaller.order()).isEqualTo(HIGH.value() + 20);
        assertThat(lowerInstaller.order()).isEqualTo(0);
    }

    @Test
    public void testComparison() {
        assertThat(greaterInstaller).isGreaterThan(lowerInstaller);
    }

    @HighConcern
    static class MyModule extends AbstractModule
    {
        @Override
        protected void configure()
        {

        }
    }

    @Concern(name="high" , priority= HIGH, order = 20)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    public @interface HighConcern
    {
    }

    static class MyModule2 extends AbstractModule
    {
        @Override
        protected void configure()
        {

        }
    }
}
