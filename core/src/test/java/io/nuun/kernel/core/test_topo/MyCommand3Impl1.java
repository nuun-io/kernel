package io.nuun.kernel.core.test_topo;

public class MyCommand3Impl1 implements MyCommand3
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
