package io.github.jkaano.toomanykeybinds;

import io.github.jkaano.toomanykeybinds.platform.Services;

public class CommonClass {

    public static void init() {
        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!",
                Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        if (Services.PLATFORM.isModLoaded("toomanykeybinds")) {
            Constants.LOG.info("Too Many Keybinds loaded");
        }
    }

}