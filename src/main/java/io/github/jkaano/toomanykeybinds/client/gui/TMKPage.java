package io.github.jkaano.toomanykeybinds.client.gui;

import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class TMKPage {

    private final int page_number;
    private final String name;

    private final List<KeyMapping> keys;
    private final List<String> keyNames = new ArrayList<>();
    private List<ButtonIdentity> buttonIdentities;

    private boolean hidden = false;

    public TMKPage(String name, int page_number, List<KeyMapping> keys){
        this.name = name;
        this.page_number = page_number;
        this.keys = keys;
        keys.forEach(key -> {this.keyNames.add(key.getName());});
        initButtons();
    }

    public void initButtons(){
        buttonIdentities = new ArrayList<>();
        keys.forEach(key -> {buttonIdentities.add(new ButtonIdentity(key));});
    }

    public void setHidden(boolean value){
        this.hidden = value;
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
        return "Page{name=" + name + ";keys=" + keyNames + ";page_number=" + page_number + ";hidden=" + hidden + "}";
    }

}
