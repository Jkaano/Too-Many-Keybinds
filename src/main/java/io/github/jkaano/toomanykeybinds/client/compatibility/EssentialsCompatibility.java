package io.github.jkaano.toomanykeybinds.client.compatibility;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import net.minecraftforge.fml.ModList;

public class EssentialsCompatibility {

    //Essentials causes issues with setting java.awt.headless to false and then creating a robot,
    //This class just holds a variable that checks if essentials is installed and uses a less effective
    //key press method. Using robot allows for keyRelease which some keybinds check for, Forge keyDown
    //doesn't trigger these events.

    public static void setEssentials() {
        TooManyKeybinds.getRobotCompatibilityChecker().addCompatible(
                ModList.get().isLoaded("essential")
        );
    }

}
