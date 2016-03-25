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
package io.nuun.kernel.core.pluginsit.dummy4;

import com.google.inject.Scopes;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import org.kametic.specifications.Specification;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DummyPlugin4 extends AbstractPlugin
{
    public Collection<Class<?>> collection;
    private Specification<Class<?>> specification;

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
        Specification<Class<?>> specification = and(classAnnotatedWith(MarkerSample5.class), classImplements(Interface2.class));

        assertThat(specification.isSatisfiedBy(Pojo1.class)).isFalse();
        assertThat(specification.isSatisfiedBy(Pojo2.class)).isTrue();

        return bindingRequestsBuilder().specification(specification).withScope(Scopes.SINGLETON).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        specification = and(classAnnotatedWith(MarkerSample5.class), classImplements(Interface1.class));

        assertThat(specification.isSatisfiedBy(Pojo1.class)).isTrue();
        assertThat(specification.isSatisfiedBy(Pojo2.class)).isFalse();

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
