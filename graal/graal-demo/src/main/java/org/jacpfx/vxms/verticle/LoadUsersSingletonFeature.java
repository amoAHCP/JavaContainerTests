package org.jacpfx.vxms.verticle;

import org.graalvm.nativeimage.Feature;
import org.graalvm.nativeimage.ImageSingletons;

public class LoadUsersSingletonFeature  implements Feature {
    @Override
    public void afterRegistration(Feature.AfterRegistrationAccess access) {
        /* This code runs during image generation. */
        ImageSingletons.add(LoadUsers.class, new LoadUsers());
    }
}

