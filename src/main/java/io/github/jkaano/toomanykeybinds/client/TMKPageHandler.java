package io.github.jkaano.toomanykeybinds.client;

import io.github.jkaano.toomanykeybinds.client.gui.PageButtonIdentity;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPage;
import io.github.jkaano.toomanykeybinds.client.screen.TMKScreen;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TMKPageHandler {

    private static TMKPage[] pages;
    private static TMKPage[][] pageSelect;

    private static TMKPage[] searchablePages;
    private static TMKPage[][] searchablePageSelect;

    private final int BUTTON_COUNT = TMKConstants.BUTTON_COUNT;

    private static PageButtonIdentity[][] buttons;
    private static PageButtonIdentity[][] searchableButtons;

    public TMKPageHandler(){
        System.out.println("Too Many Keybinds: Page Handler created");
    }

    public void update(KeyMapping[][] keys){
        System.out.println("Too Many Keybinds: Updating pages");
        pages = createPages(keys);
        pageSelect = splitPages(pages);
        buttons = initButtons(pageSelect);
    }

    public void updateSearchables(KeyMapping[][] keys, TMKScreen screen){
        System.out.println("Too Many Keybinds: Updating searchable pages");
        searchablePages = createPages(keys);
        searchablePageSelect = splitPages(searchablePages);
        searchableButtons = initButtons(searchablePageSelect);
        screen.update();
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

    public PageButtonIdentity[][] initButtons(TMKPage[][] pages){
        List<PageButtonIdentity> pgi = new ArrayList<>();
        PageButtonIdentity[][] tempPgi = new PageButtonIdentity[pages.length][];

        for(int i = 0; i < pages.length; i++){
            for(int k = 0; k < pages[i].length; k++){
                TMKPage currentPage = pages[i][k];
                pgi.add(new PageButtonIdentity(currentPage.getName(), currentPage.getPageNumber()));
            }
            tempPgi[i] = pgi.toArray(new PageButtonIdentity[0]);
        }
        return tempPgi;
    }

    //Getters
    public TMKPage[] getPages(){
        return  pages;
    }

    public TMKPage[][] getPageSelect(){
        return pageSelect;
    }

    public TMKPage[] getSearchablePages(){
        return searchablePages;
    }

    public TMKPage[][] getSearchablePageSelect(){
        return searchablePageSelect;
    }

    public PageButtonIdentity[][] getButtons(){
        return buttons;
    }

    public PageButtonIdentity[][] getSearchableButtons(){
        return searchableButtons;
    }

}
