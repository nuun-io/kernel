package io.nuun.kernel.core.test_topo;

public class MyCommand4Impl2 implements MyCommand4
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
