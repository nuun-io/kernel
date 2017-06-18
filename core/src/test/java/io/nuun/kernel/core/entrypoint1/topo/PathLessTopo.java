package io.nuun.kernel.core.entrypoint1.topo;

import io.nuun.kernel.api.annotations.Topology;

import javax.inject.Named;

@Topology
public interface PathLessTopo
{
    @Named("fromPathLessTopo")
    String message = "!";
}