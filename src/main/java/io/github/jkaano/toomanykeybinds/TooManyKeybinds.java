package io.github.jkaano.toomanykeybinds;

import io.github.jkaano.toomanykeybinds.client.KeyHandler;
import io.github.jkaano.toomanykeybinds.client.TMKPageHandler;
import io.github.jkaano.toomanykeybinds.client.compatibility.EssentialsCompatibility;
import io.github.jkaano.toomanykeybinds.client.compatibility.RobotCompatibilityChecker;
import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import io.github.jkaano.toomanykeybinds.client.gui.ButtonIdentity;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

@Mod(TooManyKeybinds.MODID)
public class TooManyKeybinds {
    public static final String MODID = "toomanykeybinds";

    public static RobotCompatibilityChecker robotCompatibilityChecker;

    public TooManyKeybinds(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        //Create compatibility checking object
        robotCompatibilityChecker = new RobotCompatibilityChecker();

        //Set compatibilities
        EssentialsCompatibility.setEssentials();
    }

    private static int page = 0;
    private static int pageSelect = 0;

    public static KeyHandler tmkHandler;
    public static TMKPageHandler pageHandler;

    public static boolean searching = false;

    public static List<ButtonIdentity> keyQueue = new ArrayList<>();

    public static void setPage(int page) {
        TooManyKeybinds.page = page;
    }

    public static void setPageSelect(int pageSelect){
        TooManyKeybinds.pageSelect = pageSelect;
    }

    public static int getPage(){
        return page;
    }

    public static int getPageSelect(){
        return pageSelect;
    }

    public static RobotCompatibilityChecker getRobotCompatibilityChecker(){
        return robotCompatibilityChecker;
    }

}
