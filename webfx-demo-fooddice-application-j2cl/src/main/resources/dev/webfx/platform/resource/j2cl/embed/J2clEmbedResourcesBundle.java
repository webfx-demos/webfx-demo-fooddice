// File managed by WebFX (DO NOT EDIT MANUALLY)
package dev.webfx.platform.resource.j2cl.embed;

import org.treblereel.j2cl.processors.annotations.GWT3Resource;
import org.treblereel.j2cl.processors.common.resources.ClientBundle;
import org.treblereel.j2cl.processors.common.resources.TextResource;
import dev.webfx.platform.resource.spi.impl.j2cl.J2clResourceBundleBase;

@GWT3Resource
public interface J2clEmbedResourcesBundle extends ClientBundle {

    J2clEmbedResourcesBundle R = J2clEmbedResourcesBundleImpl.INSTANCE;

    @Source("/dev/webfx/platform/meta/exe/exe.properties")
    TextResource r1();

    @Source("/levels/level2.lvl")
    TextResource r2();

    @Source("/levels/level3.lvl")
    TextResource r3();

    @Source("/levels/level5.lvl")
    TextResource r4();

    @Source("/levels/level6.lvl")
    TextResource r5();

    @Source("/levels/level7.lvl")
    TextResource r6();

    @Source("/levels/level8.lvl")
    TextResource r7();

    final class ProvidedJ2clResourceBundle extends J2clResourceBundleBase {
        public ProvidedJ2clResourceBundle() {
            registerResource("dev/webfx/platform/meta/exe/exe.properties", () -> R.r1().getText());
            registerResource("levels/level2.lvl", () -> R.r2().getText());
            registerResource("levels/level3.lvl", () -> R.r3().getText());
            registerResource("levels/level5.lvl", () -> R.r4().getText());
            registerResource("levels/level6.lvl", () -> R.r5().getText());
            registerResource("levels/level7.lvl", () -> R.r6().getText());
            registerResource("levels/level8.lvl", () -> R.r7().getText());
        }
    }
}