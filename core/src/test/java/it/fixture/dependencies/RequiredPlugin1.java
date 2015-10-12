package it.fixture.dependencies;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class RequiredPlugin1 extends AbstractPlugin {

    public static final String NAME = "required-plugin-1";

    private boolean initialized = false;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public InitState init(InitContext initContext) {
        initialized = true;
        return InitState.INITIALIZED;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
