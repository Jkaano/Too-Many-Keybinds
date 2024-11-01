package io.github.jkaano.toomanykeybinds.client;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.screen.TMKScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class KeyHandler {

    private static KeyMapping[] KEYS;
    private static String[] categories;
    private static KeyMapping[][] preparedPages;

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
        preparedPages = preparePages(categories);
        TooManyKeybinds.pageHandler.update(preparedPages);
        System.out.println("Too Many Keybinds Pages prepared");
    }

    public void update(){
        System.out.println("Too Many Keybinds: Updating keys");
        KEYS = ArrayUtils.clone(Minecraft.getInstance().options.keyMappings);
        categories = setCategories(KEYS);
        preparedPages = preparePages(categories);
        TooManyKeybinds.pageHandler.update(preparedPages);
    }

    public void updateSearchable(String lookup, TMKScreen screen){
        System.out.println("Too Many Keybinds: Updating searchable keys");
        setSearchableKeys(lookup);
        if(searchableCategories.length != 0){
            TooManyKeybinds.pageHandler.updateSearchables(preparePages(searchableCategories), screen);
        }else{
            System.out.println("No searchable keys");
        }

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
    private KeyMapping[][] preparePages(String[] categories){

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

        return tempMapping;
    }

    public void setSearchableKeys(String search){

        List<KeyMapping> searchable = new ArrayList<>();

        for(KeyMapping key : KEYS){
            String comp = Component.translatable(key.getName()).getString();
            if(comp.toLowerCase().contains(search.toLowerCase())){
                System.out.println(comp + " contains " + search);
                searchable.add(key);
            }
        }

        KeyMapping[] searchableKeys = searchable.toArray(new KeyMapping[0]);
        searchableCategories = setCategories(searchableKeys);

    }

    public KeyMapping[] getKeys(){
        return KEYS;
    }

}
