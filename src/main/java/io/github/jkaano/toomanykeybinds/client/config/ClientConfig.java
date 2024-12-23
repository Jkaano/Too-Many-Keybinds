package io.github.jkaano.toomanykeybinds.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    //Automatic key press will auto set on start up unless lock auto is set to true
    public static ForgeConfigSpec.ConfigValue<Boolean> AUTOMATIC_KEY_PRESS;
    public static ForgeConfigSpec.ConfigValue<Boolean> LOCK_AUTO;

    public static ForgeConfigSpec.ConfigValue<Long> PRESS_DELAY;

    public static final String AUTO_PRESS = "auto_press.";

    static {

        BUILDER.push("Too Many Keybinds Configuration");

        AUTOMATIC_KEY_PRESS = BUILDER.comment("Automatic Key Press or User Requested Key Press").define(AUTO_PRESS + "auto_key_press", true);
        LOCK_AUTO = BUILDER.define(AUTO_PRESS + "lock_value_on_startup", false);

        PRESS_DELAY = BUILDER.comment("Cancel Delay(Seconds) - Applies if auto key press is off").defineInRange("cancel_delay", 5L, 1L, 60L);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }

}
