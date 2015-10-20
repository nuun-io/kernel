package it.fixture;

import io.nuun.kernel.core.AbstractPlugin;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class NoOpPlugin2 extends AbstractPlugin {

    public static final String NAME = "no-op2";

    @Override
    public String name() {
        return NAME;
    }
}
