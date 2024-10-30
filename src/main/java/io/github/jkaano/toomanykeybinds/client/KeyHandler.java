package io.github.jkaano.toomanykeybinds.client;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class KeyHandler {

    private static KeyMapping[] KEYS;
    private static String[] categories;

    private static KeyMapping[] searchableKeys;
    private static String[] searchableCategories;

    public KeyHandler() {
        System.out.println("Too Many Keybinds: Key Handler created");
        TooManyKeybinds.pageHandler = new TMKPageHandler();
        KEYS = ArrayUtils.clone(Minecraft.getInstance().options.keyMappings);
        System.out.println("Too Many Keybinds: Keys copied");
        System.out.println("Too Many Keybinds: Creating categories");
        categories = setCategories(KEYS);
        System.out.println("Too Many Keybinds: Categories created");
        System.out.println("Too Many Keybinds: Preparing pages");
        preparePages();
        System.out.println("Too Many Keybinds Pages prepared");
    }

    public void update(){
        System.out.println("Too Many Keybinds: Updating keys");
        KEYS = ArrayUtils.clone(Minecraft.getInstance().options.keyMappings);
        categories = setCategories(KEYS);
        preparePages();
    }

    //Create a list of categories
    private String[] setCategories(KeyMapping[] keys){
        List<String> categories = new ArrayList<>();

        for(KeyMapping key : keys){
            if(!categories.contains(key.getCategory())) {
                categories.add(key.getCategory());
            }
        }

        return categories.toArray(new String[0]);
    }

    //Split keys into categories, then update pageHandler
    private void preparePages(){

        List<KeyMapping> tempKey = new ArrayList<>();
        KeyMapping[][] tempMapping = new KeyMapping[categories.length][];

        for(int i = 0; i < categories.length; i++){
            for(KeyMapping key : KEYS){
                if(key.getCategory().equals(categories[i])){
                    tempKey.add(key);
                }
            }

            tempMapping[i] = tempKey.toArray(new KeyMapping[0]);
            tempKey.clear();

        }

        TooManyKeybinds.pageHandler.update(tempMapping);
    }

    public KeyMapping[] getKeys(){
        return KEYS;
    }

}
