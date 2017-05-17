package io.nuun.kernel.core.test_topo.sample;

import javax.inject.Provider;

public class MyService2Provider implements Provider<MyService2>
{

    @Override
    public MyService2 get()
    {
        
        return new MyService2()
        {
        };
    }

}
