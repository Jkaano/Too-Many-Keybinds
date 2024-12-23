package io.github.jkaano.toomanykeybinds.client.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public abstract class ScreenButton{

    private final Component name;

    private int x, y, width, height;

    public ScreenButton(String name){
        this.name = Component.translatable(name);
    }

    public void setRegion(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Button addButton(){
        return Button.builder(name, this::handleButton)
                .bounds(x, y, width, height)
                .tooltip(Tooltip.create(name))
                .build();
    }
    public Button addButton(Component tooltip){
        return Button.builder(name, this::handleButton)
                .bounds(x, y, width, height)
                .tooltip(Tooltip.create(tooltip))
                .build();
    }

    protected abstract void handleButton(Button button);
}
