package io.nuun.kernel.core.test_topo;

public class MyCommand1Impl2 implements MyCommand1
{

    @Override
    public String name()
    {
        return this.getClass().getName();
    }

    @Override
    public void execute(String... args)
    {

    }

}
