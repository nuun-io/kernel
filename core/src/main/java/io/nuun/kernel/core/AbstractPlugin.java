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
package io.nuun.kernel.core;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.Round;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.BindingRequestBuilder;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequestBuilder;
import io.nuun.kernel.api.plugin.request.builders.BindingRequestBuilderMain;
import io.nuun.kernel.core.internal.injection.ModuleEmbedded;
import io.nuun.kernel.spi.DependencyInjectionProvider;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Epo Jemba
 */
public abstract class AbstractPlugin implements Plugin
{
    protected Context context = null;
    protected Object containerContext = null;
    protected Round round;

    // ============================= PLUGIN LIFE CYCLE USED BY KERNEL =============================

    @Override
    public InitState init(InitContext initContext)
    {
        return InitState.INITIALIZED;
    }

    @Override
    public void start(Context context)
    {
        this.context = context;
    }

    @Override
    public void stop()
    {
    }

    // ============================= PLUGIN request builders =============================

    protected KernelParamsRequestBuilder kernelParamsRequestBuilder()
    {
        return new KernelParamsRequestBuilder();
    }

    protected ClasspathScanRequestBuilder classpathScanRequestBuilder()
    {
        return new ClasspathScanRequestBuilder();
    }

    protected BindingRequestBuilderMain bindingRequestsBuilder()
    {
        return new BindingRequestBuilder();
    }

    // * ============================= PLUGIN info and requests * ============================= //

    @Override
    public abstract String name();

    @Override
    public String description()
    {
        return name() + " Nuun Based Plugin.";
    }

    @Override
    public Collection<KernelParamsRequest> kernelParamsRequests()
    {
        return Collections.emptySet();
    }

    @Override
	public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        return Collections.emptySet();
    }

    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        return Collections.emptySet();
    }

    @Override
    public Collection<Class<?>> requiredPlugins()
    {
        return Collections.emptySet();
    }
    
    @Override
    public Collection<Class<?>> dependentPlugins()
    {
        return Collections.emptySet();
    }

    @Override
    public String pluginPropertiesPrefix()
    {
        return "";
    }

    @Override
    public String pluginPackageRoot()
    {
        return "";
    }

    @Override
    public String rootPackages()
    {
        return "";
    }

    @Override
    public UnitModule unitModule()
    {
        return nativeUnitModule() != null ?  new ModuleEmbedded(nativeUnitModule()) : null;
    }
    
    /**
     * Convenient method for plugin to return directly a native module rather than a UnitModule.
     * <p>
     * then {@link AbstractPlugin#unitModule()} will use {@link AbstractPlugin#nativeUnitModule()} to create the UnitModule.
     * 
     * @return the nativeModule.
     */
    public Object nativeUnitModule()
    {
        return null;
    }
    
    /**
     * Convenient method for plugin to return directly a native module rather than a UnitModule.
     * <p>
     * then {@link AbstractPlugin#unitModule()} will use {@link AbstractPlugin#nativeUnitModule()} to create the UnitModule.
     * 
     * @return the nativeModule for overring purpose.
     */
    public Object nativeOverridingUnitModule()
    {
        return null;
    }
    
    @Override
    public UnitModule overridingUnitModule()
    {
        return nativeOverridingUnitModule() != null ? new ModuleEmbedded(nativeOverridingUnitModule()) : null;
    }

    @Override
    public void provideContainerContext(Object containerContext){
    	this.containerContext = containerContext;
    }
    
    @Override
    public Set<URL> computeAdditionalClasspathScan()
    {
        return Collections.emptySet();
    }

    @Override
    public DependencyInjectionProvider dependencyInjectionProvider()
    {
        return null;
    }
    
    @Override
    public void provideRound(Round round)
    {
        this.round = round;
    }
    
    @Override
    public Map<String, String> kernelParametersAliases()
    {
        return new HashMap<>();
    }
    
    protected UnitModule unitModule(Object module)
    {
        return new ModuleEmbedded(module);
    }
}
