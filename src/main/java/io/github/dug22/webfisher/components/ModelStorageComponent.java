package io.github.dug22.webfisher.components;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ModelStorageComponent {

    private final Gson gson = new Gson();

    //Specifies the location of the ollama_model.json file.
    @Value("${model.storage.location}")
    private String modelStorageLocation;


    /**
     * Retrieves the current large language model from the file located at src/main/resources/db/ollama_model.json.
     *
     * @return the current model as defined in the JSON file.
     */
    public String getCurrentModel(){
        return loadModelFromFile();
    }

    /**
     * Loads the current large language model from the file located at src/main/resources/db/ollama_model.json.
     *
     * @return the loaded model.
     */
    public String loadModelFromFile() {
        File file = new File(modelStorageLocation);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                return jsonObject.has("model") ? jsonObject.get("model").getAsString() : null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Saves the selected large language model into the file located at src/main/resources/db/ollama_model.json
     * @param model the model selected
     * @throws IOException if an I/O error occurs during the file writing process.
     */
    public void saveModelToFile(String model) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model", model);

        try (Writer writer = new FileWriter(modelStorageLocation)) {
            gson.toJson(jsonObject, writer);
        }
    }
}
