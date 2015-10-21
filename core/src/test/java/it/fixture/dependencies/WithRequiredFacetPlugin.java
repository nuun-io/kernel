package it.fixture.dependencies;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class WithRequiredFacetPlugin extends AbstractPlugin {

    public static final String NAME = "with-required-deps";

    @Override
    public String name() {
        return NAME;
    }


    @Override
    public InitState init(InitContext initContext) {

        Facet1 facet1 = initContext.dependency(Facet1.class);

        if (!facet1.isInitialized()) {
            throw new IllegalStateException("Facet1 should be initialized before WithRequiredFacetPlugin");
        }

        List<?> dependencies = initContext.dependencies();
        if (dependencies.size() != 1) {
            throw new IllegalStateException("WithRequiredFacetPlugin should have only one dependency");
        }

        List<Facet1> facet1s = initContext.dependencies(Facet1.class);
        if (facet1s.size() != 1) {
            throw new IllegalStateException("WithRequiredFacetPlugin should have only one dependency");
        }

        return InitState.INITIALIZED;
    }
    @Override
    public Collection<Class<?>> requiredPlugins() {
        return Lists.<Class<?>>newArrayList(Facet1.class);
    }
}
