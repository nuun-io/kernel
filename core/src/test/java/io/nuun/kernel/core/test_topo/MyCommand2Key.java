package io.nuun.kernel.core.test_topo;

import java.util.function.Function;

public class MyCommand2Key implements Function<MyCommand2, String>{

    @Override
    public String apply(MyCommand2 t) {
        return t.name();
    }

}
