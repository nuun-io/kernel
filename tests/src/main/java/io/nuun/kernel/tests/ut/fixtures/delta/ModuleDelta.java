package io.nuun.kernel.tests.ut.fixtures.delta;

import com.google.inject.spi.Element;

public interface ModuleDelta
{

    public ElementDelta elementDelta(Class<? extends Element> elementClass);

}