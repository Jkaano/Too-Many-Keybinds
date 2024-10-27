package io.github.jkaano.toomanykeybinds;

import io.github.jkaano.toomanykeybinds.client.KeybindingsReader;
import net.minecraftforge.fml.common.Mod;

@Mod(TooManyKeybinds.MODID)
public class TooManyKeybinds {
    public static final String MODID = "toomanykeybinds";

    public static int page = 0;

    public static KeybindingsReader tmkHandler;
}
