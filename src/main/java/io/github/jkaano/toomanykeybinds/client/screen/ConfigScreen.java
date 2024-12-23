package io.github.jkaano.toomanykeybinds.client.screen;

import com.mojang.serialization.Codec;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.button.ScreenButton;
import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import io.github.jkaano.toomanykeybinds.client.handler.ClientModHandler;
import io.github.jkaano.toomanykeybinds.client.pages.PageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen{
    private static final Component TITLE = Component.literal("Too Many Keybinds Config");
    private final Screen lastScreen;

    private static final int TOP_HEIGHT = 32;
    private static final int BOTTOM_OFFSET = 32;
    private static final int ITEM_HEIGHT = 25;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_OFFSET = 26;

    private OptionsList optionsList;

    private PageHandler pageHandler = ClientModHandler.pageHandler;

    private final int keyDelay = ClientConfig.PRESS_DELAY.get().intValue();
    private final boolean initialAutoOption;

    private final OptionInstance<Boolean> auto = OptionInstance.createBoolean("options." + TooManyKeybinds.MODID + ".auto_press", ClientConfig.AUTOMATIC_KEY_PRESS.get());

    private final OptionInstance<Integer> delay = new OptionInstance<>("options." + TooManyKeybinds.MODID + ".key_delay" , OptionInstance.noTooltip(),
            (c, i) -> Component.translatable("options." + TooManyKeybinds.MODID + ".key_delay", Integer.toString(i)),
            new OptionInstance.IntRange(1, 60), Codec.DOUBLE.xmap((p_232007_) -> {
                return (int)(p_232007_ * 29.5D + 30.5D);},
            (p_232009_) -> {
                return ((double) p_232009_.intValue() - 30.5D) / 29.5D;}),
            keyDelay, (p_231951_) -> {
                Minecraft.getInstance().levelRenderer.needsUpdate();});

    private final OptionInstance<?>[] options = new OptionInstance[]{auto, delay};

    public ConfigScreen(Screen previous){
        super(TITLE);
        this.lastScreen = previous;
        initialAutoOption = ClientConfig.AUTOMATIC_KEY_PRESS.get();
    }

    @Override
    protected void init(){
        super.init();

        assert minecraft != null;
        optionsList = new OptionsList(minecraft, width, height,
                TOP_HEIGHT, height - BOTTOM_OFFSET, ITEM_HEIGHT);
        optionsList.addSmall(options);
        addRenderableWidget(optionsList);

        //Done button
        ScreenButton done = new ScreenButton("Done"){
            @Override
            protected void handleButton(Button button){
                onClose();
            }
        };
        done.setRegion(
                (width - BUTTON_WIDTH)/2,
                height - DONE_BUTTON_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT
        );
        addRenderableWidget(done.addButton());

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float particleTicks){
        renderBackground(graphics);
        optionsList.render(graphics, mouseX, mouseY, particleTicks);
        graphics.drawCenteredString(this.font, this.title, width/2, (TOP_HEIGHT - this.font.lineHeight)/2, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, particleTicks);
    }

    @Override
    public void onClose(){
        assert minecraft != null;
        save();
        minecraft.setScreen(lastScreen);
    }

    public void save(){
        ClientConfig.PRESS_DELAY.set((long)delay.get());
        ClientConfig.AUTOMATIC_KEY_PRESS.set(auto.get());
        if(auto.get() != initialAutoOption && !ClientConfig.LOCK_AUTO.get()){
            ClientConfig.LOCK_AUTO.set(true);
        }
        ClientConfig.PRESS_DELAY.save();
        ClientConfig.AUTOMATIC_KEY_PRESS.save();
        ClientConfig.LOCK_AUTO.save();
        pageHandler.updateBinds();
    }

}
