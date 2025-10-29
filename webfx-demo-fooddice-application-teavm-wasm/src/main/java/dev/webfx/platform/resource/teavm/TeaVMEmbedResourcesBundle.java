package dev.webfx.platform.resource.teavm;

import dev.webfx.platform.resource.spi.impl.teavm.TeaVMResourceBundle;
import org.teavm.classlib.ResourceSupplier;
import org.teavm.classlib.ResourceSupplierContext;

public final class TeaVMEmbedResourcesBundle extends TeaVMResourceBundle implements ResourceSupplier {

    private static final String[] RESOURCE_PATHS = {
        "dev/webfx/platform/meta/exe/exe.properties",
        "levels/level2.lvl",
        "levels/level3.lvl",
        "levels/level5.lvl",
        "levels/level6.lvl",
        "levels/level7.lvl",
        "levels/level8.lvl"
    };

    // Note: called at TeaVM build time, not at runtime
    @Override
    public String[] supplyResources(ResourceSupplierContext context) {
        return RESOURCE_PATHS;
    }

    public TeaVMEmbedResourcesBundle() {
        super(RESOURCE_PATHS);
    }

}