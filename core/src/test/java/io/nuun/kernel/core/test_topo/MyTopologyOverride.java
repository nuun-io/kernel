package io.nuun.kernel.core.test_topo;

import io.nuun.kernel.api.annotations.Topology;
import io.nuun.kernel.core.test_topo.sample.MyService;
import io.nuun.kernel.core.test_topo.sample.MyServiceImplOver;

@Topology(overriding = true)
public interface MyTopologyOverride
{
    Integer theAnswer = 42;

    MyServiceImplOver injects(MyService key);
}