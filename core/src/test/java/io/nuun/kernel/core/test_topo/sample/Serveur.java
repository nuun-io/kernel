package io.nuun.kernel.core.test_topo.sample;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

import com.google.inject.BindingAnnotation;


@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@Documented
public @interface Serveur
{

}
