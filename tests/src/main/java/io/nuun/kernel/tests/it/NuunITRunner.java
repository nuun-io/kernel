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
package io.nuun.kernel.tests.it;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.config.ClasspathScanMode;
import io.nuun.kernel.api.config.KernelConfiguration;
import io.nuun.kernel.tests.it.annotations.Expect;
import io.nuun.kernel.tests.it.annotations.WithPlugins;
import io.nuun.kernel.tests.it.annotations.WithoutSpiPluginsLoader;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.google.inject.ProvisionException;

/**
 * @author epo.jemba@kametic.com
 *
 */
public class NuunITRunner extends BlockJUnit4ClassRunner
{
    private Kernel kernel;
    private Class<?> expectedClass = null;

    
    /**
     * @param klass
     * @throws InitializationError
     */
    public NuunITRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
        
    }
    
    @Override
    public void run(RunNotifier notifier) {
        kernel = initKernel();
        super.run(notifier);
        kernel.stop();
    }


    @Override
    protected Object createTest() throws Exception {
        boolean catchOccured = false;
        boolean expecting = withExpect();
        Throwable actualThrowable = null;
        Object test = null;
        
        try {
            test = kernel.getMainInjector().getInstance(getTestClass().getJavaClass());
        } catch (Throwable t)
        {
//          t.printStackTrace();
            
            if (t.getClass().equals(ProvisionException.class) )
            {
                t = t.getCause();
            }
                
            actualThrowable = t;
            
            if (t.getClass().equals(expectedClass))
            {
                test = super.createTest();
            }
            catchOccured = true;
        }
        
        if (expecting && ! catchOccured)
        {
            
            String message = "EXPECTED_EXCEPTION_DID_NOT_OCCURED";
            message += "\n\tExpected class = " + expectedClass;
            message += "\n\tActual throwable = " + actualThrowable;
            NuunITException exception = new NuunITException(message,actualThrowable);
            throw exception;
            
        }
        
        if (! expecting  && catchOccured)
        {
            String message = "UNEXPECTED_EXCEPTION_OCCURED";
            message += "\n\tExpected class = " + expectedClass;
            message += "\n\tActual throwable = " + actualThrowable;
            NuunITException exception = new NuunITException(message,actualThrowable);
            throw exception;
        }
        
        
        return test;
    }
    
    private Kernel initKernel() {

        KernelConfiguration configuration =
                newKernelConfiguration()
                 ;
        
        withoutSpiPluginsLoader(configuration);
        withPlugins(configuration);
        
        Kernel underTest = createKernel ( configuration  );
        
        underTest.init(); //
        underTest.start(); //
        return underTest;
    }


    private void withPlugins(KernelConfiguration configuration) {
        WithPlugins annotation = getTestClass().getJavaClass().getAnnotation(WithPlugins.class);
        if(annotation!=null){
            configuration.plugins(annotation.value());
        }
    }
    
    private boolean withExpect() {
        Expect annotation = getTestClass().getJavaClass().getAnnotation(Expect.class);
        if ( annotation != null ) {
            expectedClass  = annotation.value();
        }
        
        return expectedClass != null;

    }


    private void withoutSpiPluginsLoader(KernelConfiguration configuration) {
        if(getTestClass().getJavaClass().getAnnotation(WithoutSpiPluginsLoader.class)!=null){
            configuration.withoutSpiPluginsLoader();
        }
    }
    
    
}
