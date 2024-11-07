package io.github.jkaano.toomanykeybinds.client;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.screen.TMKScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.stream.Collectors;

public class KeyHandler {

    private static List<KeyMapping> keys;

    public KeyHandler() {
        TooManyKeybinds.pageHandler = new TMKPageHandler();
        update();
    }

    public void update(){
        keys = Arrays.asList(Minecraft.getInstance().options.keyMappings); //Keys to list
        List<String> categories = keys.stream().map(KeyMapping::getCategory).distinct().toList(); //Keys to category but distinct
        Map<String, List<KeyMapping>> categorizedKeyMapping = keys.stream().collect(Collectors.groupingBy(KeyMapping::getCategory)); //Categorize keys by category
        TooManyKeybinds.pageHandler.update(categorizedKeyMapping, categories);
    }

    public void updateSearchable(String lookup, TMKScreen screen){
        List <KeyMapping> searchableKeys = new ArrayList<>();
        keys.forEach(key -> {
            if(Component.translatable(key.getName()).getString().toLowerCase().contains(lookup.toLowerCase())){
                searchableKeys.add(key);
            }});
        List<String> searchableCategories = searchableKeys.stream().map(KeyMapping::getCategory).distinct().toList();
        Map<String, List<KeyMapping>> categorizedSearchable = searchableKeys.stream().collect(Collectors.groupingBy(KeyMapping::getCategory));

        if(!searchableCategories.isEmpty()){
            TooManyKeybinds.pageHandler.updateSearchable(categorizedSearchable, searchableCategories, screen);
        }

    }
}
