package io.nuun.kernel.core.test_topo;

public class MyCommand1Impl1 implements MyCommand1
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
