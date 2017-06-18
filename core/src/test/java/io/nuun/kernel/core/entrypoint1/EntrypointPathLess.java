package io.nuun.kernel.core.entrypoint1;

import io.nuun.kernel.api.annotations.Entrypoint;

import javax.inject.Inject;
import javax.inject.Named;

import org.assertj.core.api.Assertions;

@Entrypoint
public class EntrypointPathLess implements Runnable
{

    @Inject
    @Named("fromPathLessTopo")
    String message;

    @Override
    public void run()
    {
        Assertions.assertThat(message).isEqualTo("!");
    }

}
