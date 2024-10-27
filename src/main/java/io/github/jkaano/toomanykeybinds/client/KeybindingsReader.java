package io.github.jkaano.toomanykeybinds.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeybindingsReader {

    private static KeyMapping[] KEYS;
    private static KeyMapping[][] splitCategoryKeys;
    private static KeyMapping[][] splitPagesKeys;

    private static String[] categories;
    private static String[] pages;

    private int chunkSize = 10;

    public KeybindingsReader() {
        update();
    }

    public void update(){
        KEYS = Minecraft.getInstance().options.keyMappings;
        splitCategoryKeys = splitCategories();
        splitPages(splitCategoryKeys);
    }

    //Create a list of categories
    private void setCategories(){
        List<String> categories = new ArrayList<>();

        for(KeyMapping key : KEYS){
            if(!categories.contains(key.getCategory())) {
                categories.add(key.getCategory());
            }
        }

        KeybindingsReader.categories = categories.toArray(new String[0]);
    }

    //Split keys into categories
    private KeyMapping[][] splitCategories(){

        setCategories();

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

    private void splitPages(KeyMapping[][] arrayToSplit){

        KeyMapping[][] arrays;
        List<KeyMapping[]> pages = new ArrayList<>();

        for(KeyMapping[] keys : arrayToSplit){
            if(keys.length > chunkSize){
                arrays = splitKeys(keys, chunkSize);
                Collections.addAll(pages, arrays);
            }else{
                pages.add(keys);
            }
        }

        splitPagesKeys = pages.toArray(new KeyMapping[0][]);
        setPages();

    }

    //Code from GameDroids on stackoverflow
    private KeyMapping[][] splitKeys(KeyMapping[] arrayToSplit, int chunkSize){
        int remainder = arrayToSplit.length % chunkSize;
        int chunks = arrayToSplit.length / chunkSize + (remainder > 0 ? 1 : 0);

        KeyMapping[][] arrays = new KeyMapping[chunks][];

        for(int i = 0; i < (remainder > 0 ? chunks - 1 : chunks); i++){
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
        }
        if(remainder > 0){
            arrays[chunks-1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + remainder);
        }

        return arrays;
    }



    private void setPages(){
        List<String> pages = new ArrayList<>();

        for(KeyMapping[] keys : splitPagesKeys){
            pages.add(keys[0].getCategory());
        }

        KeybindingsReader.pages = pages.toArray(new String[0]);
    }



    //For potential customization option, probably won't ever use
    public void setChunkSize(int chunkSize){
        this.chunkSize = chunkSize;
    }

    //Getters
    public KeyMapping[][] getSplitPagesKeys(){
        return splitPagesKeys;
    }

    public String[] getPages(){
        return pages;
    }

    public String[] getCategories() {
        return categories;
    }

    public KeyMapping[][] getSplitCategoryKeys(){
        return splitCategoryKeys;
    }

    public KeyMapping[] getKeys(){
        return KEYS;
    }

}
