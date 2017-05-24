package io.nuun.kernel.core.test_topo_error1;

import java.util.function.Predicate;

import io.nuun.kernel.api.annotations.Topology;
import io.nuun.kernel.core.test_topo.MethodPredicate;
import io.nuun.kernel.core.test_topo.sample.MyMethodInterceptor;

@Topology(properties = "topology.properties")
public interface TopologyError {
    
    MyMethodInterceptor intercepts(Predicate<Class> pc, MethodPredicate pm);

}
