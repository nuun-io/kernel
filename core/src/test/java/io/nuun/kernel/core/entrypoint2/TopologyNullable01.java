package io.nuun.kernel.core.entrypoint2;

import io.nuun.kernel.api.annotations.Topology;

import javax.annotation.Nullable;

@Topology
public interface TopologyNullable01
{
    @Nullable
    NullableService nullableService = null;

}
