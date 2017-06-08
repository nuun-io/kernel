package impossible3;

import com.google.inject.name.Named;

import io.nuun.kernel.api.annotations.Topology;

/*
 * this topology is not accessible.
 * no injection with String,"topo3" should work
 */

@Topology()
public interface Topo3 {
    
    @Named("topo3")
    String  marker   = "topo3";    
}

