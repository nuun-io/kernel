package it.fixture.dependencies;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;

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

        for (Object plugin : initContext.pluginsRequired()) {
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
    public Collection<Class<?>> dependentPlugins() {
        return Lists.<Class<?>>newArrayList(DependentPlugin1.class);
    }
}
