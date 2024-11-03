package io.github.jkaano.toomanykeybinds.client.gui;

import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TMKPage {

    private KeyMapping[] keys;
    private final int page_number; //I might need this for when I allow users to create their own pages but idk yet
    private final String name;

    private ButtonIdentity[] buttons;

    public TMKPage(String name, int page_number){
        this.name = name;
        this.page_number = page_number;
        System.out.println("TMKPage generated: " + this.name);
        System.out.println("Page Number: " + this.page_number);
    }

    public TMKPage(String name, int page_number, KeyMapping[] keys){
        this.name = name;
        this.page_number = page_number;
        this.keys = keys;
        this.buttons = new ButtonIdentity[keys.length];
        System.out.println("TMKPage generated: " + this.name);
        System.out.println("Page Number: " + this.page_number);
        initButtons();
        System.out.println("TMK Page: " + this.name + " has generated " + this.buttons.length + " button(s)");
    }

    public void initButtons(){
        for(int i = 0; i < this.keys.length; i++){
            this.buttons[i] = new ButtonIdentity(this.keys[i]);
        }
    }

    public void setKeys(KeyMapping[] keys){
        this.keys = keys;
    }

    public void addKey(KeyMapping key){
        List<KeyMapping> temp = new ArrayList<>();
        Collections.addAll(temp, this.keys);
        temp.add(key);

        this.keys = temp.toArray(new KeyMapping[0]);
    }

    //Getters
    public KeyMapping[] getKeys(){
        return this.keys;
    }
    public String getName(){
        return this.name;
    }

    public int getPageNumber() {
        return page_number;
    }

    public ButtonIdentity[] getButtons(){
        return this.buttons;
    }

}
