package io.github.jkaano.toomanykeybinds;

import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import io.github.jkaano.toomanykeybinds.client.pages.PageFileHandler;
import io.github.jkaano.toomanykeybinds.client.screen.ConfigScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(TooManyKeybinds.MODID)
public class TooManyKeybinds {
    public static final String MODID = "toomanykeybinds";

    public static PageFileHandler pageFileHandler;

    public TooManyKeybinds(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        MinecraftForge.registerConfigScreen(ConfigScreen::new);
    }

}
