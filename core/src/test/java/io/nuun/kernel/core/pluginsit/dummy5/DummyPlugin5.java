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
package io.nuun.kernel.core.pluginsit.dummy5;

import com.google.inject.Scopes;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DummyPlugin5 extends AbstractPlugin
{

    public Collection<Class<?>> collection;


    public DummyPlugin5()
    {
    }

    @Override
    public String name()
    {
        return "dummuyPlugin5";
    }
    
    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        
        return bindingRequestsBuilder() //
                .descendentTypeOf(GrandParentClass.class).withScope(Scopes.SINGLETON) //
                .metaAnnotationType(MetaMarkerSample.class).withScope(Scopes.SINGLETON) //
                .metaAnnotationRegex(".*YMetaMarker.*").withScope(Scopes.SINGLETON)
//                .descendentTypeOf(GrandParentInterface.class) //
                .build();
    }
    
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        
        return classpathScanRequestBuilder()
                .descendentTypeOf(GrandParentClass.class) //
                .descendentTypeOf(GrandParentInterface.class) //
                .build();
    }
    
    
    @Override
    public InitState init(InitContext initContext)
    {
        Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass = initContext.scannedSubTypesByAncestorClass();
        
        collection = scannedSubTypesByAncestorClass.get(GrandParentClass.class);
        
        assertThat(collection).isNotEmpty();
        assertThat(collection).hasSize(2);
        assertThat(collection).containsOnly(DescendantFromClass.class , ParentClass.class);
        return InitState.INITIALIZED;
    }
    
    
    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin5.class.getPackage().getName();
    }

}
