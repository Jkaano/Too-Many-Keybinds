package io.github.jkaano.toomanykeybinds.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    //Automatic key press will
    public static ForgeConfigSpec.ConfigValue<Boolean> AUTOMATIC_KEY_PRESS;
    public static ForgeConfigSpec.ConfigValue<Boolean> LOCK_AUTO;

    static {

        BUILDER.push("Too Many Keybinds Configuration");

        AUTOMATIC_KEY_PRESS = BUILDER.comment("Automatic Key Press").define("auto_key_press", true);
        LOCK_AUTO = BUILDER.define("lock_value_on_startup", false);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }

}
