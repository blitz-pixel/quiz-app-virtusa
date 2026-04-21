package com.example.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Users;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Path filePath;

    public UserRepository(String fullPath) {
        this.filePath = Paths.get(fullPath);
    
    }


    public List<Users> loadUsers() {
        try {
            Path parent = filePath.getParent();
            if (parent != null && Files.notExists(parent)) {
                throw new RuntimeException("Users folder not found: " + parent);
            } else if (Files.notExists(filePath)) {
                throw new RuntimeException("No users data file detected at: " + filePath + ". Please create users.json.");
            } else if (Files.size(filePath) == 0) {
                return new ArrayList<>();
            }
            return OBJECT_MAPPER.readValue(filePath.toFile(), new TypeReference<List<Users>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Unable to read users data. Please check that the JSON format is valid.", e);
        }
    }

    public void addUser(Users user){
        List<Users> users = loadUsers();
        users.add(user);

        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), users);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save user data. Please check file permissions and path.", e);
        }
    }
}
