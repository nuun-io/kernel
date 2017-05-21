package io.nuun.kernel.core.test_topo.sample;

import javax.inject.Provider;

public class MyService2ProviderBis implements Provider<MyService2>
{

    @Override
    public MyService2 get()
    {

        return new MyServiceImpl2Bis();

    }

}
