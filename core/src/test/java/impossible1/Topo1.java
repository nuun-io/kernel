package impossible1;

import com.google.inject.name.Named;

import io.nuun.kernel.api.annotations.Topology;

/*
 * this topology is accessible only via META-INF/scan
 */

@Topology()
public interface Topo1 {
    
    @Named("topo1")
    String  marker   = "topo1";    

}
