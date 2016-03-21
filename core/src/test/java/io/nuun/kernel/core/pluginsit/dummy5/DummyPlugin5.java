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
