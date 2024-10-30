package io.github.jkaano.toomanykeybinds.client.screen;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.TMKConstants;
import io.github.jkaano.toomanykeybinds.client.TMKPageHandler;
import io.github.jkaano.toomanykeybinds.client.gui.ButtonIdentity;
import io.github.jkaano.toomanykeybinds.client.gui.PageButtonIdentity;
import io.github.jkaano.toomanykeybinds.client.gui.TMKPage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class TMKScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + TooManyKeybinds.MODID + ".tmk_primary_screen");
    private Component PAGE_TITLE;

    private final ResourceLocation TEXTURE = new ResourceLocation(TooManyKeybinds.MODID, "textures/gui/tmk_primary_screen.png");

    private final int imageWidth = 225, imageHeight = 140;
    private int leftPos, topPos;

    private int page;
    private int pageSelect;

    private final TMKPageHandler PAGE_HANDLER = TooManyKeybinds.pageHandler;
    private final TMKPage[] TMK_PAGES = PAGE_HANDLER.getPages();
    private final TMKPage[][] PAGE_SELECT = PAGE_HANDLER.getPageSelect();

    public TMKScreen(){
        super(TITLE);
    }

    @Override
    protected void init(){
        super.init();

        page = TooManyKeybinds.getPage();
        pageSelect = TooManyKeybinds.getPageSelect();

        ButtonIdentity[] pageButtons = TMK_PAGES[page].getButtons();

        PAGE_TITLE = Component.translatable(TMK_PAGES[page].getName());

        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;

        if(this.minecraft == null) return;
        Level level = this.minecraft.level;
        if(level == null) return;

        EditBox search = new EditBox(font, leftPos, topPos - 30, 200, 20, Component.literal("what"));
        addRenderableWidget(search);
        //Draw buttons belonging to a page
        int column = 0;
        for(ButtonIdentity buttons: pageButtons){
            if(column < 5){
                buttons.setRegion(
                        leftPos + imageWidth/2 - 105,
                        topPos + 16 + 20*column,
                        100, 15);
                addRenderableWidget(buttons.addButton());
            }else{
                buttons.setRegion(
                        leftPos + imageWidth/2 + 5,
                        topPos + 16 + 20*(column-5),
                        100, 15);
                addRenderableWidget(buttons.addButton());
            }
            column++;
        }

        //Create next and previous buttons
        addRenderableWidget(
                Button.builder(Component.literal("Next"), this::handleNextButton)
                        .bounds(leftPos + (imageWidth/2) + 15, topPos + imageHeight - 23, 50, 15)
                        .tooltip(Tooltip.create(Component.literal("Next Page")))
                        .build());

        addRenderableWidget(
                Button.builder(Component.literal("Back"), this::handleBackButton)
                        .bounds(leftPos + (imageWidth/2) - 65, topPos + imageHeight - 23, 50, 15)
                        .tooltip(Tooltip.create(Component.literal("Previous Page")))
                        .build());

        //Add page select buttons
        for(int i = 0; i < PAGE_SELECT[pageSelect].length; i++){
            int PAGE_COUNT = TMKConstants.PAGE_COUNT;
            PageButtonIdentity selectPage = new PageButtonIdentity(
                    PAGE_SELECT[pageSelect][i].getName(), i + (pageSelect*PAGE_COUNT),
                    leftPos-91, topPos + 16 + (17*i),
                    90, 15, this);
            addRenderableWidget(selectPage.addButton());
        }

        //Create next and previous buttons for page select
        addRenderableWidget(
                Button.builder(Component.literal(">"), this::handleNextPageButton)
                        .bounds(leftPos - 50, topPos + imageHeight - 23, 15, 15)
                        .tooltip(Tooltip.create(Component.literal("Next Page")))
                        .build());

        addRenderableWidget(
                Button.builder(Component.literal("<"), this::handleBackPageButton)
                        .bounds(leftPos - 91, topPos + imageHeight - 23, 15, 15)
                        .tooltip(Tooltip.create(Component.literal("Previous Page")))
                        .build());


    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        renderBackground(graphics);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawString(this.font, PAGE_TITLE,
                leftPos + imageWidth/2 - (PAGE_TITLE.getString().length()*5)/2,
                topPos + 7, 0xFFFFFF,
                true);
        graphics.drawString(this.font, String.valueOf(page+1),
                leftPos + imageWidth/2 - (String.valueOf(page+1).length()*6)/2,
                topPos + imageHeight - 20, 0xFFFFFF,
                true);

    }

    //Clear widgets and reinitialize
    public void update(){
        this.clearWidgets();
        this.init();
    }

    //Handle next and previous buttons
    private void handleNextButton(Button button){
        if(page < TMK_PAGES.length - 1){
            TooManyKeybinds.setPage(page+1);
            update();
        }else if(page == TMK_PAGES.length-1){
            TooManyKeybinds.setPage(0);
            update();
        }
    }
    private void handleBackButton(Button button){
        if(page > 0){
            TooManyKeybinds.setPage(page-1);
            update();
        }else if(page == 0){
            TooManyKeybinds.setPage(TMK_PAGES.length - 1);
            update();
        }
    }

    //Handle next and previous page select buttons
    private void handleNextPageButton(Button button){
        if(pageSelect < PAGE_SELECT.length - 1){
            TooManyKeybinds.setPageSelect(pageSelect+1);
            update();
        }else if(pageSelect == PAGE_SELECT.length - 1){
            TooManyKeybinds.setPageSelect(0);
            update();
        }
    }

    private void handleBackPageButton(Button button){
        if(pageSelect > 0){
            TooManyKeybinds.setPageSelect(pageSelect - 1);
            update();
        }else if(pageSelect == 0){
            TooManyKeybinds.setPageSelect(PAGE_SELECT.length - 1);
            update();
        }
    }

    @Override
    public boolean isPauseScreen(){
        return false;
    }

}
