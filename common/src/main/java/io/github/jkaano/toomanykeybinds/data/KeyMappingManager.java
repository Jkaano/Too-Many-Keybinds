package io.github.jkaano.toomanykeybinds.data;

import io.github.jkaano.toomanykeybinds.Constants;
import io.github.jkaano.toomanykeybinds.config.KeyMappingFileManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads keys from game and save file, adds new bindings and resaves.
 */
public class KeyMappingManager {
    // Registries
    private static Map<String, KeyMapping> keyRegistry;
    private static LinkedHashMap<String,Category> categoryRegistry;

    // Gui
    private static List<Category> displayCategories;

    // File Manager
    private final KeyMappingFileManager fileManager;

    public KeyMappingManager() {
        this.fileManager = new KeyMappingFileManager();
    }

    /**
     * Loads the current configuration files if found
     * Adds any new keys or categories to it, then saves.
     * Also creates a map of key names to their respective mapping from Minecraft Options
     */
    public void init() {
        // Build registries
        KeyMapping[] keyMappings = Minecraft.getInstance().options.keyMappings;
        buildKeyRegistry(keyMappings);
        buildCategoryRegistry(keyMappings, loadCategory());

        // Update displayable categories
        updateDisplayCategories(false);

        // Save objects to files
        fileManager.saveToFile(Constants.KEYMAPCONFIGDIRECTORY, categoryRegistry.values());
        fileManager.saveToFile(Constants.ALLKEYBINDS, keyRegistry.keySet());
    }

    /**
     * Loads JSON file for updating
     * @return new LinkedHashMap if no file exists or collects the list to a LinkedHashMap
     */
    public LinkedHashMap<String, Category> loadCategory() {
        List<Category> loadedCategories = fileManager.readFromFile(Constants.KEYMAPCONFIGDIRECTORY);

        if (loadedCategories == null) { return new LinkedHashMap<>(); }

        return loadedCategories.stream().collect(
                Collectors.toMap(
                        Category::getName,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    /**
     * Build keyRegistry, key=keyName, value=KeyMapping
     * @param keyMappings is the list of KeyMappings provided by Minecraft$Options
     */
    public void buildKeyRegistry(KeyMapping[] keyMappings) {
        keyRegistry = new HashMap<>();
        for (KeyMapping keyMapping : keyMappings) {
            keyRegistry.putIfAbsent(keyMapping.getName(), keyMapping);
        }
    }

    /**
     * Builds categoryRegistry, key=categoryName, value=Category
     * @param keyMappings is the list of KeyMappings provided by Minecraft$Options
     * @param loadedCategories is the LinkedHashMap of Categories saved to file by TooManyKeybinds
     */
    public void buildCategoryRegistry(KeyMapping[] keyMappings, LinkedHashMap<String, Category> loadedCategories) {
        categoryRegistry = new LinkedHashMap<>(loadedCategories);
        for (KeyMapping keyMapping : keyMappings) {
            String categoryName = keyMapping.getCategory();
            categoryRegistry.computeIfAbsent(
                    categoryName,
                    c -> new Category(c, new ArrayList<>())
                    )
                    .addKeyIfAbsent(keyMapping.getName());
        }
    }

    /**
     * Update which categories will be displayed in the menu
     * @param showHidden toggles whether hidden categories are shown
     */
    public void updateDisplayCategories(boolean showHidden) {
        if (showHidden) {
            displayCategories = new ArrayList<>(categoryRegistry.values());
        } else {
            displayCategories = categoryRegistry.values().stream()
                    .filter(c -> !c.isHidden())
                    .toList();
        }
    }

    // Getters
    public static Map<String, KeyMapping> getKeyRegistry() { return keyRegistry; }
    public static LinkedHashMap<String, Category> getCategoryRegistry() { return categoryRegistry; }
    public static List<Category> getDisplayCategories() { return displayCategories; }

}
