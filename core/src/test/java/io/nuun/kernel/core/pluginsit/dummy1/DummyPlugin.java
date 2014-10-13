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
/**
 * 
 */
package io.nuun.kernel.core.pluginsit.dummy1;

import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.di.UnitModule;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.core.internal.KernelCoreTest;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin2;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Epo Jemba
 * 
 */
public class DummyPlugin extends AbstractPlugin
{

    public  static final String ALIAS_DUMMY_PLUGIN1 = "alias.dummy.plugin1";

    public static final String NUUNROOTALIAS = "nuunrootalias";

    private Logger logger = LoggerFactory.getLogger(DummyPlugin.class);

    private com.google.inject.Module module;

    /**
     * 
     */
    public DummyPlugin()
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#name()
     */
    @Override
    public String name()
    {
        return "dummyPlugin";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#description()
     */
    @Override
    public String description()
    {
        return "description";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#propertiesPrefix()
     */
    @Override
    public String pluginPropertiesPrefix()
    {
        return "dummy-";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#packageRoot()
     */
    @Override
    public String pluginPackageRoot()
    {
        return  DummyPlugin.class.getPackage().getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#module()
     */
    @Override
    public Object nativeUnitModule()
    {
        return module;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#init()
     */
    @Override
    public InitState init(InitContext initContext)
    {
        String param = initContext.kernelParam("dummy.plugin1");
        assertThat(param).isNotEmpty();
        assertThat(param).isEqualTo("WAZAAAA");

        String param2 = initContext.kernelParam( Kernel.NUUN_ROOT_PACKAGE );
        assertThat(param2).isNotNull();
        assertThat(param2).isEqualTo("internal,"+KernelCoreTest.class.getPackage().getName());
        
        Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass = initContext.scannedClassesByAnnotationClass();
        
        Collection<Class<?>> cAnnotations1 = scannedClassesByAnnotationClass.get(MarkerSample4.class);
        assertThat(cAnnotations1).hasSize(1);

        Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass = initContext.scannedSubTypesByParentClass();
        Collection<Class<?>> cParent1 = scannedSubTypesByParentClass.get(DummyMarker.class);
        assertThat(cParent1).hasSize(1);

        Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex = initContext.scannedClassesByAnnotationRegex();
        Collection<Class<?>> cAnnotations2 = scannedClassesByAnnotationRegex.get(".*MarkerSample3");
        assertThat(cAnnotations2).hasSize(1);

        Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex = initContext.scannedSubTypesByParentRegex();
        Collection<Class<?>> cParent2 = scannedSubTypesByParentRegex.get(".*WithCustomSuffix");
        
        logger.info("c2 : " +cParent2.toString());
        assertThat(cParent2).hasSize(2);
        
        Map<String, Collection<Class<?>>> scannedTypesByRegex = initContext.scannedTypesByRegex();
        Collection<Class<?>> cParent3 = scannedTypesByRegex.get(".*WithCustomSuffix");

        Collection<Class<?>> klasses = new HashSet<Class<?>>();
        klasses.addAll(cParent3);
        klasses.addAll(cParent2);
        klasses.addAll(cParent1);
        klasses.addAll(cAnnotations2);
        klasses.addAll(cAnnotations1);

        module = new DummyModule(klasses);
        
        assertThat( initContext.pluginsRequired() ).isNotNull();
        assertThat( initContext.pluginsRequired() ).hasSize(1);
        assertThat( initContext.pluginsRequired().iterator().next().getClass() ).isEqualTo(DummyPlugin2.class);
        return InitState.INITIALIZED;

    }
    
    public static void main(String[] args)
    {
        String ee = "zerzerzer.eer.EpoWithCustomSuffix";
        if (ee.matches(".*WithCustomSuffix"))
        {
            System.out.println("OK");
        }
        else
        {
            System.out.println("PAS OK");
        }
    }
    
    
    /* (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#bindingRequests()
     */
    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        return bindingRequestsBuilder().subtypeOfRegex(".*WithCustom2Suffix").build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#classpathScanRequests()
     */
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        return classpathScanRequestBuilder() //
            .annotationRegex(".*MarkerSample3") //
            .annotationType(MarkerSample4.class) //
            .subtypeOf(DummyMarker.class) //
            .subtypeOfRegex(".*WithCustomSuffix") //
            .typeOfRegex(".*WithCustomSuffix") //
            .build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#requiredKernelParams()
     */
    @Override
    public Collection<KernelParamsRequest> kernelParamsRequests()
    {

        return kernelParamsRequestBuilder().mandatory("dummy.plugin1").build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#requiredPlugins()
     */
    @SuppressWarnings(
    {
        "unchecked"
    })
    @Override
    public Collection<Class<? extends Plugin>> requiredPlugins()
    {
        return (Collection<Class<? extends Plugin>>) collectionOf(DummyPlugin2.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#destroy()
     */
    @Override
    public void destroy()
    {
    }

    // public Collection<String> annotationNames()
    // {
    // return collectionOf("MarkerSample3");
    // }
    // public Collection<Class<? extends Annotation>> annotationTypes()
    // {
    // return (Collection) collectionOf(MarkerSample4.class);
    //
    // }
    // public Collection<Class<?>> parentTypes()
    // {
    // return (Collection) collectionOf(DummyMarker.class);
    // }
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see org.nuunframework.kernel.plugin.AbstractPlugin#typesNames()
    // */
    // @SuppressWarnings("unchecked")
    // @Override
    // public Collection<String> typesNames()
    // {
    // return (Collection<String>) collectionOf("WithCustomSuffix");
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.Plugin#start()
     */
    @Override
    public void start(Context context)
    {
        assertThat(context).isNotNull();
        logger.info("DummyPlugin is starting");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#stop()
     */
    @Override
    public void stop()
    {
        logger.info("DummyPlugin is stopping");
    }

    @Override
    public Map<String, String> kernelParametersAliases()
    {
        Map<String, String> m = new HashMap<String, String>();
        m.put(NUUNROOTALIAS, Kernel.NUUN_ROOT_PACKAGE);
        m.put(ALIAS_DUMMY_PLUGIN1, "dummy.plugin1");
        return m;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#kernelParamsRequired()
     */
    // @Override
    // public Collection<String> kernelParamsRequired()
    // {
    // return convertToCollection("zerzerze" , "zerzer");
    // }

}
