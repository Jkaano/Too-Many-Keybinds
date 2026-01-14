package io.github.jkaano.toomanykeybinds.gui.screen;

import io.github.jkaano.toomanykeybinds.data.KeyMappingManager;
import io.github.jkaano.toomanykeybinds.util.KeySelectController;
import io.github.jkaano.toomanykeybinds.util.KeyboardSimulator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

// TODO: Remove all literals with translatable

/**
 * Main window for the screen.
 */
public class KeySelectScreen extends Screen {

    // Menu
    /**
     * Current index tracks the current category being selected.
     * Partitions is how many pages of that category are needed to display all keys (10 per page)
     * Current partition is the partition being highlighted.
     */
    private static int currentIndex = 2;
    private static int partitions = 0;
    private static int currentPartition = 0;

    // Button Information
    private final int BUTTON_WIDTH = 98;
    private final int BUTTON_HEIGHT = 20;
    private final int BUTTON_GAP = 2;

    // Where to place buttons in radial menu (X, Y)
    private final int[][] RADIAL_POSITION_OFFSET = {
            {-(BUTTON_GAP + BUTTON_WIDTH),                  -(BUTTON_GAP*2 + BUTTON_HEIGHT/2 + BUTTON_HEIGHT*2)},
            {-(BUTTON_GAP + BUTTON_WIDTH + BUTTON_WIDTH/4), -(BUTTON_GAP + BUTTON_HEIGHT + BUTTON_HEIGHT/2)},
            {-(BUTTON_GAP + BUTTON_WIDTH + BUTTON_WIDTH/2),  -BUTTON_HEIGHT/2},
            {-(BUTTON_GAP + BUTTON_WIDTH + BUTTON_WIDTH/4),   BUTTON_GAP + BUTTON_HEIGHT/2},
            {-(BUTTON_GAP + BUTTON_WIDTH),                    BUTTON_GAP*2 + BUTTON_HEIGHT*2 - (BUTTON_HEIGHT/2)},
            {  BUTTON_GAP,                                  -(BUTTON_GAP*2 + BUTTON_HEIGHT/2 + BUTTON_HEIGHT*2)},
            {  BUTTON_GAP + BUTTON_WIDTH/4,                 -(BUTTON_GAP + BUTTON_HEIGHT + BUTTON_HEIGHT/2)},
            {  BUTTON_GAP + BUTTON_WIDTH/2,                 -(BUTTON_HEIGHT/2)},
            {  BUTTON_GAP + BUTTON_WIDTH/4,                   BUTTON_GAP + BUTTON_HEIGHT/2},
            {  BUTTON_GAP,                                    BUTTON_GAP*2 + BUTTON_HEIGHT*2 - (BUTTON_HEIGHT/2)}
    };

    // Screen Information
    private int CENTER_WIDTH;
    private int CENTER_HEIGHT;

    // Controller
    KeySelectController controller;

    // Widgets
    protected EditBox searchbar;
    private Button[] buttons;
    private Button prev, next;

    private StringWidget currentIndexString;
    private StringWidget partitionsString;
    private StringWidget currentPartitionString;

    public KeySelectScreen() {
        super(Component.literal("Key Select Screen"));
    }

    @Override
    protected void init() {
        super.init();
        this.controller = new KeySelectController();

        this.CENTER_WIDTH = this.width / 2;
        this.CENTER_HEIGHT = this.height / 2;

        // TODO: Code to manage what radial menu appears
        // TODO: Ideas:
        // TODO:  - Set category to next in list
        // TODO:  - Get display binds
        // TODO:  - If >10, partition and set partition count
        // TODO:  - If partition == partition count, next category
        // TODO:  - Repeat

        /*
         * Partitions category and uses it based on current index
         */
        refreshButtons();

        /*
         * Debug text
         */
        currentIndexString = new StringWidget(10, 10, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.literal("Current Category: " + controller.getCurrentIndex()), this.font);
        partitionsString = new StringWidget(10, 30, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.literal("Max partitions: " + controller.getPartitions()), this.font);
        currentPartitionString = new StringWidget(10, 50, BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.literal("Current Partition: " + controller.getCurrentPartition()), this.font);

        this.addRenderableWidget(currentIndexString);
        this.addRenderableWidget(partitionsString);
        this.addRenderableWidget(currentPartitionString);

        /*
         * Navigation widgets
         */
        prev = new Button.Builder(
                Component.literal("<"),
                (event) -> {
                    // Previous page
                    controller.back();
                    refreshButtons();
                })
                .bounds(CENTER_WIDTH - BUTTON_WIDTH/4,
                        CENTER_HEIGHT + BUTTON_HEIGHT/2 + BUTTON_GAP,
                        BUTTON_WIDTH/4 - BUTTON_GAP,
                        BUTTON_HEIGHT)
                .build();

        next = new Button.Builder(
                Component.literal(">"),
                (event) -> {
                    // Next page
                    controller.next();
                    refreshButtons();
                })
                .bounds(CENTER_WIDTH + BUTTON_GAP,
                        CENTER_HEIGHT + BUTTON_HEIGHT/2 + BUTTON_GAP,
                        BUTTON_WIDTH/4 - BUTTON_GAP,
                        BUTTON_HEIGHT)
                .build();

        this.addRenderableWidget(prev);
        this.addRenderableWidget(next);

        searchbar = new EditBox(this.font,
                CENTER_WIDTH - BUTTON_WIDTH/2 + BUTTON_GAP/2,
                CENTER_HEIGHT - BUTTON_HEIGHT/2 + BUTTON_GAP/2,
                BUTTON_WIDTH - BUTTON_GAP,
                BUTTON_HEIGHT - BUTTON_GAP, Component.literal("Search"));

        this.addRenderableWidget(searchbar);

    }

    private void refreshButtons() {
        if (this.buttons != null) {
            for (Button button : this.buttons) {
                this.removeWidget(button);
            }
        }

        if (controller.getCurrentPartitionList() != null) {
            this.buttons = generateRadialMenu(
                    controller.getCurrentPartitionList().get(controller.getPartitionIndex()));
        } else {
            this.buttons = generateRadialMenu(null);
        }

        for (Button button : buttons) {
            this.addRenderableWidget(button);
        }

    }

    // TODO: Call click function
    KeyboardSimulator simulator = new KeyboardSimulator();
    private void pressKey(String keyName) {
        int keycode = KeyMappingManager.getKeyRegistry().get(keyName).getDefaultKey().getValue();
        simulator.pressKey(keycode);
    }

    // TODO: Generate Radial Menu
    // TODO: Make functional on press
    private Button[] generateRadialMenu(List<String> partitionedCategory) {
        if (partitionedCategory == null) return new Button[0];

        int buttonCount = partitionedCategory.size();
        Button[] buttons = new Button[buttonCount];

        for (int i = 0; i < buttonCount; i++) {
            int current = i;
            buttons[i] = Button.builder(
                    Component.translatable(partitionedCategory.get(current)),
                    (event) -> {
                        this.onClose();
                        Minecraft.getInstance().player.sendSystemMessage(
                                Component.literal(partitionedCategory.get(current)));
                        pressKey(partitionedCategory.get(current));
                    })
                    .bounds(CENTER_WIDTH + RADIAL_POSITION_OFFSET[i][0],
                            CENTER_HEIGHT + RADIAL_POSITION_OFFSET[i][1],
                            BUTTON_WIDTH,
                            BUTTON_HEIGHT)
                    .build();
        }

        return buttons;
    }

    // TODO: Close window when pressing {key} again
    // Forge handled in KeySelectScreenForge

    // Menu won't pause game when open
    @Override
    public boolean isPauseScreen(){
        return false;
    }

}
