package it.fixture;

import io.nuun.kernel.core.AbstractPlugin;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class NoOpPlugin1 extends AbstractPlugin {

    public static final String NAME = "no-op1";

    @Override
    public String name() {
        return NAME;
    }
}
