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
package io.nuun.kernel.core.pluginsit.dummy4;

import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;
import java.util.Map;

import org.kametic.specifications.Specification;

import com.google.inject.Scopes;

public class DummyPlugin4 extends AbstractPlugin
{

    private Specification<Class<?>> specification;
    public Collection<Class<?>> collection;


    public DummyPlugin4()
    {
    }

    @Override
    public String name()
    {
        return "dummuyPlugin4";
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<BindingRequest> bindingRequests()
    {
        Specification<Class<?>> specification = and( classAnnotatedWith(MarkerSample5.class) , classImplements(Interface2.class));
        
        assertThat( specification.isSatisfiedBy(Pojo1.class) ).isFalse();
        assertThat( specification.isSatisfiedBy(Pojo2.class) ).isTrue();
        
        return bindingRequestsBuilder().specification(specification ).withScope(Scopes.SINGLETON).build();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        specification = and( classAnnotatedWith(MarkerSample5.class) , classImplements(Interface1.class));

        assertThat( specification.isSatisfiedBy(Pojo1.class) ).isTrue();
        assertThat( specification.isSatisfiedBy(Pojo2.class) ).isFalse();
        
        return classpathScanRequestBuilder().specification(specification).build();
    }
    
    
    @SuppressWarnings("rawtypes")
    @Override
    public InitState init(InitContext initContext)
    {
        Map<Specification, Collection<Class<?>>> scannedTypesBySpecification = initContext.scannedTypesBySpecification();
        
        collection = scannedTypesBySpecification.get(specification);
        
        assertThat(collection).isNotEmpty();
        assertThat(collection).hasSize(1);
        assertThat(collection).containsOnly(Pojo1.class);
        return InitState.INITIALIZED;
    }
    
    
    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin4.class.getPackage().getName();
    }

}
