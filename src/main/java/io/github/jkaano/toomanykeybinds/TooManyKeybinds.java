package io.github.jkaano.toomanykeybinds;

import io.github.jkaano.toomanykeybinds.client.KeyHandler;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPage;
import io.github.jkaano.toomanykeybinds.client.TMKPageHandler;
import io.github.jkaano.toomanykeybinds.client.handler.RobotHandler;
import net.minecraftforge.fml.common.Mod;

@Mod(TooManyKeybinds.MODID)
public class TooManyKeybinds {
    public static final String MODID = "toomanykeybinds";

    private static int page = 0;
    private static int pageSelect = 0;

    public static KeyHandler tmkHandler;
    public static TMKPageHandler pageHandler;

    public static boolean searching = false;

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

}
