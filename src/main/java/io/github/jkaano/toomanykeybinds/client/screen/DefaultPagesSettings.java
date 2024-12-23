package io.github.jkaano.toomanykeybinds.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class DefaultPagesSettings extends Screen{
    private static final Component TITLE = Component.literal("Edit Default Pages");

    private Screen lastScreen;

    public DefaultPagesSettings(Screen screen){
        super(TITLE);
        this.lastScreen = screen;
    }

    @Override
    protected void init(){
        super.init();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float particleTick){
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, particleTick);
    }

}
