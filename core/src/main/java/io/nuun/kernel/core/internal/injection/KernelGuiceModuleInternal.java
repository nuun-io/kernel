/**
 * Copyright (C) 2013-2014 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
