package io.github.jkaano.toomanykeybinds.client.screen;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.KeyHandler;
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
    private final ResourceLocation TEXTURE1 = new ResourceLocation(TooManyKeybinds.MODID, "textures/gui/tmk_screen.png");

    private final int imageWidth = 225, imageHeight = 140;
    private int leftPos, topPos;

    private int page;
    private int pageSelect;

    private final TMKPageHandler PAGE_HANDLER = TooManyKeybinds.pageHandler;
    private TMKPage[] TMK_PAGES = PAGE_HANDLER.getPages();
    private TMKPage[][] PAGE_SELECT = PAGE_HANDLER.getPageSelect();

    private final KeyHandler KEY_HANDLER = TooManyKeybinds.tmkHandler;

    private EditBox search;
    private boolean searching = TooManyKeybinds.searching;
    private String lookup = "";

    public TMKScreen(){
        super(TITLE);
    }

    @Override
    protected void init(){
        super.init();

        PageButtonIdentity[][] pageButtonIdentities;
        if(!searching){
            TMK_PAGES = PAGE_HANDLER.getPages();
            PAGE_SELECT = PAGE_HANDLER.getPageSelect();
            pageButtonIdentities = PAGE_HANDLER.getButtons();
        }else{
            TMK_PAGES = PAGE_HANDLER.getSearchablePages();
            PAGE_SELECT = PAGE_HANDLER.getSearchablePageSelect();
            pageButtonIdentities = PAGE_HANDLER.getSearchableButtons();
        }

        page = TooManyKeybinds.getPage();
        pageSelect = TooManyKeybinds.getPageSelect();

        ButtonIdentity[] pageButtons = TMK_PAGES[page].getButtons();
        PageButtonIdentity[] pageSelectButton = pageButtonIdentities[pageSelect];

        PAGE_TITLE = Component.translatable(TMK_PAGES[page].getName());

        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;

        if(this.minecraft == null) return;
        Level level = this.minecraft.level;
        if(level == null) return;

        search = new EditBox(font, leftPos+1, topPos - 21, imageWidth - 52, 19,
                search, Component.literal("Search Keybind"));
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
        for(int i = 0; i < pageSelectButton.length; i++){
            int PAGE_COUNT = TMKConstants.PAGE_COUNT;
            if(i < PAGE_COUNT/2){
                pageSelectButton[i].setRegion(
                        leftPos-91,
                        topPos + 16 + (17*i),
                        90, 15, this);
                addRenderableWidget(pageSelectButton[i].addButton());
            }else{
                pageSelectButton[i].setRegion(
                        leftPos+imageWidth+1,
                        topPos + 16 + (17*i) - (17*PAGE_COUNT/2),
                        90, 15, this);
                addRenderableWidget(pageSelectButton[i].addButton());
            }
        }

        //Create next and previous buttons for page select
        addRenderableWidget(
                Button.builder(Component.literal(">"), this::handleNextPageButton)
                        .bounds(leftPos + imageWidth + 1, topPos + imageHeight - 23, 15, 15)
                        .tooltip(Tooltip.create(Component.literal("Next Page Select")))
                        .build());

        addRenderableWidget(
                Button.builder(Component.literal("<"), this::handleBackPageButton)
                        .bounds(leftPos - 16, topPos + imageHeight - 23, 15, 15)
                        .tooltip(Tooltip.create(Component.literal("Previous Page Select")))
                        .build());

        addRenderableWidget(
                Button.builder(Component.literal("Search"), this::handleSearchButton)
                        .bounds(leftPos + imageWidth - 49, topPos - 21, 50, 20)
                        .tooltip(Tooltip.create(Component.literal("Search")))
                        .build());


    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        renderBackground(graphics);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        graphics.blit(TEXTURE1, leftPos-97, topPos+5, 0, 0, 97, 130);
        graphics.blit(TEXTURE1, leftPos+imageWidth, topPos+5, 97, 0, 97, 130);
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawString(this.font, PAGE_TITLE,
                leftPos + imageWidth/2 - (PAGE_TITLE.getString().length()*5)/2,
                topPos + 7, 0xFFFFFF,
                true);
        graphics.drawString(this.font, String.valueOf(page+1),
                leftPos + imageWidth/2 - (String.valueOf(page+1).length()*6)/2,
                topPos + imageHeight - 25, 0xFFFFFF,
                true);
        graphics.drawString(this.font, "-",
                leftPos+imageWidth/2-3,
                topPos+imageHeight-20, 0xFFFFFF);
        graphics.drawString(this.font, String.valueOf(TMK_PAGES.length),
                leftPos + imageWidth/2 - (String.valueOf(TMK_PAGES.length).length()*6)/2,
                topPos + imageHeight - 15, 0xFFFFFF,
                true);

    }

    @Override
    public void tick(){
        search.tick();
        if(!search.getValue().equals(lookup)){
            lookup = search.getValue();
            searching = !lookup.isEmpty();
        }
    }

    //Clear widgets and reinitialize
    public void update(){
        this.clearWidgets();
        this.init();
        System.out.println("Updating screen");
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

    private void handleSearchButton(Button button){
        TooManyKeybinds.searching = searching;
        TooManyKeybinds.setPage(0);
        TooManyKeybinds.setPageSelect(0);
        KEY_HANDLER.updateSearchable(lookup, this);
        System.out.println("Updating search value to: " + lookup);
    }

    @Override
    public boolean isPauseScreen(){
        return false;
    }

}

