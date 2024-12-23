package io.github.jkaano.toomanykeybinds.client.pages;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;

import java.util.List;

import static io.github.jkaano.toomanykeybinds.client.TMKConstants.PAGE_COUNT;

public class PageGroup{

    @Expose private String message;

    @Expose private List<Page> pages;
    //Split pages into groups for page select options on the menu's wings
    private List<List<Page>> partitionedPages;

    public PageGroup(List<Page> pages){
        this.pages = pages;
        this.message = "";
        updatePartitions();
    }

    public void updatePartitions(){
        partitionedPages = Lists.partition(pages, PAGE_COUNT);
    }

    public void add(Page page){
        pages.add(page);
    }

    public void remove(Page page){
        pages.remove(page);
    }

    //Setters
    public void setMessage(String message){
        this.message = message;
    }

    //Getters
    public List<Page> getPages(){
        return pages;
    }
    public List<List<Page>> getPartitionedPages(){
        return partitionedPages;
    }

}
