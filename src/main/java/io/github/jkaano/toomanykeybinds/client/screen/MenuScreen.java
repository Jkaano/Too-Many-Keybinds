package io.github.jkaano.toomanykeybinds.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.button.ButtonHandler;
import io.github.jkaano.toomanykeybinds.client.button.ScreenButton;
import io.github.jkaano.toomanykeybinds.client.handler.ClientModHandler;
import io.github.jkaano.toomanykeybinds.client.pages.Page;
import io.github.jkaano.toomanykeybinds.client.pages.PageGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static io.github.jkaano.toomanykeybinds.client.TMKConstants.BUTTON_COUNT;
import static io.github.jkaano.toomanykeybinds.client.TMKConstants.PAGE_COUNT;

public class MenuScreen extends Screen{

    private static final Component TITLE = Component.translatable("gui." + TooManyKeybinds.MODID + "tmk_primary_screen");

    private static final ResourceLocation DIRT = new ResourceLocation("textures/gui/light_dirt_background.png");
    private static final ResourceLocation BUTTON = new ResourceLocation("textures/gui/widgets.png");

    private PageGroup pages;
    private List<Page> partitionedPages;

    //Screen size and spacing
    private final int menuWidth = 240;
    private final int menuHeight = 138;
    private int x, y;
    private final int vPadding = 5;

    private final int buttonWidth = 100;
    private final int buttonHeight = 15;
    private final int hGap = ((menuWidth - (buttonWidth*2)) / 3)/2;

    private static int currentPage = 0;
    private static int pageCount;

    private static int currentPartition = 0;
    private static int partitionCount;

    private String pageTitle;

    //Buttons
    private List<ButtonHandler> buttonHandlers;
    private List<Button> widgets;
    private List<Button> partitionWidgets;
    private ScreenButton nextPage;
    private ScreenButton previousPage;
    private ScreenButton nextPartition;
    private ScreenButton previousPartition;
    private ScreenButton config;

    //Search
    private EditBox search;
    private String lookup = "";
    private boolean searching = false;

    public MenuScreen(){
        super(TITLE);

        pages = ClientModHandler.pageHandler.getDisplayPages();
        partitionedPages = pages.getPartitionedPages().get(currentPartition);
        pageTitle = Component.translatable(pages.getPages().get(currentPage).getName()).getString();
        widgets = new ArrayList<>();
        partitionWidgets = new ArrayList<>();

        initManeuverButtons();
        initSettingsButtons();
    }

    @Override
    protected void init(){
        super.init();

        if(this.minecraft == null) return;
        Level level = this.minecraft.level;
        if(level == null) return;

        x = (width - menuWidth) / 2;
        y = (height - menuHeight) / 2;

        pageCount = pages.getPages().size() - 1;
        partitionCount = pages.getPartitionedPages().size() - 1;

        buttonHandlers = pages.getPages().get(currentPage).getButtonHandlers();
        drawButtons(buttonHandlers);
        drawPartitionButtons(partitionedPages);

        //Add page maneuver buttons
        int pageNumberWidth;
        if(pageCount > 9){
            pageNumberWidth = this.font.width(String.valueOf(pageCount))/2;
        }else{
            pageNumberWidth = 0;
        }
        nextPage.setRegion(
                x + (menuWidth/2) + hGap + pageNumberWidth,
                y + menuHeight - (vPadding + buttonHeight),
                buttonWidth/2, buttonHeight);
        addRenderableWidget(nextPage.addButton());
        previousPage.setRegion(
                x + (menuWidth/2) - (hGap + (buttonWidth/2)) - pageNumberWidth,
                y + menuHeight - (vPadding + buttonHeight),
                buttonWidth/2, buttonHeight);
        addRenderableWidget(previousPage.addButton());

        nextPartition.setRegion(
                x + menuWidth + vPadding/2,
                y + menuHeight - (vPadding + buttonHeight-1),
                buttonHeight, buttonHeight);
        addRenderableWidget(nextPartition.addButton());
        previousPartition.setRegion(
                x - buttonHeight - vPadding/2,
                y + menuHeight - (vPadding + buttonHeight-1),
                buttonHeight, buttonHeight);
        addRenderableWidget(previousPartition.addButton());

        search = new EditBox(font, x+1, y-buttonHeight-1, menuWidth-2, buttonHeight, search,
                Component.literal("Search Keybind"));
        search.setSuggestion("Search");
        addRenderableWidget(search);

        //Settings buttons
        config.setRegion(
                x + menuWidth - vPadding/2 + (int)(buttonWidth*0.8) - buttonHeight,
                y + menuHeight - (vPadding + buttonHeight-1),
                buttonHeight, buttonHeight);
        addRenderableWidget(config.addButton(Component.literal("Configuration")));

    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        int titleRoom = 2*vPadding + this.font.lineHeight - 2;
        int wingHeight = 2+(1+PAGE_COUNT/2)*buttonHeight + (vPadding/2)*9;
        renderBackground(graphics);
        //Main frame
        graphics.blit(DIRT, x, y, 0,
                0f, 0f,
                menuWidth, menuHeight,
                16, 16);
        //Wings
        graphics.blit(DIRT, x-(int)(buttonWidth*0.8)-2, y+titleRoom-2-(vPadding/2), 0,
                0f, 0f,
                (int)(buttonWidth*0.8)+2, wingHeight,
                16, 16);
        graphics.blit(DIRT, x+menuWidth, y+titleRoom-2-(vPadding/2), 0,
                0f, 0f,
                (int)(buttonWidth*0.8)+2, wingHeight,
                16, 16);
        //Border
        drawBorders(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        //Page title/Category
        graphics.drawCenteredString(this.font, pageTitle,
                x + (menuWidth/2), y + vPadding, Color.WHITE.getRGB());

        //Page number
        graphics.drawCenteredString(this.font, String.valueOf(currentPage+1),
                x + (menuWidth/2), y + menuHeight - (this.font.lineHeight*2)-3, Color.WHITE.getRGB());
        graphics.drawCenteredString(this.font, String.valueOf(pageCount+1),
                x + (menuWidth/2), y + menuHeight - this.font.lineHeight-2, Color.WHITE.getRGB());
        graphics.drawCenteredString(this.font, "_", x+(menuWidth/2), y+menuHeight - this.font.lineHeight*2-2, Color.WHITE.getRGB());
    }

    @Override
    public void tick(){
        search.tick();
        if(!search.getValue().equals(lookup)){
            lookup = search.getValue();
            searching = !lookup.isEmpty();
            if(!searching){
                pages = ClientModHandler.pageHandler.getDisplayPages();
                search.setSuggestion("Search");
                update();
            }else{
                ClientModHandler.pageHandler.searchPages(lookup);
                pages = ClientModHandler.pageHandler.getSearchedPages();
                search.setSuggestion("");
            }
            currentPage = 0;
            currentPartition = 0;
            pageCount = pages.getPages().size() - 1;
            partitionedPages = pages.getPartitionedPages().get(currentPartition);
            partitionCount = pages.getPartitionedPages().size() - 1;
            update();
        }

    }

    private void update(){
        for(Button widget : widgets){
            this.removeWidget(widget);
        }
        for(Button widget : partitionWidgets){
            this.removeWidget(widget);
        }
        widgets.clear();
        partitionWidgets.clear();
        buttonHandlers = pages.getPages().get(currentPage).getButtonHandlers();
        pageTitle = Component.translatable(pages.getPages().get(currentPage).getName()).getString();
        partitionedPages = pages.getPartitionedPages().get(currentPartition);
        drawButtons(buttonHandlers);
        drawPartitionButtons(partitionedPages);
    }

    private void drawButtons(List<ButtonHandler> buttonHandlers){
        int titleRoom = 2*vPadding + this.font.lineHeight - 2;
        int column = 0;
        for(ButtonHandler button : buttonHandlers){
            if(column < BUTTON_COUNT/2){
                button.setRegion(
                        x + menuWidth/2 - buttonWidth - hGap,
                        y + titleRoom + (vPadding + buttonHeight)*column,
                        buttonWidth, buttonHeight);
            }else{
                button.setRegion(
                        x + menuWidth/2 + hGap,
                        y + titleRoom + (vPadding + buttonHeight)*(column-5),
                        buttonWidth, buttonHeight);
            }
            widgets.add(addRenderableWidget(button.addButton()));
            column++;
        }
    }

    private void drawPartitionButtons(List<Page> partitionedPages){
        int titleRoom = 2*vPadding + this.font.lineHeight - 2;
        int column = 0;
        ScreenButton button;
        for(Page partition : partitionedPages){
            button = new ScreenButton(partition.getName()){
                @Override
                protected void handleButton(Button button){
                    currentPage = partition.getDisplayIndex();
                    update();
                }
            };
            if(column < PAGE_COUNT/2){
                button.setRegion(x - (int)(buttonWidth*0.8), y + titleRoom + ((buttonHeight + vPadding/2)*column++), (int)(buttonWidth*0.8), buttonHeight);
            }else{
                button.setRegion(x + menuWidth, y + titleRoom + ((buttonHeight + vPadding/2)*(column++-(PAGE_COUNT/2))), (int)(buttonWidth*.8), buttonHeight);
            }
            partitionWidgets.add(addRenderableWidget(button.addButton()));
        }
    }

    private void initManeuverButtons(){
        nextPage = new ScreenButton("Next"){
            @Override
            protected void handleButton(Button button){
                if(currentPage < pageCount){currentPage++;}
                else{currentPage = 0;}
                update();
            }
        };
        previousPage = new ScreenButton("Back"){
            @Override
            protected void handleButton(Button button){
                if(currentPage > 0){currentPage--;}
                else{currentPage = pageCount;}
                update();
            }
        };

        nextPartition = new ScreenButton(">"){
            @Override
            protected void handleButton(Button button){
                if(currentPartition < partitionCount){currentPartition++;}
                else{currentPartition = 0;}
                update();
            }
        };
        previousPartition = new ScreenButton("<"){
            @Override
            protected void handleButton(Button button){
                if(currentPartition > 0){currentPartition--;}
                else{currentPartition = partitionCount;}
                update();
            }
        };
    }

    private void initSettingsButtons(){
        Minecraft mcIn = Minecraft.getInstance();
        config = new ScreenButton("C"){
            @Override
            protected void handleButton(Button button){
                mcIn.setScreen(new ConfigScreen(MenuScreen.this));
            }
        };
    }

    private void drawBorders(@NotNull GuiGraphics graphics){
        int titleRoom = 2*vPadding + this.font.lineHeight - 2;
        int wingHeight = 2+(1+PAGE_COUNT/2)*buttonHeight + (vPadding/2)*9;
        int wingWidth = (int)(buttonWidth*0.8);
        int wallHSegments = (menuHeight-4)/14;
        int wallWSegments = (menuWidth-4)/193;
        int wingHSegments = (wingHeight-4)/14;
        //Main window
        graphics.blit(BUTTON, x, y, 0, 1f, 67f, 2, 2, 256, 256); //Top-left corner
        graphics.blit(BUTTON, x+menuWidth-2, y, 0, 197f, 67f, 2, 2, 256, 256); //Top-right corner
        for(int i = 0; i <= wallHSegments; i++){
            int segH = 14;
            if(i == wallHSegments){
                segH = (menuHeight-4) % (i*segH);
            }
            graphics.blit(BUTTON, x, y + 2 + (14 * i), 0, 1f, 69f, 2, segH, 256, 256); //Left wall
        }
        for(int i = 0; i <= wallHSegments; i++){
            int segH = 14;
            if(i == wallHSegments){
                segH = (menuHeight-4) % (i*segH);
            }
            graphics.blit(BUTTON, x+menuWidth-2, y + 2 + (14 * i), 0, 197f, 69f, 2, segH, 256, 256); //Right Wall
        }
        for(int i = 0; i <= wallWSegments; i++){
            int segW = 193;
            if(i == wallWSegments){
                segW = (menuWidth-4) % (i*segW);
            }
            graphics.blit(BUTTON, x + 2 + (193*i), y, 0, 3f, 67f, segW, 2, 256, 256); //Top Wall
        }
        for(int i = 0; i <= wallWSegments; i++){
            int segW = 193;
            if(i == wallWSegments){
                segW = menuWidth % (i*segW);
            }
            graphics.blit(BUTTON, x + (193*i), y+menuHeight-2, 0, 3f, 83f, segW, 2, 256, 256); //Bottom Wall
        }
        //Side windows
        graphics.blit(BUTTON, x-(int)(buttonWidth*0.8)-2, y+titleRoom-4, 0, 1f, 67f, 2, 2, 256, 256); //Top-left corner
        graphics.blit(BUTTON, x-(int)(buttonWidth*0.8)-2, y+menuHeight-2, 0, 1f, 83f, 2, 2, 256, 256); //Bottom-left corner
        graphics.blit(BUTTON, x+menuWidth+(int)(buttonWidth*0.8), y+titleRoom-4, 0, 197f, 67f, 2, 2, 256, 256); //Top-right corner
        graphics.blit(BUTTON, x+menuWidth+(int)(buttonWidth*0.8), y+menuHeight-2, 0, 197f, 83f, 2, 2, 256, 256); //Bottom-right corner
        for(int i = 0; i <= wingHSegments; i++){
            int segH = 14;
            if(i == wingHSegments){
                segH = (wingHeight-4) % (i*segH);
            }
            graphics.blit(BUTTON, x-(int)(buttonWidth*0.8)-2, y + titleRoom-2 + (14 * i), 0, 1f, 69f, 2, segH, 256, 256); //Left wall
        }
        for(int i = 0; i <= wingHSegments; i++){
            int segH = 14;
            if(i == wingHSegments){
                segH = (wingHeight-4) % (i*segH);
            }
            graphics.blit(BUTTON, x+menuWidth+(int)(buttonWidth*0.8), y + titleRoom - 2 + (14 * i), 0, 197f, 69f, 2, segH, 256, 256); //Right Wall
        }
        graphics.blit(BUTTON, x-wingWidth, y+titleRoom-4, 0, 3f, 67f, wingWidth, 2, 256, 256); //Top left wing
        graphics.blit(BUTTON, x+menuWidth, y+titleRoom-4, 0, 3f, 67f, wingWidth, 2, 256, 256); //Top right wing
        graphics.blit(BUTTON, x-wingWidth, y+menuHeight-2, 0, 3f, 83f, wingWidth, 2, 256, 256); //Bottom left wing
        graphics.blit(BUTTON, x+menuWidth, y+menuHeight-2, 0, 3f, 83f, wingWidth, 2, 256, 256); //Bottom right wing
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods){

        if(Keybindings.INSTANCE.openTMK.isActiveAndMatches(InputConstants.getKey(key, scancode)) && Minecraft.getInstance().player != null && !searching){
            Minecraft.getInstance().player.closeContainer();
            return true;
        }

        return super.keyPressed(key, scancode, mods);
    }

    @Override
    public boolean isPauseScreen(){
        return false;
    }

}
