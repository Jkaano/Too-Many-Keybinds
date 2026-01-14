package io.github.jkaano.toomanykeybinds.util;

import io.github.jkaano.toomanykeybinds.Constants;
import io.github.jkaano.toomanykeybinds.data.Category;
import io.github.jkaano.toomanykeybinds.data.KeyMappingManager;

import java.util.List;

public class KeySelectController {

    // Index
    private static int partitionIndex = 0;
    private static int categoryIndex = 0;

    // Category
    private List<Category> displayCategories;
    private List<List<String>> currentPartitionList;
    private Category currentCategory;

    /**
     * Controls the display of the KeySelectScreen
     */
    public KeySelectController() {
        this.displayCategories = KeyMappingManager.getDisplayCategories();

        updateState();
    }

    /**
     * Updates the state of the menu
     * First updates the category to ensure it can still grab from the display
     * Second updates the partition to divide the category into workable pieces
     */
    private void updateState() {
        setCurrentCategory();
        setCurrentPartition();
    }

    /**
     * Refreshes the display categories in the menu
     */
    private void refresh() {
        this.displayCategories = KeyMappingManager.getDisplayCategories();
        updateState();
    }

    /**
     * Sets the current category for based on the categoryIndex
     */
    public void setCurrentCategory() {
        if (displayCategories == null || displayCategories.isEmpty()) {
            currentCategory = null;
            categoryIndex = 0;
            return;
        }

        if (categoryIndex >= 0 && categoryIndex < displayCategories.size()) {
            currentCategory = displayCategories.get(categoryIndex);
        } else {
            Constants.LOG.error("Invalid categoryIndex: {}. List is size: {}",
                                categoryIndex, displayCategories.size());
            categoryIndex = 0;
            currentCategory = displayCategories.get(0);
        }
    }

    /**
     * Partitions the current category
     */
    public void setCurrentPartition() {
        if (currentCategory == null) {
            currentPartitionList = null;
            partitionIndex = 0;
            return;
        }

        currentPartitionList = currentCategory.partition();

        if (currentPartitionList == null || currentPartitionList.isEmpty()) {
            currentPartitionList = null;
            partitionIndex = 0;
        } else if (partitionIndex >= currentPartitionList.size()){
            partitionIndex = Math.max(0, currentPartitionList.size() - 1);
        }

    }

    /**
     * Move to the next category
     */
    public void next() {
        if (displayCategories == null || displayCategories.isEmpty()) return;

        int currentSize = (currentPartitionList == null) ? 0 : currentPartitionList.size();

        if (partitionIndex < currentSize - 1) {
            partitionIndex++;
        } else {
            categoryIndex = (categoryIndex + 1) % displayCategories.size();
            partitionIndex = 0;
            updateState();
        }
    }

    /**
     * Move to the previous category
     */
    public void back() {
        if (displayCategories == null || displayCategories.isEmpty()) return;

        if (partitionIndex > 0) {
            partitionIndex--;
        } else {
            categoryIndex = (categoryIndex - 1 + displayCategories.size()) % displayCategories.size();
            updateState();
            partitionIndex = (currentPartitionList == null) ? 0 : currentPartitionList.size() - 1;
        }
    }

    // Getters
    public List<List<String>> getCurrentPartitionList() { return currentPartitionList; }
    public int getPartitionIndex() { return partitionIndex; }

    public int getCurrentIndex() { return categoryIndex; }
    public int getPartitions() {
        return (currentPartitionList == null) ? -1 : currentPartitionList.size() - 1;
    }
    public int getCurrentPartition() { return partitionIndex; }

}
