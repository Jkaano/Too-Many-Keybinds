package io.github.jkaano.toomanykeybinds.client.compatibility;

public class EssentialsCompatibility {

    //Essentials causes issues with setting java.awt.headless to false and then creating a robot,
    //This class just holds a variable that checks if essentials is installed and uses a less effective
    //key press method. Using robot allows for keyRelease which some keybinds check for, Forge keyDown
    //doesn't trigger these events.
    public static boolean essentials = false;

}
