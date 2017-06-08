package impossible2;

import com.google.inject.name.Named;

import io.nuun.kernel.api.annotations.Topology;

/*
 * this topology is accessible only via META-INF/scan
 */

@Topology()
public interface Topo2 {
    
    @Named("topo2")
    String  marker   = "topo2";    
}

