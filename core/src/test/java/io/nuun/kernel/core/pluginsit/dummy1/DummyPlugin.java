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
package io.nuun.kernel.core.pluginsit.dummy1;

import com.google.common.collect.Lists;
import com.google.inject.Module;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.BindingRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.KernelParamsRequest;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.core.pluginsit.dummy23.DummyPlugin2;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Epo Jemba
 */
public class DummyPlugin extends AbstractPlugin
{
    public static final String ALIAS_DUMMY_PLUGIN1 = "alias.dummy.plugin1";

    public static final String NUUN_ROOT_ALIAS = "nuunrootalias";
    public static final String NAME = "dummyPlugin";

    private Module module;

    @Override
    public String name()
    {
        return NAME;
    }

    @Override
    public String description()
    {
        return "description";
    }

    @Override
    public String pluginPropertiesPrefix()
    {
        return "dummy-";
    }

    @Override
    public String pluginPackageRoot()
    {
        return DummyPlugin.class.getPackage().getName();
    }

    @Override
    public Object nativeUnitModule()
    {
        return module;
    }

    @Override
    public InitState init(InitContext initContext)
    {
        String param = initContext.kernelParam("dummy.plugin1");
        assertThat(param).isNotEmpty();
        assertThat(param).isEqualTo("WAZAAAA");

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

        assertThat(cParent2).hasSize(2);

        Map<String, Collection<Class<?>>> scannedTypesByRegex = initContext.scannedTypesByRegex();
        Collection<Class<?>> cParent3 = scannedTypesByRegex.get(".*WithCustomSuffix");

        Collection<Class<?>> classes = new HashSet<>();
        classes.addAll(cParent3);
        classes.addAll(cParent2);
        classes.addAll(cParent1);
        classes.addAll(cAnnotations2);
        classes.addAll(cAnnotations1);

        module = new DummyModule(classes);

        assertThat(initContext.pluginsRequired()).isNotNull();
        assertThat(initContext.pluginsRequired()).hasSize(1);
        assertThat(initContext.pluginsRequired().iterator().next().getClass()).isEqualTo(DummyPlugin2.class);
        return InitState.INITIALIZED;
    }

    @Override
    public Collection<BindingRequest> bindingRequests()
    {
        return bindingRequestsBuilder().subtypeOfRegex(".*WithCustom2Suffix").build();
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        return classpathScanRequestBuilder()
                .annotationRegex(".*MarkerSample3")
                .annotationType(MarkerSample4.class)
                .subtypeOf(DummyMarker.class)
                .subtypeOfRegex(".*WithCustomSuffix")
                .typeOfRegex(".*WithCustomSuffix")
                .build();
    }

    @Override
    public Collection<KernelParamsRequest> kernelParamsRequests()
    {
        return kernelParamsRequestBuilder().mandatory("dummy.plugin1").build();
    }

    @Override
    public Collection<Class<?>> requiredPlugins()
    {
        return Lists.newArrayList(DummyPlugin2.class);
    }

    @Override
    public void start(Context context)
    {
        assertThat(context).isNotNull();
    }


    @Override
    public Map<String, String> kernelParametersAliases()
    {
        Map<String, String> m = new HashMap<>();
        m.put(NUUN_ROOT_ALIAS, "nuun.root.package");
        m.put(ALIAS_DUMMY_PLUGIN1, "dummy.plugin1");
        return m;
    }

}
