/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        List<Installer> installers = new ArrayList<>();
        for (UnitModule unitModule : unitModules)
        {
            installers.add(new UnitModuleInstaller(unitModule));
        }
        return installers;
    }

    List<Installer> createFromClasses(Collection<Class<?>> classes) {
        List<Installer> installerList = new ArrayList<>();
        for (Class<?> aClass : classes)
        {
            installerList.add(new ClassInstaller(aClass, classesWithScopes.get(aClass)));
        }
        return installerList;
    }
}
