package io.github.jkaano.toomanykeybinds.client.gui;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.screen.TMKScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PageButtonIdentity {

    private final Component name;
    private final int index;
    private int leftPos, topPos, width, height;
    private TMKScreen container;

    public PageButtonIdentity(String name, int index){

        this.name = Component.translatable(name);
        this.index = index;

    }

    public Button addButton(){
        return Button.builder(name, this::handleButton)
                .bounds(leftPos, topPos, width, height)
                .tooltip(Tooltip.create(name))
                .build();
    }

    public void setRegion(int leftPos, int topPos, int width, int height, TMKScreen screen){
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.width = width;
        this.height = height;
        this.container = screen;
    }

    public void handleButton(Button button){
        TooManyKeybinds.setPage(index);
        container.update();
    }

}
