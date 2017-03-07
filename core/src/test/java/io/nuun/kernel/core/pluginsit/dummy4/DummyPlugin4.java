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
import io.nuun.kernel.api.predicates.ClassAnnotatedWith;
import io.nuun.kernel.api.predicates.ClassImplements;
import io.nuun.kernel.core.AbstractPlugin;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class DummyPlugin4 extends AbstractPlugin
{
    public Collection<Class<?>> collection;
    private Predicate<Class<?>> predicate;

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
        Predicate<Class<?>> predicate = new ClassAnnotatedWith(MarkerSample5.class).and(new ClassImplements(Interface2.class));

        assertThat(predicate.test(Pojo1.class)).isFalse();
        assertThat(predicate.test(Pojo2.class)).isTrue();

        return bindingRequestsBuilder().predicate(predicate).withScope(Scopes.SINGLETON).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        predicate = new ClassAnnotatedWith(MarkerSample5.class).and(new ClassImplements(Interface1.class));

        assertThat(predicate.test(Pojo1.class)).isTrue();
        assertThat(predicate.test(Pojo2.class)).isFalse();

        return classpathScanRequestBuilder().predicate(predicate).build();
    }


    @SuppressWarnings("rawtypes")
    @Override
    public InitState init(InitContext initContext)
    {
        Map<Predicate<Class<?>>, Collection<Class<?>>> scannedTypesByPredicate = initContext.scannedTypesByPredicate();

        collection = scannedTypesByPredicate.get(predicate);

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
