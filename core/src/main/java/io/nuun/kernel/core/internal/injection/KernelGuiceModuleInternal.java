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

import com.google.inject.AbstractModule;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.core.internal.ContextInternal;
import io.nuun.kernel.core.internal.RequestHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bootstrap the plugins needed to initialize the application.
 *
 * @author ejemba
 */
public class KernelGuiceModuleInternal extends AbstractModule
{
    private final RequestHandler requestHandler;
    private boolean overriding = false;

    public KernelGuiceModuleInternal(RequestHandler requestHandler)
    {
        this.requestHandler = requestHandler;
    }

    public KernelGuiceModuleInternal overriding()
    {
        overriding = true;
        return this;
    }

    @Override
    protected final void configure()
    {
        // All bindings will be needed explicitly. this simple line makes the framework bullet-proof !
        binder().requireExplicitBindings();

        bind(Context.class).to(ContextInternal.class);

        installModuleAndClassesInOrder();
    }

    private void installModuleAndClassesInOrder()
    {
        List<Installer> installers = getComparableConcerns();
        Collections.sort(installers, Collections.reverseOrder());
        for (Installer installer : installers)
        {
            installer.install(binder());
        }
    }

    private List<Installer> getComparableConcerns()
    {
        List<Installer> installers = new ArrayList<Installer>();
        InstallerFactory installerFactory = new InstallerFactory(requestHandler.getClassesWithScopes());
        if (!overriding)
        {
            installers.addAll(installerFactory.createFromClasses(requestHandler.getClassesToBind()));
            installers.addAll(installerFactory.createFromUnitModules(requestHandler.getModules()));
        } else
        {
            installers.addAll(installerFactory.createFromUnitModules(requestHandler.getOverridingModules()));
        }
        return installers;
    }
}
