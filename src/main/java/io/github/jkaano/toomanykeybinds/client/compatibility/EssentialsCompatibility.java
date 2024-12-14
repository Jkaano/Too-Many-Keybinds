package io.github.jkaano.toomanykeybinds.client.compatibility;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import net.minecraftforge.fml.ModList;

public class EssentialsCompatibility {

    //Essentials causes issues with setting java.awt.headless to false and then creating a robot,
    //This class just holds a variable that checks if essentials is installed and uses a less effective
    //key press method. Using robot allows for keyRelease which some keybinds check for, Forge keyDown
    //doesn't trigger these events.
    //public static boolean essentials = true;

    public static void setEssentials() {
        essentials = ModList.get().isLoaded("essential");

        //If essentials is installed, turn off auto key press, or if it is uninstalled turn it on
        //Only do this if the user hasn't manually changed the key bind
        if(essentials && !ClientConfig.LOCK_AUTO.get()){
            ClientConfig.AUTOMATIC_KEY_PRESS.set(false);
            ClientConfig.AUTOMATIC_KEY_PRESS.save();
        }else if(!ClientConfig.LOCK_AUTO.get()){
            ClientConfig.AUTOMATIC_KEY_PRESS.set(true);
            ClientConfig.AUTOMATIC_KEY_PRESS.save();
        }
    }

}
