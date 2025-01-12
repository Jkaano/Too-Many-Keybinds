package io.github.jkaano.toomanykeybinds.client.screen;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.button.ScreenButton;
import io.github.jkaano.toomanykeybinds.client.handler.ClientModHandler;
import io.github.jkaano.toomanykeybinds.client.pages.Page;
import io.github.jkaano.toomanykeybinds.client.pages.PageGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Comparator;

public class CustomPageScreen extends Screen{

    private final Screen lastScreen;
    private PageGroup customPages;

    int totalPages;
    int pagesPerPartition = 32;

    int currentPartition = 0;
    int partitionCount;

    public CustomPageScreen(Screen lastScreen){
        super(Component.literal("CustomPageScreen"));
        this.lastScreen = lastScreen;
        customPages = ClientModHandler.pageHandler.getCustomPages();
        customPages.updatePartitions(pagesPerPartition);
        totalPages = customPages.getPages().size();
        partitionCount = Math.max(0, customPages.getPartitionedPages().size() - 1);
    }

    @Override
    protected void init(){
        super.init();

        setButtons();

        ScreenButton add = new ScreenButton("+"){
            @Override
            protected void handleButton(Button button){
                customPages.add(new Page(Component.translatable("page." + TooManyKeybinds.MODID + ".new_page").getString(), totalPages));
                update();
            }
        };
        add.setRegion(
                width - 25,
                height - 26,
                20, 20
        );
        addRenderableWidget(add.addButton(Component.translatable("gui." + TooManyKeybinds.MODID + ".add_page")));

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

    public void update(){

        customPages = ClientModHandler.pageHandler.getCustomPages();
        customPages.updatePartitions(pagesPerPartition);
        totalPages = customPages.getPages().size();
        partitionCount = Math.max(0, customPages.getPartitionedPages().size() - 1);

        if(customPages.getPages() != null && !customPages.getPages().isEmpty()){
            customPages.getPages().sort(Comparator.comparingInt(Page::getIndex));
            int index = 0;
            for(Page page : customPages.getPages()){
                page.setIndex(index++);
            }
        }

        this.clearWidgets();
        init();
    }

    private void setButtons(){
        if(!customPages.getPages().isEmpty()){
            customPages.getPartitionedPages().get(currentPartition).forEach(page -> {
                ScreenButton pageButton = new ScreenButton(page.getName()){
                    @Override
                    protected void handleButton(Button button){
                        assert minecraft != null;
                        minecraft.setScreen(new EditCustomPageScreen(CustomPageScreen.this, page));
                    }
                };
                int col = page.getIndex()/8;
                int rowOffset = (8*25) * col;
                int partitionOffset = 4*105*currentPartition;
                pageButton.setRegion(
                        (width-415)/2 + (col * 105) - partitionOffset,
                        (height-195)/2 - 13 + (page.getIndex() * 25) - rowOffset,
                        100, 20
                );
                addRenderableWidget(pageButton.addButton());
            });
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float particleTick){
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, particleTick);
    }

    @Override
    public void onClose(){

        try{
            TooManyKeybinds.pageFileHandler.savePageGroup(customPages, TooManyKeybinds.pageFileHandler.customPagePath);
        }catch(IllegalStateException | IOException e){
            TooManyKeybinds.LOGGER.error("Couldn't save custom PageGroup to file: {}", e.toString());
        }

        ClientModHandler.pageHandler.createDisplayPages();

        assert minecraft != null;
        minecraft.setScreen(lastScreen);
    }

}
