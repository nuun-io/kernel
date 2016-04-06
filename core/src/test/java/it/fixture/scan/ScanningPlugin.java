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
package it.fixture.scan;

import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import org.kametic.specifications.Specification;

import java.util.Collection;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class ScanningPlugin extends AbstractPlugin
{

    public static final String NAME = "scanning";

    public final Specification<Class<?>> TO_SCAN_SPEC = classAnnotatedWith(ToScan.class);

    private Collection<Class<?>> scannedClasses;

    @Override
    public String name()
    {
        return NAME;
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        return classpathScanRequestBuilder().specification(TO_SCAN_SPEC).build();
    }

    @Override
    public InitState init(InitContext initContext)
    {
        scannedClasses = initContext.scannedTypesBySpecification().get(TO_SCAN_SPEC);
        return InitState.INITIALIZED;
    }

    public Collection<Class<?>> getScannedClasses()
    {
        return scannedClasses;
    }
}
