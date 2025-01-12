package io.github.jkaano.toomanykeybinds.client.pages;

import com.google.gson.annotations.Expose;
import io.github.jkaano.toomanykeybinds.client.button.ButtonHandler;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class Page{

    @Expose private String name; //This will be the category page
    @Expose private int index; //Position of page in page group
    @Expose private boolean hidden = false; //Show in menu
    @Expose private List<String> keys; //List of keys set to this page
    public List<KeyMapping> keyList;

    private int displayIndex;

    private List<ButtonHandler> buttonHandlers;

    public Page(String name, int index){
        this.name = name;
        this.index = index;
        keys = new ArrayList<>();
    }

    public Page(String name, int index, List<KeyMapping> keys){
        this(name, index);
        this.keyList = keys;
        setKeys(keys);
    }

    //Setters
    public void setKeys(List<KeyMapping> keys){
        this.keys.clear();
        keys.forEach(key -> {
            this.keys.add(key.getName());
        });
        setButtons(keys);
    }

    public void setButtons(List<KeyMapping> keys){
        buttonHandlers = new ArrayList<>();
        keys.forEach(key -> {
            buttonHandlers.add(new ButtonHandler(key));
        });
    }

    public void setHidden(boolean hidden){
        this.hidden = hidden;
    }

    public void setIndex(int index){
        this.index = index;
    }
    public void setDisplayIndex(int index){this.displayIndex = index;}
    public void setName(String name){ this.name = name;}

    //Getters
    public String getName(){
        return name;
    }

    public int getIndex(){
        return index;
    }
    public int getDisplayIndex(){return displayIndex;}

    public List<String> getKeys(){
        return keys;
    }

    public List<ButtonHandler> getButtonHandlers(){
        return buttonHandlers;
    }
    public boolean getHidden(){
        return hidden;
    }

    @Override
    public String toString(){
        return name + " : " + keys;
    }

}
