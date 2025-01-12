package io.github.jkaano.toomanykeybinds.client.screen;

import com.mojang.serialization.Codec;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.button.ScreenButton;
import io.github.jkaano.toomanykeybinds.client.handler.ClientModHandler;
import io.github.jkaano.toomanykeybinds.client.pages.Page;
import io.github.jkaano.toomanykeybinds.client.pages.PageGroup;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EditCustomPageScreen extends Screen{

    private Screen lastScreen;
    private Page page;

    private EditBox pageName;
    private PageGroup customPages = ClientModHandler.pageHandler.getCustomPages();

    private ScreenButton[] buttons = new ScreenButton[10];
    int keyCount;

    public EditCustomPageScreen(Screen lastScreen, Page page){
        super(Component.literal("EditCustomPageScreen"));
        this.lastScreen = lastScreen;
        this.page = page;

        keyCount = (page.keyList != null) ? page.keyList.size() : 0;
    }

    @Override
    protected void init(){
        super.init();
        assert minecraft != null;

        pageName = new EditBox(this.font, (width-200)/2, 10, 200, 20, Component.literal(page.getName()));
        pageName.setValue(page.getName());
        addRenderableWidget(pageName);

        OptionInstance<Integer> index = new OptionInstance<>("options." + TooManyKeybinds.MODID + ".page_index" , OptionInstance.noTooltip(),
                (c, v) -> Component.translatable("options." + TooManyKeybinds.MODID + ".page_index", Integer.toString(v+1)),
                new OptionInstance.IntRange(0, customPages.getPages().size()-1), Codec.DOUBLE.xmap((p_232007_) -> (int)(p_232007_ * 29.5D + 30.5D),
                (p_232009_) -> ((double) p_232009_ - 30.5D) / 29.5D),
                page.getIndex(), i -> {
            customPages.getPages().get(i).setIndex(page.getIndex());
            page.setIndex(i);
        });

        OptionInstance<Boolean> hidden = OptionInstance.createBoolean(page.getName(), !page.getHidden(), value -> page.setHidden(!page.getHidden()));

        addRenderableWidget(index.createButton(minecraft.options, width/2 - 101 , 35, 101));
        addRenderableWidget(hidden.createButton(minecraft.options, width/2, 35, 100));

        setButtons();

        ScreenButton delete = new ScreenButton(Component.translatable("gui." + TooManyKeybinds.MODID + ".delete").getString()){
            @Override
            protected void handleButton(Button button){
                customPages.remove(page);
                MenuScreen.currentPage = 0;
                MenuScreen.currentPartition = 0;
                onClose();
            }
        };
        delete.setRegion(
                width/2 + 102,
                height - 26,
                100, 20
        );
        addRenderableWidget(delete.addButton());

        ScreenButton done = new ScreenButton(Component.translatable("gui." + TooManyKeybinds.MODID + ".done").getString()){
            @Override
            protected void handleButton(Button button){ onClose(); }
        };
        done.setRegion(
                (width - 200)/2,
                height - 26,
                200, 20
        );
        addRenderableWidget(done.addButton());

    }

    private void setButtons(){
        int loopLength = Math.min(10, keyCount+1);
        for(int i = 0; i < loopLength; i++){
            int index = i;
            String name;
            if(page.keyList != null && i < page.keyList.size()) name = page.keyList.get(i).getName();
            else name = Component.translatable("gui." + TooManyKeybinds.MODID + ".unset").getString();

            ScreenButton button = new ScreenButton(name){
                @Override
                protected void handleButton(Button button){
                    if(!pageName.getValue().isEmpty()) page.setName(pageName.getValue());
                    assert minecraft != null;
                    minecraft.setScreen(new SelectKeyBindScreen(EditCustomPageScreen.this, page, index));
                }
            };
            buttons[i] = button;
        }
        for(int i = 0; i < loopLength; i++){
            int col = i / 5;
            int rowOffset = 125*col;
            buttons[i].setRegion(
                    (width - 150)/2 - 80 + 160*col,
                    25*i - rowOffset + 60,
                    150, 20
            );
            addRenderableWidget(buttons[i].addButton());
        }
    }

    public void update(){
        keyCount = (page.keyList != null) ? page.keyList.size() : 0;
        this.clearWidgets();
        init();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float particleTick){
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, particleTick);
    }

    @Override
    public void onClose(){

        if(!pageName.getValue().isEmpty()) page.setName(pageName.getValue());
        if(page.keyList != null){
            page.setKeys(page.keyList);
            page.setButtons(page.keyList);
        }
        if(lastScreen instanceof CustomPageScreen) ((CustomPageScreen) lastScreen).update();

        assert minecraft != null;
        minecraft.setScreen(lastScreen);
    }

}
