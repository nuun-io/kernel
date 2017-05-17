package io.nuun.kernel.core.test_topo.sample;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;


@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Documented
public @interface Server
{

}
