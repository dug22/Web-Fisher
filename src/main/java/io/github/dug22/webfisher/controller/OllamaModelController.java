package io.github.dug22.webfisher.controller;

import io.github.dug22.webfisher.components.ModelStorageComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OllamaModelController {


    private String currentModel;

    private final ModelStorageComponent modelStorageComponent;

    //Constructor that initializes the modelStorageComponent and loads the current model.
    @Autowired
    public OllamaModelController(ModelStorageComponent ollamaModelFileComponent) {
        this.modelStorageComponent = ollamaModelFileComponent;
        this.currentModel = modelStorageComponent.getCurrentModel();
    }


    /**
     * Endpoint to update the current large language model
     * @param request a map containing the new model as a String.
     * @return a ResponseEntity indicating success or failure of the update.
     */
    @PostMapping("/update-model")
    public ResponseEntity<String> updateModel(@RequestBody Map<String, String> request) {
        String newModel = request.get("model");
        if (newModel == null || newModel.isEmpty()) {
            return ResponseEntity.badRequest().body("Model name is required.");
        }

        currentModel = newModel;
        try {
            modelStorageComponent.saveModelToFile(newModel);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save model: " + e.getMessage());
        }

        return ResponseEntity.ok("Model updated to: " + newModel);
    }

    /**
     * Endpoint to retrieve the current large language model
     * @return a ResponseEntity containing the current model or an error if no model was found.
     */
    @GetMapping("/current-model")
    public ResponseEntity<String> getCurrentModel() {
        if (currentModel == null || currentModel.isEmpty()) {
            return ResponseEntity.status(404).body("No model found.");
        }
        return ResponseEntity.ok(currentModel);
    }
}
