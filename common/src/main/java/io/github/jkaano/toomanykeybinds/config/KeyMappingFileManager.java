package io.github.jkaano.toomanykeybinds.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.jkaano.toomanykeybinds.Constants;
import io.github.jkaano.toomanykeybinds.data.Category;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Manages the saving and reading of the KeyMapping save files
 */
public class KeyMappingFileManager {

    // Gson
    private final Gson GSON;
    private final Gson gsonReader;

    public KeyMappingFileManager() {
        this.GSON = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        gsonReader = new Gson();

        Path path = Paths.get(Constants.DIRECTORY);
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
                Constants.LOG.info("File created at {}", path.toAbsolutePath());
            } else {
                Constants.LOG.info("File already exists at {}", path.toAbsolutePath());
            }
        } catch (IOException e) {
            Constants.LOG.error("Error creating directory at {} using {}", path.toAbsolutePath(), this.getClass());
            Constants.LOG.error("An error occurred during an I/O exception", e);
        }
    }

    // Saves key information
    public <T> void saveToFile(String path, T object) {
        try (FileWriter writer = new FileWriter(path)) {
            GSON.toJson(object, writer);
        } catch (IOException e) {
            Constants.LOG.error("An error occurred saving {}, IO Exception {}", path, e);
        }
    }

    // Reads from config file
    public List<Category> readFromFile(String path) {
        Type listType = new TypeToken<List<Category>>(){}.getType();
        try (FileReader reader = new FileReader(path)) {
            return gsonReader.fromJson(reader, listType);
        } catch (IOException e) {
            Constants.LOG.error("An error occurred reading {}, IO Exception {}", path, e);
            return null;
        }
    }

}
