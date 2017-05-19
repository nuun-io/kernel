package io.nuun.kernel.core.internal.topology;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;



public class TopologyDefinitionCoreTest {
    
    TopologyDefinitionCore underTest;
    
    
    static class MyProvider implements Provider<String> {

        @Override
        public String get() {
            return "yay";
        }
        
    }
    
    
    @Before
    public void init() 
    {
        underTest = new TopologyDefinitionCore();
    }
    
    @Test
    public void assertProviderOf_should_check() throws Exception 
    {
        Object returnee = Whitebox.invokeMethod(underTest, "assertProviderOf" , Provider.class , MyProvider.class );
        
        assertThat(returnee).isNotNull();
        assertThat(returnee).isInstanceOf(Class.class);
        assertThat(returnee).isEqualTo(String.class);
        
        
    }

}
