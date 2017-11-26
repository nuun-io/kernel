package io.nuun.kernel.core.test_topo;

public class MyCommand2Impl2 implements MyCommand2
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
