package it.fixture.injection;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.nuun.kernel.core.AbstractPlugin;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class InjectionPlugin1 extends AbstractPlugin {

    public static final String NAME = "injection-plugin1";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Object nativeUnitModule() {
        return new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(InjectableInterface1.class).to(InjecteeImplementation1.class);
            }
        };
    }
}
