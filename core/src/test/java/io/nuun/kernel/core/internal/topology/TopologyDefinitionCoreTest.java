package io.nuun.kernel.core.internal.topology;

import javax.inject.Provider;

import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.nuun.kernel.core.KernelException;



public class TopologyDefinitionCoreTest {
    
    TopologyDefinitionCore underTest;
    
    
    static class Jsr330Provider implements Provider<String> 
    {

        @Override
        public String get() {
            return "yay";
        }
        
    }
    
    static class GoogleProvider implements com.google.inject.Provider<Long>
    {
        @Override
        public Long get() {
            return 42l;
        }
    }
    
    @Before
    public void init() 
    {
        underTest = new TopologyDefinitionCore();
    }
    
    @Test
    public void assertProviderOf_should_check_JSR330_Provider() throws Exception 
    {
        Whitebox.invokeMethod(underTest, "assertProviderOf" , Long.class , GoogleProvider.class );
    }

    @Test
    public void assertProviderOf_should_check_Google_Provider() throws Exception 
    {
        Whitebox.invokeMethod(underTest, "assertProviderOf" , String.class , Jsr330Provider.class );
    }

    @Test
    public void assertProviderOf_should_raise_error_JSR330_Provider() throws Exception 
    {
        try 
        {
            Whitebox.invokeMethod(underTest, "assertProviderOf" , String.class , GoogleProvider.class );
            Fail.failBecauseExceptionWasNotThrown(KernelException.class);
        } catch (KernelException kernelException) {}
        
    }

    @Test
    public void assertProviderOf_should_raise_error_Google_Provider() throws Exception 
    {
        try {
            Whitebox.invokeMethod(underTest, "assertProviderOf" , Long.class , Jsr330Provider.class );
            Fail.failBecauseExceptionWasNotThrown(KernelException.class);
        } catch (KernelException kernelException) {}
    }
    
}
