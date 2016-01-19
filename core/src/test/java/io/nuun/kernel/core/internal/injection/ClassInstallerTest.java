package io.nuun.kernel.core.internal.injection;

import com.google.inject.Binder;
import com.google.inject.Scopes;
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

import static io.nuun.kernel.spi.Concern.Priority.HIGHER;
import static io.nuun.kernel.spi.Concern.Priority.LOWER;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JMockit.class)
public class ClassInstallerTest
{

    private ClassInstaller greaterInstaller;
    private ClassInstaller lowerInstaller;

    @Before
    public void setUp() throws Exception
    {
        greaterInstaller = new ClassInstaller(ClassToBind.class, Scopes.SINGLETON);
        lowerInstaller = new ClassInstaller(ClassToBind2.class);
    }

    @Test
    public void testBindClassWithScope(@Mocked final Binder binder) throws Exception
    {
        new Expectations() {{
           binder.bind(ClassToBind.class).in(Scopes.SINGLETON);
        }};
        greaterInstaller.install(binder);
    }

    @Test
    public void testBindClass(@Mocked final Binder binder) throws Exception
    {
        new Expectations() {{
            binder.bind(ClassToBind2.class);
        }};
        lowerInstaller.install(binder);
    }

    @Test
    public void testInstallerOrder() {
        assertThat(greaterInstaller.order()).isEqualTo(HIGHER.value() + 20);
        assertThat(lowerInstaller.order()).isEqualTo(LOWER.value() - 20);
    }

    @Test
    public void testComparison() {
        assertThat(greaterInstaller).isGreaterThan(lowerInstaller);
    }

    @Concern(name="higher" , priority= HIGHER, order = 20)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    public @interface HigherConcern
    {
    }

    @HigherConcern
    static class ClassToBind {
    }

    @Concern(name="lower" , priority= LOWER, order = -20)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE})
    public @interface LowerConcern
    {
    }

    @LowerConcern
    static class ClassToBind2 {
    }
}