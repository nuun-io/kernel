package io.nuun.kernel.core.error1_test_topo;

import io.nuun.kernel.api.annotations.Topology;
import io.nuun.kernel.core.test_topo.MethodPredicate;
import io.nuun.kernel.core.test_topo.sample.MyMethodInterceptor;

import java.util.function.Predicate;

@Topology(propertiesPath = "topology.properties")
public interface TopologyError
{

    MyMethodInterceptor intercepts(Predicate<Class> pc, MethodPredicate pm);

}
