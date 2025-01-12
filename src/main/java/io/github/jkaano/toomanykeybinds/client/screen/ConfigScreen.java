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
import org.jetbrains.annotations.NotNull;

public class ConfigScreen extends Screen{
    private static final Component TITLE = Component.translatable("gui." + TooManyKeybinds.MODID + ".config");
    private final Screen lastScreen;

    private static final int TOP_HEIGHT = 32;
    private static final int BOTTOM_OFFSET = 55;
    private static final int ITEM_HEIGHT = 25;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_OFFSET = 26;

    private static final int PADDING = 4;

    private OptionsList optionsList;

    private PageHandler pageHandler = ClientModHandler.pageHandler;

    private final int keyDelay = ClientConfig.PRESS_DELAY.get().intValue();
    private final boolean initialAutoOption;

    private final OptionInstance<Boolean> auto = OptionInstance.createBoolean("options." + TooManyKeybinds.MODID + ".auto_press", ClientConfig.AUTOMATIC_KEY_PRESS.get(), pBoolean -> ClientConfig.AUTOMATIC_KEY_PRESS.set(pBoolean));

    private final OptionInstance<Integer> delay = new OptionInstance<>("options." + TooManyKeybinds.MODID + ".key_delay" , OptionInstance.noTooltip(),
            (c, i) -> Component.translatable("options." + TooManyKeybinds.MODID + ".key_delay", Integer.toString(i)),
            new OptionInstance.IntRange(1, 60), Codec.DOUBLE.xmap((p_232007_) -> {
                return (int)(p_232007_ * 29.5D + 30.5D);},
            (p_232009_) -> {
                return ((double) p_232009_ - 30.5D) / 29.5D;}),
            keyDelay, (p_231951_) -> ClientConfig.PRESS_DELAY.set(p_231951_.longValue()));

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

        ScreenButton defaultPageSettings = new ScreenButton(Component.translatable("gui." + TooManyKeybinds.MODID + ".default_pages").getString()){
            @Override
            protected void handleButton(Button button){
                Minecraft.getInstance().setScreen(new DefaultPagesSettings(ConfigScreen.this));
            }
        };
        defaultPageSettings.setRegion(
                (width - BUTTON_WIDTH)/2,
                height - BOTTOM_OFFSET + PADDING,
                BUTTON_WIDTH/2 - 5, BUTTON_HEIGHT
        );
        addRenderableWidget(defaultPageSettings.addButton());

        ScreenButton customPageSettings = new ScreenButton(Component.translatable("gui." + TooManyKeybinds.MODID + ".custom_pages").getString()){
            @Override
            protected void handleButton(Button button){
                Minecraft.getInstance().setScreen(new CustomPageScreen(ConfigScreen.this));
            }
        };
        customPageSettings.setRegion(
                (width - BUTTON_WIDTH)/2 + BUTTON_WIDTH/2 + 5,
                height - BOTTOM_OFFSET + PADDING,
                BUTTON_WIDTH/2 - 5, BUTTON_HEIGHT
        );
        addRenderableWidget(customPageSettings.addButton());

        //Done button
        ScreenButton done = new ScreenButton(Component.translatable("gui." + TooManyKeybinds.MODID + ".done").getString()){
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
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float particleTicks){
        renderBackground(graphics);
        optionsList.render(graphics, mouseX, mouseY, particleTicks);
        graphics.drawCenteredString(this.font, this.title, width/2, (TOP_HEIGHT - this.font.lineHeight)/2, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, particleTicks);
    }

    @Override
    public void onClose(){
        save();
        assert minecraft != null;
        minecraft.setScreen(lastScreen instanceof MenuScreen ? new MenuScreen() : lastScreen);
    }

    public void save(){
        if(auto.get() != initialAutoOption && !ClientConfig.LOCK_AUTO.get()){
            ClientConfig.LOCK_AUTO.set(true);
        }
        ClientConfig.PRESS_DELAY.save();
        ClientConfig.AUTOMATIC_KEY_PRESS.save();
        ClientConfig.LOCK_AUTO.save();
        pageHandler.updateBinds();
    }

}
