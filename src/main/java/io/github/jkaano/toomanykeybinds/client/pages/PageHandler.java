package io.github.jkaano.toomanykeybinds.client.pages;

import com.google.common.collect.Lists;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.jkaano.toomanykeybinds.client.TMKConstants.BUTTON_COUNT;

public class PageHandler{

    private final Minecraft mcIn;
    private final PageFileHandler fileHandler;

    private List<KeyMapping> keyBinds;

    private PageGroup defaultPages;
    private PageGroup searchedPages;
    private PageGroup displayPages;
    private PageGroup customPages;

    public PageHandler(){
        mcIn = Minecraft.getInstance();
        fileHandler = TooManyKeybinds.pageFileHandler;

        updateBinds();
        createDefaultPages();
        createCustomPages();
        createDisplayPages();
    }

    public void updateBinds(){
        keyBinds = Arrays.asList(mcIn.options.keyMappings);
        if(defaultPages != null){
            for(Page page : defaultPages.getPages()){
                List<KeyMapping> pageKeys = new ArrayList<>();
                for(KeyMapping key : keyBinds){
                    if(page.getKeys().contains(key.getName())){
                        pageKeys.add(key);
                    }
                }
                page.setButtons(pageKeys);
            }
        }
    }

    public void createDisplayPages(){
        List<Page> combinedPages = new ArrayList<>();
        if(!customPages.getPages().isEmpty()) combinedPages.addAll(customPages.getPages());
        combinedPages.addAll(defaultPages.getPages());
        combinedPages.removeIf(Page::getHidden);
        combinedPages.removeIf(page -> page.getKeys().isEmpty());
        int index = 0;
        for(Page page : combinedPages){page.setDisplayIndex(index++);}
        displayPages = new PageGroup(combinedPages);
    }

    public void createCustomPages(){
        try{
            customPages = fileHandler.readPageGroup(fileHandler.customPagePath);
        }catch(IllegalStateException | IOException e){
            TooManyKeybinds.LOGGER.error("Couldn't read custom page file: {}", e.toString());
        }
        if(customPages == null) customPages = new PageGroup(new ArrayList<>());
        customPages.setMessage("Add, edit, or remove user made pages");
        if(!customPages.getPages().isEmpty()){
            customPages.getPages().sort(Comparator.comparingInt(Page::getIndex));
            keyBinds = Arrays.asList(mcIn.options.keyMappings);
            customPages.getPages().forEach(page -> {
                List<KeyMapping> pageKeys = new ArrayList<>();
                for(KeyMapping key: keyBinds){
                    if(page.getKeys().contains(key.getName())){
                        pageKeys.add(key);
                    }
                }
                if(!page.getKeys().isEmpty()){
                    page.keyList = pageKeys;
                    page.setButtons(pageKeys);
                }
            });
        }
        save(customPages, fileHandler.customPagePath);
    }

    public void createDefaultPages(){
        List<Page> pages = partitionPages(keyBinds);
        defaultPages = new PageGroup(pages);
        defaultPages.setMessage("Only change index and hidden - Can be used as list for custom keys");
        //Reads through the json file if it exists and then grabs saved values before overwriting
        //Overwrites the default pages in case mods are added or removed
        try{
            PageGroup checkPageStats = fileHandler.readPageGroup(fileHandler.defaultPagesPath);
            for(Page page : defaultPages.getPages()){
                for(Page readPage : checkPageStats.getPages()){
                    if(page.getName().equals(readPage.getName()) && page.getKeys().equals(readPage.getKeys())){
                        page.setHidden(readPage.getHidden());
                        page.setIndex(readPage.getIndex());
                        checkPageStats.getPages().remove(readPage);
                        break;
                    }
                }
            }
        }catch(IllegalStateException | IOException e){
            TooManyKeybinds.LOGGER.error("Couldn't create default pages: {}", e.toString());
        }

        defaultPages.getPages().sort(Comparator.comparingInt(Page::getIndex));

        save(defaultPages, fileHandler.defaultPagesPath);
    }

    public void searchPages(String lookup){
        List<KeyMapping> searchKeys = new ArrayList<>();
        List<Page> pages;
        if(!lookup.toLowerCase().contains("cat:")){
            for(KeyMapping key : keyBinds){
                if(Component.translatable(key.getName()).getString().toLowerCase().contains(lookup.toLowerCase())){
                    searchKeys.add(key);
                }
            }
            if(!searchKeys.isEmpty()){
                pages = partitionPages(searchKeys);
            }else{
                pages = partitionPages(keyBinds);
            }
        }else{
            pages = new ArrayList<>();
            String catLookup = lookup.replaceAll("cat:", "").toLowerCase().trim();
            for(Page page : defaultPages.getPages()){
                if(Component.translatable(page.getName()).getString().toLowerCase().contains(catLookup)){
                    pages.add(page);
                }
            }
            if(pages.isEmpty()) pages = partitionPages(keyBinds);
        }
        int index = 0;
        for(Page page : pages){
            page.setDisplayIndex(index++);
        }

        searchedPages = new PageGroup(pages);
    }

    public List<Page> partitionPages(List<KeyMapping> keys){
        List<Page> pages = new ArrayList<>();
        List<String> categories = keys.stream().map(KeyMapping::getCategory).distinct().toList();
        Map<String, List<KeyMapping>> categorizedKeys = keys.stream().collect(
                Collectors.groupingBy(KeyMapping::getCategory));
        int index = 0;
        for(String category : categories){
            if(categorizedKeys.get(category).size() > BUTTON_COUNT){
                List<List<KeyMapping>> partitions = Lists.partition(categorizedKeys.get(category), BUTTON_COUNT);
                for(List<KeyMapping> partition : partitions){
                    pages.add(new Page(category, index++, partition));
                }
            }else{pages.add(new Page(category, index++, categorizedKeys.get(category)));}
        }
        return pages;
    }

    public void save(PageGroup pageGroup, String filePath){
        try{
            fileHandler.savePageGroup(pageGroup, filePath);
        }catch(IOException e){
            TooManyKeybinds.LOGGER.error("Couldn't save PageGroup to file: {}", e.toString());
        }
    }

    //Getters
    public List<KeyMapping> getKeyBinds(){
        return keyBinds;
    }
    public PageGroup getDefaultPages(){
        return defaultPages;
    }
    public PageGroup getSearchedPages(){
        return searchedPages;
    }
    public PageGroup getDisplayPages(){
        return displayPages;
    }
    public PageGroup getCustomPages(){return customPages;}

}
