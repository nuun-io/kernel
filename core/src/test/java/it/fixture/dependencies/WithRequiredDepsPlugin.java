package it.fixture.dependencies;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class WithRequiredDepsPlugin extends AbstractPlugin {

    public static final String NAME = "with-required-deps";

    @Override
    public String name() {
        return NAME;
    }


    @Override
    public InitState init(InitContext initContext) {
        boolean isInitialized = false;

        for (Plugin plugin : initContext.pluginsRequired()) {
            if (plugin instanceof RequiredPlugin1){
                isInitialized = ((RequiredPlugin1) plugin).isInitialized();
            }
        }

        if (!isInitialized) {
            throw new IllegalStateException("RequiredPlugin1 should not be initialized before WithRequiredDepsPlugin");
        }

        return InitState.INITIALIZED;
    }
    @Override
    public Collection<Class<? extends Plugin>> requiredPlugins() {
        List<Class<? extends Plugin>> dependents = new ArrayList<Class<? extends Plugin>>();
        dependents.add(RequiredPlugin1.class);
        return dependents;
    }

}
