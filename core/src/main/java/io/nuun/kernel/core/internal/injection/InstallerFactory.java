package io.nuun.kernel.core.internal.injection;

import io.nuun.kernel.api.di.UnitModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class InstallerFactory
{
    private final Map<Class<?>, Object> classesWithScopes;

    public InstallerFactory(Map<Class<?>, Object> classesWithScopes)
    {
        this.classesWithScopes = classesWithScopes;
    }

    List<Installer> createFromUnitModules(Collection<UnitModule> unitModules) {
        List<Installer> installers = new ArrayList<Installer>();
        for (UnitModule unitModule : unitModules)
        {
            installers.add(new UnitModuleInstaller(unitModule));
        }
        return installers;
    }

    List<Installer> createFromClasses(Collection<Class<?>> classes) {
        List<Installer> installerList = new ArrayList<Installer>();
        for (Class<?> aClass : classes)
        {
            installerList.add(new ClassInstaller(aClass, classesWithScopes.get(aClass)));
        }
        return installerList;
    }
}
