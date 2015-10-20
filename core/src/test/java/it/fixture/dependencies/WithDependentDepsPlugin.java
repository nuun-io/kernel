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
public class WithDependentDepsPlugin extends AbstractPlugin {

    public static final String NAME = "with-dependent-deps";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public InitState init(InitContext initContext) {
        boolean isInitialized = false;

        for (Plugin plugin : initContext.pluginsRequired()) {
            if (plugin instanceof DependentPlugin1){
                isInitialized = ((DependentPlugin1) plugin).isInitialized();
            }
        }

        if (isInitialized) {
            throw new IllegalStateException("DependentPlugin1 should not be initialized before WithDependentDepsPlugin");
        }

        return InitState.INITIALIZED;
    }

    @Override
    public Collection<Class<? extends Plugin>> dependentPlugins() {
        List<Class<? extends Plugin>> dependents = new ArrayList<Class<? extends Plugin>>();
        dependents.add(DependentPlugin1.class);
        return dependents;
    }
}
