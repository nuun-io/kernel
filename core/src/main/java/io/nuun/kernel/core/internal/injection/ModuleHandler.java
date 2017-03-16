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

import com.google.common.collect.Maps;
import com.google.inject.Module;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.di.ModuleValidation;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.KernelConfigurationInternal;
import io.nuun.kernel.core.internal.RequestHandler;
import io.nuun.kernel.spi.DependencyInjectionProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ModuleHandler
{
    private final KernelConfigurationInternal kernelConfig;
    private final Collection<DependencyInjectionProvider> dependencyInjectionProviders = new ArrayList<>();

    private final Map<Class<? extends Plugin>, UnitModule> unitModules = Maps.newConcurrentMap();
    private final Map<Class<? extends Plugin>, UnitModule> overridingUnitModules = Maps.newConcurrentMap();
    private final Map<Class<? extends Plugin>, UnitModule> nonGuiceUnitModules = Maps.newConcurrentMap();
    private final Map<Class<? extends Plugin>, UnitModule> nonGuiceOverridingUnitModules = Maps.newConcurrentMap();

    public ModuleHandler(KernelConfigurationInternal kernelConfig)
    {
        this.kernelConfig = kernelConfig;
    }

    public void addDependencyInjectionProvider(DependencyInjectionProvider dependencyInjectionProvider)
    {
        dependencyInjectionProviders.add(dependencyInjectionProvider);
    }

    public void handleUnitModule(RequestHandler requestHandler, Plugin plugin)
    {
        UnitModule unitModule = plugin.unitModule();
        if (unitModule == null || unitModule.nativeModule() == null)
        {
            return;
        }

        // Conversion of native module if needed
        if (isNotGuiceModule(unitModule))
        {
            // we also keep a trace on non guice unit module in case.
            nonGuiceUnitModules.put(plugin.getClass(), unitModule);
            // Convert to Guice Module which is the internal DI engine
            unitModule = convertToNativeModule(plugin.name(), unitModule.nativeModule());
        }

        validateUnitModule(unitModule);

        requestHandler.addChildModule(ModuleEmbedded.wrap(Module.class.cast(unitModule.nativeModule())));
        unitModules.put(plugin.getClass(), unitModule);
    }

    private boolean isNotGuiceModule(UnitModule unitModule)
    {
        return !Module.class.isAssignableFrom(unitModule.nativeModule().getClass());
    }

    private UnitModule convertToNativeModule(String pluginName, Object nativeUnitModule)
    {
        DependencyInjectionProvider provider = findDependencyInjectionProvider(nativeUnitModule);
        if (provider != null)
        {
            return provider.convert(nativeUnitModule);
        } else
        {
            throw new KernelException("Missing DependencyInjectionProvider for module %s of plugin %s.",
                    nativeUnitModule.getClass().getCanonicalName(), pluginName);
        }
    }

    private DependencyInjectionProvider findDependencyInjectionProvider(Object nativeUnitModule)
    {
        for (DependencyInjectionProvider diProvider : dependencyInjectionProviders)
        {
            if (diProvider.canHandle(nativeUnitModule.getClass()))
            {
                return diProvider;
            }
        }
        return null;
    }

    private void validateUnitModule(UnitModule unitModule)
    {
        for (ModuleValidation validation : kernelConfig.getValidations())
        {
            if (validation.canHandle(unitModule.nativeModule().getClass()))
            {
                try
                {
                    validation.validate(unitModule);
                } catch (Exception validationException)
                {
                    throw new KernelException("Error when validating di definition " + unitModule, validationException);
                }
            }
        }
    }

    public void handleOverridingUnitModule(RequestHandler requestHandler, Plugin plugin)
    {
        UnitModule unitModule = plugin.overridingUnitModule();
        if (unitModule == null || unitModule.nativeModule() == null)
        {
            return;
        }

        // Conversion of native module if needed
        if (isNotGuiceModule(unitModule))
        {
            // we also keep a trace on non guice unit module in case.
            nonGuiceOverridingUnitModules.put(plugin.getClass(), unitModule);

            unitModule = convertToNativeModule(plugin.name(), unitModule.nativeModule());
        }

        validateUnitModule(unitModule);

        requestHandler.addChildOverridingModule(ModuleEmbedded.wrap(Module.class.cast(unitModule.nativeModule())));
        overridingUnitModules.put(plugin.getClass(), unitModule);
    }

    public Map<Class<? extends Plugin>, UnitModule> getUnitModules()
    {
        return unitModules;
    }

    public Map<Class<? extends Plugin>, UnitModule> getOverridingUnitModules()
    {
        return overridingUnitModules;
    }

    public Map<Class<? extends Plugin>, UnitModule> getNonGuiceUnitModules()
    {
        return nonGuiceUnitModules;
    }

    public Map<Class<? extends Plugin>, UnitModule> getNonGuiceOverridingUnitModules()
    {
        return nonGuiceOverridingUnitModules;
    }
}
