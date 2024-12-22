package io.github.dug22.webfisher.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.dug22.webfisher.model.PastChat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class PastChatsService {

    //Specifies the location of the past_chats.json file.
    @Value("${past_chats.storage.location}")
    private String pastChatsFileLocation;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Saves a new PastChat to the storage file.
     * The chat is appended to the existing list of past chats, and the list is saved back to the file.
     *
     * @param pastChat the PastChat object to be saved.
     * @throws IOException if an error occurs while writing to the storage file.
     */
    public void savePastChat(PastChat pastChat) throws IOException {
        List<PastChat> pastChats = loadPastChats();
        pastChats.add(pastChat);
        try (Writer writer = new FileWriter(pastChatsFileLocation)) {
            gson.toJson(pastChats, writer);
        }
    }

    /**
     * Loads all past chats from the storage file.
     * If the file does not exist or is empty, an empty list is returned.
     *
     * @return a list of PastChat objects loaded from the file.
     * @throws IOException if an error occurs while reading from the storage file.
     */
    public List<PastChat> loadPastChats() throws IOException {
        File file = new File(pastChatsFileLocation);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type listType = new TypeToken<List<PastChat>>() {
                }.getType();
                List<PastChat> pastChats = gson.fromJson(reader, listType);
                return pastChats != null ? pastChats : new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }
}