package io.nuun.kernel.core.internal.injection;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.nuun.kernel.api.di.UnitModule;

public class UnitModuleInstaller extends Installer
{
    private UnitModule unitModule;

    public UnitModuleInstaller(UnitModule unitModule)
    {
        this.unitModule = unitModule;
    }

    @Override
    protected Class<?> getOriginalClass()
    {
        return unitModule.nativeModule().getClass();
    }

    @Override
    protected void install(Binder binder)
    {
        binder.install(unitModule.as(Module.class));
    }
}
