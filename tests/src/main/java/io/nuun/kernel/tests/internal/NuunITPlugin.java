/**
 * This file is part of Nuun IO Kernel Tests.
 *
 * Nuun IO Kernel Tests is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Tests is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Tests.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.tests.internal;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.kernel.tests.it.NuunITRunner;
import io.nuun.kernel.tests.it.annotations.ITBind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.runner.RunWith;
import org.kametic.specifications.Specification;


/**
 * @author epo.jemba{@literal @}kametic.com
 *
 */
public class NuunITPlugin extends AbstractPlugin
{

    private Collection<Class<?>> integrationTestsClass;

    @SuppressWarnings("unchecked")
    private final Specification<Class<?>> iTSpecs = or(classAnnotatedWith(RunWith.class), classAnnotatedWith(ITBind.class));

    @Override
    public String name() {
        return "nuun-kernel-tests-plugin";
    }

    @Override
    @SuppressWarnings("rawtypes")
    public InitState init(InitContext initContext) {
        Map<Specification, Collection<Class<?>>> scannedTypesBySpecification = initContext.scannedTypesBySpecification();
        Collection<Class<?>> iTClassCandidates = scannedTypesBySpecification.get(iTSpecs);
        if (iTClassCandidates != null && !iTClassCandidates.isEmpty()) {
            integrationTestsClass = new ArrayList<Class<?>>();
            for (Class<?> itCandidate : iTClassCandidates) {
                if (itCandidate.getAnnotation(RunWith.class) != null &&
//                        itCandidate.getAnnotation(RunWith.class).value().equals(NuunITRunner.class)
                        NuunITRunner.class.isAssignableFrom( itCandidate.getAnnotation(RunWith.class).value())
                        || itCandidate.getAnnotation(ITBind.class) != null) {
                    integrationTestsClass.add(itCandidate);
                }
            }
        }
        return InitState.INITIALIZED;
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder().specification(iTSpecs).build();
    }

    @Override
    public Object nativeUnitModule() {
        return new NuunITModule(integrationTestsClass);
    }

}
