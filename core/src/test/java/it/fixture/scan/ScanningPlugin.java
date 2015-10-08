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
public class ScanningPlugin extends AbstractPlugin {

    public static final String NAME = "scanning";

    public final Specification<Class<?>> TO_SCAN_SPEC = classAnnotatedWith(ToScan.class);

    private Collection<Class<?>> scannedClasses;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder().specification(TO_SCAN_SPEC).build();
    }

    @Override
    public InitState init(InitContext initContext) {
        scannedClasses = initContext.scannedTypesBySpecification().get(TO_SCAN_SPEC);
        return InitState.INITIALIZED;
    }

    public Collection<Class<?>> getScannedClasses() {
        return scannedClasses;
    }
}
