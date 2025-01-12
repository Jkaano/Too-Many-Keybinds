package io.github.jkaano.toomanykeybinds.client.screen;

import com.google.common.collect.Lists;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.button.ScreenButton;
import io.github.jkaano.toomanykeybinds.client.handler.ClientModHandler;
import io.github.jkaano.toomanykeybinds.client.pages.Page;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectKeyBindScreen extends Screen{

    private Screen lastScreen;
    private List<KeyMapping> keyBinds;
    private List<List<KeyMapping>> keyBindsPartition;

    private int currentPartition = 0;
    private final int partitionCount;

    private Page page;
    private KeyMapping key;
    private KeyMapping iKey;

    private int index;

    public SelectKeyBindScreen(Screen lastScreen, Page page, int index){
        super(Component.literal("SelectKeyBindScreen"));
        this.lastScreen = lastScreen;
        this.page = page;
        this.index = index;

        key = (page.keyList != null && index < page.keyList.size()) ? page.keyList.get(index) : null;
        iKey = key;

        keyBinds = ClientModHandler.pageHandler.getKeyBinds();
        keyBindsPartition = Lists.partition(keyBinds, 32);
        partitionCount = Math.max(0, keyBindsPartition.size()-1);
    }

    @Override
    protected void init(){
        super.init();

        setButtons();

        ScreenButton nextPage = new ScreenButton(">"){
            @Override
            protected void handleButton(Button button){
                if(currentPartition < partitionCount) currentPartition++;
                else currentPartition = 0;
                update();
            }
        };
        nextPage.setRegion(
                (width - 20)/2 + 112,
                height - 26,
                20, 20
        );
        addRenderableWidget(nextPage.addButton());

        ScreenButton previousPage = new ScreenButton("<"){
            @Override
            protected void handleButton(Button button){
                if(currentPartition > 0) currentPartition--;
                else currentPartition = partitionCount;
                update();
            }
        };
        previousPage.setRegion(
                (width - 20)/2 - 112,
                height - 26,
                20, 20
        );
        addRenderableWidget(previousPage.addButton());

        ScreenButton unset = new ScreenButton(Component.translatable("gui." + TooManyKeybinds.MODID + ".unset").getString()){
            @Override
            protected void handleButton(Button button){
                key = null;
                onClose();
            }
        };
        unset.setRegion(
                width/2 + 124,
                height - 26,
                75, 20
        );
        if(key != null) addRenderableWidget(unset.addButton());

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
        int index = 0;
        for(KeyMapping keyMapping : keyBindsPartition.get(currentPartition)){
            int col = index/8;
            int rowOffset = 8*25*col;
            ScreenButton button = new ScreenButton(keyMapping.getName()){
                @Override
                protected void handleButton(Button button){
                    key = keyMapping;
                    onClose();
                }
            };
            button.setRegion(
                    (width-415)/2 + (col * 105),
                    (height-195)/2 - 13 + (index++ * 25) - rowOffset,
                    100, 20
            );
            addRenderableWidget(button.addButton());
        }
    }

    private void update(){
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
        if(page.keyList == null) page.keyList = new ArrayList<>();
        if(key != null){
            if(iKey == null) page.keyList.add(key);
            else{
                page.keyList.remove(index);
                page.keyList.add(index, key);
            }
        }else{
            if(iKey != null) page.keyList.remove(index);
        }

        if(lastScreen instanceof EditCustomPageScreen) ((EditCustomPageScreen) lastScreen).update();

        assert minecraft != null;
        minecraft.setScreen(lastScreen);
    }

}
