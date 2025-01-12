package io.github.jkaano.toomanykeybinds.client.screen;

import com.mojang.serialization.Codec;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.button.ScreenButton;
import io.github.jkaano.toomanykeybinds.client.handler.ClientModHandler;
import io.github.jkaano.toomanykeybinds.client.pages.Page;
import io.github.jkaano.toomanykeybinds.client.pages.PageGroup;
import io.github.jkaano.toomanykeybinds.client.pages.PageHandler;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Comparator;

public class DefaultPagesSettings extends Screen{
    private static final Component TITLE = Component.literal("Edit Default Pages");

    private Screen lastScreen;

    private static final int TOP_HEIGHT = 32;
    private static final int BOTTOM_OFFSET = 32;
    private static final int ITEM_HEIGHT = 25;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_OFFSET = 26;

    private OptionsList optionsList;

    private final PageHandler pageHandler = ClientModHandler.pageHandler;
    private PageGroup defaultPages;

    public DefaultPagesSettings(Screen screen){
        super(TITLE);
        this.lastScreen = screen;

        defaultPages = pageHandler.getDefaultPages();
    }

    @Override
    protected void init(){
        super.init();

        assert minecraft != null;
        optionsList = new OptionsList(minecraft, width, height,TOP_HEIGHT, height-BOTTOM_OFFSET, ITEM_HEIGHT);

        setButtons();

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

    private void setButtons(){
        defaultPages.getPages().forEach(page -> {
            OptionInstance<?>[] pageSetting = new OptionInstance[]{
                    OptionInstance.createBoolean(page.getName(), !page.getHidden(), value -> page.setHidden(!page.getHidden())),
                    new OptionInstance<>("options." + TooManyKeybinds.MODID + ".page_index" , OptionInstance.noTooltip(),
                    (c, v) -> Component.translatable("options." + TooManyKeybinds.MODID + ".page_index", Integer.toString(v+1)),
                    new OptionInstance.IntRange(0, defaultPages.getPages().size()-1), Codec.DOUBLE.xmap((p_232007_) -> (int)(p_232007_ * 29.5D + 30.5D),
                    (p_232009_) -> ((double) p_232009_ - 30.5D) / 29.5D),
                    page.getIndex(), page::setIndex)
            };
            optionsList.addSmall(pageSetting);
        });
        addRenderableWidget(optionsList);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float particleTick){
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, particleTick);
    }

    @Override
    public void onClose(){
        defaultPages.getPages().sort(Comparator.comparingInt(Page::getIndex));
        save();
        ClientModHandler.pageHandler.createDisplayPages();

        assert minecraft != null;
        minecraft.setScreen(lastScreen);
    }

    private void save(){
        try{
            TooManyKeybinds.pageFileHandler.savePageGroup(defaultPages, TooManyKeybinds.pageFileHandler.defaultPagesPath);
        }catch(IOException e){
            TooManyKeybinds.LOGGER.error("Couldn't save default PageGroup from settings menu: {}", e.toString());
        }
    }

}
