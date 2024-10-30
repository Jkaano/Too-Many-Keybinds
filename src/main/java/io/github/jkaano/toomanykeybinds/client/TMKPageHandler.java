package io.github.jkaano.toomanykeybinds.client;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.gui.PageButtonIdentity;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPage;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TMKPageHandler {

    private static TMKPage[] pages;
    private static TMKPage[][] pageSelect;

    private static TMKPage[][] searchablePages;

    private final int BUTTON_COUNT = TMKConstants.BUTTON_COUNT;

    private static PageButtonIdentity[][] buttons;

    public TMKPageHandler(){
        System.out.println("Too Many Keybinds: Page Handler created");
    }

    public void update(KeyMapping[][] keys){
        System.out.println("Too Many Keybinds: Updating pages");
        pages = createPages(keys);
        TooManyKeybinds.pages = pages;
        pageSelect = splitPages(pages);
        buttons = new PageButtonIdentity[pageSelect.length][];
        initButtons();
    }

    //Create page objects, add keys, and index
    public TMKPage[] createPages(KeyMapping[][] modifiedKeys){

        List<TMKPage> pages = new ArrayList<>();
        KeyMapping[][] toSplit;


        int index = 0;
        for(KeyMapping[] keys : modifiedKeys){
            if(keys.length > BUTTON_COUNT){
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

    //Code from GameDroids on stackoverflow - Splits KeyMapping[] into chunks of CHUNKSIZE
    private KeyMapping[][] splitKeys(KeyMapping[] toSplit){
        int remainder = toSplit.length % BUTTON_COUNT;
        int chunks = toSplit.length / BUTTON_COUNT + (remainder > 0 ? 1 : 0);

        KeyMapping[][] arrays = new KeyMapping[chunks][];

        for(int i = 0; i < (remainder > 0 ? chunks - 1 : chunks); i++){
            arrays[i] = Arrays.copyOfRange(toSplit,
                    i*BUTTON_COUNT,
                    i*BUTTON_COUNT+BUTTON_COUNT);
        }if(remainder > 0){
            arrays[chunks-1] = Arrays.copyOfRange(toSplit,
                    (chunks - 1)*BUTTON_COUNT,
                    (chunks-1)*BUTTON_COUNT+remainder);
        }
        return arrays;
    }

    //Code from GameDroids on stackoverflow - Splits KeyMapping[] into chunks of CHUNKSIZE
    private TMKPage[][] splitPages(TMKPage[] toSplit){
        int PAGE_COUNT = TMKConstants.PAGE_COUNT;
        int remainder = toSplit.length % PAGE_COUNT;
        int chunks = toSplit.length / PAGE_COUNT + (remainder > 0 ? 1 : 0);

        TMKPage[][] arrays = new TMKPage[chunks][];

        for(int i = 0; i < (remainder > 0 ? chunks - 1 : chunks); i++){
            arrays[i] = Arrays.copyOfRange(toSplit,
                    i* PAGE_COUNT,
                    i* PAGE_COUNT + PAGE_COUNT);
        }if(remainder > 0){
            arrays[chunks-1] = Arrays.copyOfRange(toSplit,
                    (chunks - 1)* PAGE_COUNT,
                    (chunks-1)* PAGE_COUNT + remainder);
        }
        return arrays;
    }

    public void initButtons(){
        List<PageButtonIdentity> pgi = new ArrayList<>();

        for(int i = 0; i < pageSelect.length; i++){
            for(int k = 0; k < pageSelect[i].length; k++){
                TMKPage currentPage = pageSelect[i][k];
                pgi.add(new PageButtonIdentity(currentPage.getName(), currentPage.getPageNumber()));
            }
            buttons[i] = pgi.toArray(new PageButtonIdentity[0]);
        }
    }

    //Getters
    public TMKPage[] getPages(){
        return  pages;
    }

    public TMKPage[][] getPageSelect(){
        return pageSelect;
    }

    public PageButtonIdentity[][] getButtons(){
        return buttons;
    }

}
