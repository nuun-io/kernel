package io.nuun.kernel.api.plugin.request.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RequestDefinition
@Retention(RetentionPolicy.RUNTIME)
public @interface Request
{
    Specs value();
}