/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
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
package io.nuun.kernel.api.plugin.request.annotations;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.RoundEnvironment;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.BindingRequestBuilder;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequestBuilder;
import io.nuun.kernel.spi.DependencyInjectionProvider;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author epo.jemba@kametic.com
 */
public abstract class TestPlugin implements Plugin
{
    Logger                      logger           = LoggerFactory.getLogger(TestPlugin.class);

    Context                     context          = null;
    Object                      containerContext = null;
    RoundEnvironment            roundEnvironment = null;
    Map<String, String>         kernelParams     = null;
    InitContext                 initContext      = null;
    KernelParamsRequestBuilder  paramsBuilder    = null;
    ClasspathScanRequestBuilder scanBuilder      = null;
    BindingRequestBuilder       bindingBuilder   = null;

    /**
     * 
     */
    public TestPlugin()
    {
        paramsBuilder = new KernelParamsRequestBuilder();
        scanBuilder = new ClasspathScanRequestBuilder();
        bindingBuilder = new BindingRequestBuilder();
    }

    /**
     * ============================= PLUGIN LIFE CYCLE USED BY KERNEL =============================
     * 
     * @return
     **/

    @Override
    public InitState init(InitContext initContext)
    {
        this.initContext = initContext;

        return InitState.INITIALIZED;
    }

    @Override
    public void stop()
    {
    }

    @Override
    public void start(Context context)
    {
        this.context = context;
    }

    @Override
    public void destroy()
    {
    }

    // /**
    // * ============================= PLUGIN Utilities Helpers =============================
    // *
    // *
    // **/

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
    public Collection<Class<? extends Plugin>> requiredPlugins()
    {
        return Collections.emptySet();
    }

    @Override
    public Collection<Class<? extends Plugin>> dependentPlugins()
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
    public UnitModule unitModule()
    {
        return null;
    }

    @Override
    public UnitModule overridingUnitModule()
    {
        return null;
    }

    @Override
    public void provideContainerContext(Object containerContext)
    {
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
    public void provideRoundEnvironment(RoundEnvironment roundEnvironment)
    {
        this.roundEnvironment = roundEnvironment;

    }

    @Override
    public Map<String, String> kernelParametersAliases()
    {
        return new HashMap<String, String>();
    }

    protected Collection<Class<? extends Plugin>> collectionOf(Class<? extends Plugin>... items)
    {
        return Arrays.asList(items);
    }

    protected UnitModule unitModule(final Object module)
    {
        return new UnitModule()
        {

            @Override
            public Object nativeModule()
            {
                return module;
            }

            @Override
            public <T> T as(Class<T> targetType)
            {
                return targetType.cast(module);
            }
        };
    }
}
