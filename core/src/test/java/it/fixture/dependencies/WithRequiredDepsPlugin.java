package it.fixture.dependencies;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;

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

        for (Object plugin : initContext.pluginsRequired()) {
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
    public Collection<Class<?>> requiredPlugins() {
        return Lists.<Class<?>>newArrayList(RequiredPlugin1.class);
    }

}
