package io.github.jkaano.toomanykeybinds.client.screen;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.KeybindingsReader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class TMKPrimaryScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + TooManyKeybinds.MODID + ".tmk_primary_screen");
    private static Component PAGE_TITLE;

    private static final ResourceLocation TEXTURE = new ResourceLocation(TooManyKeybinds.MODID, "textures/gui/tmk_primary_screen.png");

    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private static final KeybindingsReader KEYBINDINGS = new KeybindingsReader();
    private static final KeyMapping[][] SPLITCAT = KEYBINDINGS.getSplitPagesKeys();
    private static final String[] PAGES = KEYBINDINGS.getPages();

    private Button button;

    public TMKPrimaryScreen(){
        super(TITLE);

        this.imageWidth = 225;
        this.imageHeight = 140;
    }

    @Override
    protected void init(){
        super.init();

        PAGE_TITLE = Component.translatable(PAGES[TooManyKeybinds.page]);

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        if(this.minecraft == null) return;
        Level level = this.minecraft.level;
        if(level == null) return;

        boolean firstColumn = true;
        for(int i = 0; i < SPLITCAT[TooManyKeybinds.page].length; i++){
            if(firstColumn) {
                ButtonIdentity newButton = new ButtonIdentity(
                        //KEYS[TooManyKeybinds.page][i],
                        SPLITCAT[TooManyKeybinds.page][i],
                        leftPos + imageWidth/2 - 105,
                        topPos + 16 + 20*i,
                        100,
                        15
                );
                addRenderableWidget(newButton.addButton());
                if(i == 4){firstColumn = false;}
            }else{
                ButtonIdentity newButton = new ButtonIdentity(
                        //KEYS[TooManyKeybinds.page][i],
                        SPLITCAT[TooManyKeybinds.page][i],
                        leftPos + imageWidth/2 + 5,
                        topPos + 16 + 20*(i-5),
                        100,
                        15
                );
                addRenderableWidget(newButton.addButton());
            }
        }

        addRenderableWidget(
                Button.builder(Component.literal("Next"), this::handleNextButton)
                        .bounds(this.leftPos + (this.imageWidth/2) + 15, this.topPos + this.imageHeight - 23, 50, 15)
                        .tooltip(Tooltip.create(Component.literal("Next Page")))
                        .build());

        addRenderableWidget(
                Button.builder(Component.literal("Back"), this::handleBackButton)
                        .bounds(this.leftPos + (this.imageWidth/2) - 65, this.topPos + this.imageHeight - 23, 50, 15)
                        .tooltip(Tooltip.create(Component.literal("Previous Page")))
                        .build());

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        renderBackground(graphics);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.font, PAGE_TITLE,
                this.leftPos + this.imageWidth/2 - (PAGE_TITLE.getString().length()*5)/2,
                this.topPos + 7, 0xFFFFFF,
                true);
        graphics.drawString(this.font, String.valueOf(TooManyKeybinds.page+1),
                this.leftPos + this.imageWidth/2 - (String.valueOf(TooManyKeybinds.page+1).length()*6)/2,
                this.topPos + this.imageHeight - 20, 0xFFFFFF,
                true);
    }

    private void handleNextButton(Button button){
        if(TooManyKeybinds.page < SPLITCAT.length - 1){
            TooManyKeybinds.page++;
            this.clearWidgets();
            this.init();
        }else if(TooManyKeybinds.page == SPLITCAT.length-1){
            TooManyKeybinds.page = 0;
            this.clearWidgets();
            this.init();
        }
    }

    private void handleBackButton(Button button){
        if(TooManyKeybinds.page > 0){
            TooManyKeybinds.page--;
            this.clearWidgets();
            this.init();
        }else if(TooManyKeybinds.page == 0){
            TooManyKeybinds.page = SPLITCAT.length-1;
            this.clearWidgets();
            this.init();
        }
    }

    @Override
    public boolean isPauseScreen(){
        return false;
    }

}
