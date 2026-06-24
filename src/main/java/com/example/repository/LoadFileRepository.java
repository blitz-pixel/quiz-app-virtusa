package com.example.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoadFileRepository<T> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Path filePath;
    private final Class<T> itemType;

    public LoadFileRepository(String fileName, Class<T> itemType){
        this.itemType = itemType;
      
        Path fileDir = Paths.get(System.getProperty("user.home"), ".quizapp");
         this.filePath = fileDir.resolve(fileName);

        initializeFile(fileName);
    }

    private void initializeFile(String fileName){
        try {
            Files.createDirectories(filePath.getParent());
            if (Files.notExists(filePath)) {

            try (var in = getClass()
                    .getClassLoader()
                    .getResourceAsStream(fileName)) {

                if (in == null) {
                    throw new RuntimeException(
                            "Default file not found: "
                                    + fileName);
                }

                Files.copy(in, filePath);
            } 
        }

    } catch (IOException e) {
        throw new RuntimeException(
                "Failed to initialize data file",
                e);
    }
    }
   
    @SuppressWarnings("unchecked")
    public LoadFileRepository(String fileName) {
        this(fileName, (Class <T>) Object.class );
    }

    public List<T> loadAll() {
        try {
            Path parent = filePath.getParent();
            // System.out.println("Loading data from: " + filePath.toAbsolutePath());
            if (parent != null && Files.notExists(parent)) {
                throw new RuntimeException("Data folder not found: " + parent);
            } else if (Files.notExists(filePath)) {
                throw new RuntimeException("No data file detected at: " + filePath + ". Please create the file.");
            } else if (Files.size(filePath) == 0) {
                return new ArrayList<>();
            }
            
            CollectionType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, itemType);
            return OBJECT_MAPPER.readValue(filePath.toFile(), listType);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read data. Please check that the JSON format is valid.", e);
        }
    }

    public void add(T item) {
        List<T> items = loadAll();
        items.add(item);

        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), items);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save data. Please check file permissions and path.", e);
        }
    }

}


