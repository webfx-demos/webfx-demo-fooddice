// File managed by WebFX (DO NOT EDIT MANUALLY)
package webfx.demo.fooddice.application.gwt.embed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import dev.webfx.platform.resource.spi.impl.gwt.GwtResourceBundleBase;

public interface EmbedResourcesBundle extends ClientBundle {

    EmbedResourcesBundle R = GWT.create(EmbedResourcesBundle.class);
    @Source("dev/webfx/platform/meta/exe/exe.properties")
    TextResource r1();

    @Source("levels/level2.lvl")
    TextResource r2();

    @Source("levels/level3.lvl")
    TextResource r3();

    @Source("levels/level5.lvl")
    TextResource r4();

    @Source("levels/level6.lvl")
    TextResource r5();



    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("dev/webfx/platform/meta/exe/exe.properties", R.r1());
            registerResource("levels/level2.lvl", R.r2());
            registerResource("levels/level3.lvl", R.r3());
            registerResource("levels/level5.lvl", R.r4());
            registerResource("levels/level6.lvl", R.r5());

        }
    }
}
