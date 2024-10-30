package io.github.jkaano.toomanykeybinds.client;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPage;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPageHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyHandler {

    private static KeyMapping[] KEYS;
    private static String[] categories;
    private static TMKPage[][] pageSelect;

    private static final int CHUNKSIZE = 10;
    private static final int PAGE_SPLIT = 6;

    public KeyHandler() {
        update();

        System.out.println("Too Many Keybinds: Creating page list");
        TooManyKeybinds.pageHandler = new TMKPageHandler();
        System.out.println("Too Many Keybinds: Successfully created page list");
    }

    public void update(){
        KEYS = ArrayUtils.clone(Minecraft.getInstance().options.keyMappings);
        TooManyKeybinds.pages = createPages(splitCategories());
        pageSelect = setPageSelect(TooManyKeybinds.pages);
    }

    //Create a list of categories
    private void setCategories(){
        List<String> categories = new ArrayList<>();

        for(KeyMapping key : KEYS){
            if(!categories.contains(key.getCategory())) {
                categories.add(key.getCategory());
            }
        }

        KeyHandler.categories = categories.toArray(new String[0]);
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

    //Create page objects, add keys, and index
    public static TMKPage[] createPages(KeyMapping[][] modifiedKeys){

        List<TMKPage> pages = new ArrayList<>();
        KeyMapping[][] toSplit;

        int index = 0;

        for(KeyMapping[] keys : modifiedKeys){
            if(keys.length > CHUNKSIZE){
                toSplit = splitKeys(keys);
                for(KeyMapping[] split : toSplit){
                    pages.add(new TMKPage(split[0].getCategory(), index, split));
                    index++;
                }
            }else{
                pages.add(new TMKPage(keys[0].getCategory(), index, keys));
                index++;
            }
        }

        return pages.toArray(new TMKPage[0]);
    }

    //Code from GameDroids on stackoverflow - Splits TMKPage[] into chunks of PAGE_SPLIT
    private static TMKPage[][] setPageSelect(TMKPage[] pages){
        int remainder = pages.length % PAGE_SPLIT;
        int chunks = pages.length / PAGE_SPLIT + (remainder > 0 ? 1 : 0);

        TMKPage[][] arrays = new TMKPage[chunks][];

        for(int i = 0; i < (remainder > 0 ? chunks - 1 : chunks); i++){
            arrays[i] = Arrays.copyOfRange(pages, i * PAGE_SPLIT, i * PAGE_SPLIT + PAGE_SPLIT);
        }
        if(remainder > 0){
            arrays[chunks-1] = Arrays.copyOfRange(pages, (chunks - 1) * PAGE_SPLIT, (chunks - 1) * PAGE_SPLIT + remainder);
        }
        return arrays;
    }

    //Code from GameDroids on stackoverflow - Splits KeyMapping[] into chunks of CHUNKSIZE
    public static KeyMapping[][] splitKeys(KeyMapping[] arrayToSplit){
        int remainder = arrayToSplit.length % CHUNKSIZE;
        int chunks = arrayToSplit.length / CHUNKSIZE + (remainder > 0 ? 1 : 0);

        KeyMapping[][] arrays = new KeyMapping[chunks][];

        for(int i = 0; i < (remainder > 0 ? chunks - 1 : chunks); i++){
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * CHUNKSIZE, i * CHUNKSIZE + CHUNKSIZE);
        }
        if(remainder > 0){
            arrays[chunks-1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * CHUNKSIZE, (chunks - 1) * CHUNKSIZE + remainder);
        }

        return arrays;
    }

    //Getters
    public TMKPage[][] getPageSelect(){
        return pageSelect;
    }

    public KeyMapping[] getKeys(){
        return KEYS;
    }

}
