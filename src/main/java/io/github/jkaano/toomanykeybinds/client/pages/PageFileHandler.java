package io.github.jkaano.toomanykeybinds.client.pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class PageFileHandler{

    private final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private final Gson gsonReader = new Gson();

    //File paths
    private final String folderPath = "./config/toomanykeybinds";
    public final String defaultPagesPath = folderPath + "/default-pages.json";

    public PageFileHandler(){
        createFile();
    }

    private void createFile(){
        File file = new File(folderPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public void savePageGroup(PageGroup pageGroup, String path) throws IOException{
        FileWriter writer = new FileWriter(path);
        GSON.toJson(pageGroup, writer);
        writer.close();
    }

    public PageGroup readPageGroup(String path) throws IllegalStateException, IOException{
        FileReader fileReader = new FileReader(path);
        PageGroup pageGroup =  gsonReader.fromJson(fileReader, PageGroup.class);
        fileReader.close();
        return pageGroup;
    }

}
