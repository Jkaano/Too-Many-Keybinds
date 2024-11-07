package io.github.jkaano.toomanykeybinds.client.gui;

import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class TMKPage {

    private final int page_number;
    private final String name;

    private final List<KeyMapping> keys;
    private List<ButtonIdentity> buttonIdentities;

    public TMKPage(String name, int page_number, List<KeyMapping> keys){
        this.name = name;
        this.page_number = page_number;
        this.keys = keys;
        initButtons();
    }

    public void initButtons(){
        buttonIdentities = new ArrayList<>();
        keys.forEach(key -> {buttonIdentities.add(new ButtonIdentity(key));});
    }

    //Getters
    public String getName(){
        return this.name;
    }

    public int getPageNumber() {
        return page_number;
    }

    public List<ButtonIdentity> getButtonIdentities(){
        return this.buttonIdentities;
    }

    @Override
    public String toString(){
        return "Page{name='" + name + "', keys='" + keys + ", page number='" + page_number + "'}";
    }

}
