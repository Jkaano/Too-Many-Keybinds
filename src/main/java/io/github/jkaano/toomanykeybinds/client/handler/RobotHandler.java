package io.github.jkaano.toomanykeybinds.client.handler;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.compatibility.EssentialsCompatibility;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.awt.*;

@Mod.EventBusSubscriber(modid = TooManyKeybinds.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RobotHandler {

    private static Robot robot;
    private static boolean useRobot;
    private static RobotCompatibilityChecker robotCompatibilityChecker;

    @SubscribeEvent
    public static void loadRobot(FMLLoadCompleteEvent event){
        robotCompatibilityChecker = TooManyKeybinds.getRobotCompatibilityChecker();
        checkCompatibility();

        if(useRobot) {
            try {
                System.out.println("Too Many Keybinds: Essentials not found, Load Robot");
                System.setProperty("java.awt.headless", "false");
                System.out.println("Too Many Keybinds: headless: " + System.getProperty("java.awt.headless"));
                System.out.println("Assigning new robot");
                robot = new Robot();
            } catch (AWTException e) {
                //throw new RuntimeException("Too Many Keybinds: Robot does not work");
                e.printStackTrace();
                useRobot = false;
            }
        }else{
            System.out.println("Too Many Keybinds: Conflict Found, ignoring robot");
        }

    }

    public static void simulateKeyPress(KeyMapping keyMapping){
        robot.keyPress(keyMapping.getKey().getValue());

    }

    public static void simulateKeyRelease(KeyMapping keyMapping){
        robot.keyRelease(keyMapping.getKey().getValue());
    }

    public static void setCompatConfig(){
        if(!useRobot && !ClientConfig.LOCK_AUTO.get()){
            ClientConfig.AUTOMATIC_KEY_PRESS.set(false);
            ClientConfig.AUTOMATIC_KEY_PRESS.save();
        }else if(!ClientConfig.LOCK_AUTO.get()){
            ClientConfig.AUTOMATIC_KEY_PRESS.set(true);
            ClientConfig.AUTOMATIC_KEY_PRESS.save();
        }
    }

    public static void checkCompatibility(){
        useRobot = robotCompatibilityChecker.checkRobotCompatibility();
    }

    public static boolean usingRobot(){
        return useRobot;
    }

}
