package io.github.jkaano.toomanykeybinds.client;

import com.google.common.collect.Lists;
import io.github.jkaano.toomanykeybinds.client.gui.PageButtonIdentity;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPage;
import io.github.jkaano.toomanykeybinds.client.screen.TMKScreen;
import net.minecraft.client.KeyMapping;

import java.util.*;

import static io.github.jkaano.toomanykeybinds.client.TMKConstants.*;

public class TMKPageHandler {

    private static List<TMKPage> pages;
    private static List<List<TMKPage>> pageSelect;
    private static List<List<PageButtonIdentity>> pageButtons;

    private static List<TMKPage> searchablePages;
    private static List<List<TMKPage>> searchablePageSelect;
    private static List<List<PageButtonIdentity>> searchableButtons;

    public TMKPageHandler(){

    }

    public void update(Map<String, List<KeyMapping>> map, List<String> categories){
        pages = createPages(map, categories); //Create pages
        pageSelect = Lists.partition(pages, PAGE_COUNT); //Split pages into groups of 12
        pageButtons = new ArrayList<>();
        pageSelect.forEach(p1 -> {
            List<PageButtonIdentity> pbi = new ArrayList<>();
            pageSelect.get(pageSelect.indexOf(p1)).forEach(p2 -> {
                pbi.add(new PageButtonIdentity(p2.getName(), p2.getPageNumber()));});
            pageButtons.add(pbi);
        }); //Initialize buttons

        ClientConfig.PAGES.set(pageSelect.toString());
        ClientConfig.PAGES.save();

        readConfig();

    }

    public void updateSearchable(Map<String, List<KeyMapping>> map, List<String> categories, TMKScreen screen){

        searchablePages = createPages(map, categories); //Create pages
        searchablePageSelect = Lists.partition(searchablePages, PAGE_COUNT); //Split pages into groups of 12
        searchableButtons = new ArrayList<>();
        searchablePageSelect.forEach(p1 -> {
            List<PageButtonIdentity> pbi = new ArrayList<>();
            searchablePageSelect.get(searchablePageSelect.indexOf(p1)).forEach(p2 -> {
                pbi.add(new PageButtonIdentity(p2.getName(), p2.getPageNumber()));});
            searchableButtons.add(pbi);
        }); //Initialize buttons
        screen.update();
    }

    //Create and sort pages from Map by adding to List based on order of Categories
    public List<TMKPage> createPages(Map<String, List<KeyMapping>> catKeys, List<String> cat){
        List<TMKPage> t = new ArrayList<>();
        int i = 0;
        for(String c : cat){
            if(catKeys.get(c).size() > BUTTON_COUNT){
                List<List<KeyMapping>> partitions = Lists.partition(catKeys.get(c), BUTTON_COUNT);
                for(List<KeyMapping> partition : partitions){
                    t.add(new TMKPage(c, i++, partition));
                }
            }else{t.add(new TMKPage(c, i++, catKeys.get(c)));}
        }
        return t;
    }

    //Getters
    public List<TMKPage> getPages(){
        return pages;
    }
    public List<List<TMKPage>> getPageSelect(){
        return pageSelect;
    }
    public List<List<PageButtonIdentity>> getPageButtons(){
        return pageButtons;
    }
    public List<TMKPage> getSearchablePages(){
        return searchablePages;
    }
    public List<List<TMKPage>> getSearchablePageSelect(){
        return searchablePageSelect;
    }
    public List<List<PageButtonIdentity>> getSearchableButtons(){
        return searchableButtons;
    }
}