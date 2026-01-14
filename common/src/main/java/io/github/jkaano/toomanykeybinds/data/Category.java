package io.github.jkaano.toomanykeybinds.data;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category {

    private String name;
    private List<String> keyNames;
    private List<String> hiddenKeyNames;
    private boolean isHidden;

    public Category(String name, List<String> keyNames) {
        this(name, keyNames, new ArrayList<>(), false);
    }

    public Category(String name, List<String> keyNames, List<String> hiddenKeyNames) {
        this(name, keyNames, hiddenKeyNames, false);
    }

    public Category(String name, List<String> keyNames, List<String> hiddenKeyNames, boolean isHidden) {
        this.name = name;
        this.keyNames = keyNames;
        this.hiddenKeyNames = hiddenKeyNames;
        this.isHidden = isHidden;
    }

    public boolean containsKeyName(String keyName) {
        return keyNames.contains(keyName);
    }

    public void addKey(String keyName) {
        this.keyNames.add(keyName);
    }

    public void addKeyIfAbsent(String keyName) {
        if (!this.keyNames.contains(keyName)) {
            this.keyNames.add(keyName);
        }
    }

    /**
     * Partitions the name into a list of up to 10 keys per partition
     * Based on the visible/display keys
     * @return partitions list of key names list
     */
    public List<List<String>> partition() {
        final int MAX_KEYS_PER_CATEGORY = 10;
        List<String> displayNames = this.getDisplayKeyNames();

        if (displayNames == null || displayNames.isEmpty()) {
            return Collections.emptyList();
        }

        return Lists.partition(displayNames, MAX_KEYS_PER_CATEGORY);
    }

    /**
     * Gets keys from category that aren't hidden
     * @return list of key names
     */
    public List<String> getDisplayKeyNames() {
        List<String> displayKeyNames = new ArrayList<>(this.keyNames);
        displayKeyNames.removeAll(this.hiddenKeyNames);
        return displayKeyNames;
    }

    // Getters
    public String getName() { return this.name; }
    public List<String> getKeyNames() { return this.keyNames; }
    public List<String> getHiddenKeyNames() { return this.hiddenKeyNames; }
    public boolean isHidden() { return this.isHidden; }



    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setVisibleKeyNames(List<String> visibleKeyNames) {
        this.keyNames = visibleKeyNames;
    }
    public void setHiddenKeyNames(List<String> hiddenKeyNames) {
        this.hiddenKeyNames = hiddenKeyNames;
    }
    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    // Overrides
    @Override
    public String toString() {
        return "Category: " + name
                + "\nVisible Keys: "
                + keyNames.toString() + "\nHidden Keys: "
                + hiddenKeyNames.toString() + "\nisHidden: " + isHidden
                + "\n";
    }

}
