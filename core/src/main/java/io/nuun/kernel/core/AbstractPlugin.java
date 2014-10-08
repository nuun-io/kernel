/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
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
/**
 * 
 */
package io.nuun.kernel.core;

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
import io.nuun.kernel.api.plugin.request.builders.BindingRequestBuilderMain;
import io.nuun.kernel.core.internal.ModuleEmbedded;
import io.nuun.kernel.spi.DependencyInjectionProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kametic.specifications.AbstractSpecification;
import org.kametic.specifications.AndSpecification;
import org.kametic.specifications.NotSpecification;
import org.kametic.specifications.OrSpecification;
import org.kametic.specifications.Specification;
import org.kametic.specifications.reflect.ClassMethodsAnnotatedWith;
import org.kametic.specifications.reflect.DescendantOfSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Epo Jemba
 */
public abstract class AbstractPlugin implements Plugin
{

    
    Logger logger = LoggerFactory.getLogger(AbstractPlugin.class);
    
    
    protected Context                         context = null;
    protected Object                          containerContext = null;
    protected RoundEnvironment roundEnvironment;
    @SuppressWarnings("unused")
    private Map<String, String>               kernelParams;
    @SuppressWarnings("unused")
    private InitContext                       initContext;
    private final KernelParamsRequestBuilder  paramsBuilder;
    private final ClasspathScanRequestBuilder scanBuilder;
    private final BindingRequestBuilder       bindingBuilder;

    /**
     * 
     */
    public AbstractPlugin()
    {
        paramsBuilder = new KernelParamsRequestBuilder();
        scanBuilder = new ClasspathScanRequestBuilder();
        bindingBuilder = new BindingRequestBuilder();
    }

    /**
     * ============================= PLUGIN LIFE CYCLE USED BY KERNEL =============================
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

    protected KernelParamsRequestBuilder kernelParamsRequestBuilder()
    {
        paramsBuilder.reset();
        return paramsBuilder;
    }

    protected ClasspathScanRequestBuilder classpathScanRequestBuilder()
    {
        scanBuilder.reset();
        return scanBuilder;
    }

    protected BindingRequestBuilderMain bindingRequestsBuilder()
    {
        bindingBuilder.reset();
        return bindingBuilder;
    }

    protected Specification<Class<?>> or(Specification<Class<?>>... participants)
    {
        return new OrSpecification<Class<?>>(participants);
    }

    protected Specification<Class<?>> and(Specification<Class<?>>... participants)
    {
        return new AndSpecification<Class<?>>(participants);
    }

    protected Specification<Class<?>> not(Specification<Class<?>> participant)
    {
        return new NotSpecification<Class<?>>(participant);
    }

    protected Specification<Class<?>> descendantOf(Class<?> ancestor)
    {
        return new DescendantOfSpecification(ancestor);
    }
    
    
    protected Specification<Class<?>> classMethodsAnnotatedWith (final Class<? extends Annotation> annotationClass)
    {
    	return new ClassMethodsAnnotatedWith(annotationClass);
    }
    protected Specification<Class<?>> fieldAnnotatedWith (final Class<? extends Annotation> annotationClass)
    {
    	return new AbstractSpecification<Class<?>> ()
    	{
    		@Override
    		public boolean isSatisfiedBy(Class<?> candidate) {
    			
    			if (candidate != null)
    			{
    			    try
                    {
    			        for (Field field : candidate.getDeclaredFields())
    			        {
    			            if (   field.isAnnotationPresent(annotationClass)   )
    			            {
    			                return true;
    			            }
    			        }
                    }
                    catch (Throwable throwable)
                    {
                        logger.debug("fieldAnnotatedWith : " +candidate +  " missing " + throwable );
                    }
    			}
    			
    			return false;
    		}
    	};
    }

    protected Specification<Class<?>> classAnnotatedWith(final Class<? extends Annotation> klass)
    {
        return new AbstractSpecification<Class<?>>()
        {
            @Override
            public boolean isSatisfiedBy(Class<?> candidate)
            {
                return candidate != null && candidate.getAnnotation(klass) != null;
            }
        };
    }

    // TODO replace this implementation by the one in ClassMethodsAnnotatedWithSpecification
    protected Specification<Class<?>> classImplements(final Class<?> klass)
    {
        return new AbstractSpecification<Class<?>>()
        {
            @Override
            public boolean isSatisfiedBy(Class<?> candidate)
            {
                if (candidate != null && klass.isInterface())
                {
                    for (Class<?> i : candidate.getInterfaces())
                    {
                        if (i.equals(klass))
                        {
                            return true;
                        }
                    }
                }

                return false;
            }
        };
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
    public Object dependencyInjectionDef()
    {
        return null;
    }
    
    @Override
    public Object dependencyInjectionOverridingDef()
    {
        return null;
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
    
    protected UnitModule unitModule(Object module)
    {
        return new ModuleEmbedded(module);
    }

}
