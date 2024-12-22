package io.github.dug22.webfisher.controller;

import io.github.dug22.webfisher.model.PastChat;
import io.github.dug22.webfisher.model.ParseRequest;
import io.github.dug22.webfisher.service.WebScraperService;
import io.github.dug22.webfisher.service.PastChatsService;
import io.github.dug22.webfisher.utils.logger.WebFisherLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api")
public class WebScraperController {

    @Autowired
    private WebScraperService webScraperService;

    @Autowired
    private PastChatsService pastChatsService;

    private final AtomicReference<String> lastWebsiteReference = new AtomicReference<>();

    /**
     * Endpoint to scrape the  content of a given website URL.
     * @param request a map containing the URL to be scraped.
     * @return a ResponseEntity containing the scraped content or an error message.
     */
    @PostMapping("/scrape")
    public ResponseEntity<String> scrapeWebsite(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        String domContent = webScraperService.scrapeWebsite(url);
        lastWebsiteReference.set(url);
        return ResponseEntity.ok(domContent);
    }

    /**
     * Endpoint to parse the DOM content of a website and save the result as a past chat.
     * @param request the request containing the DOM content and description for parsing.
     * @return a ResponseEntity containing the parsed content or an error message.
     */
    @PostMapping("/parse")
    public ResponseEntity<String> parseContent(@RequestBody ParseRequest request) {
        String parsedContent = webScraperService.parseContent(request.getDomContent(), request.getDescription(), request.getMaxChunks());
        if(parsedContent.equalsIgnoreCase("Sorry I couldn't process a result! Please scrape a given website's DOM content!") || parsedContent.isEmpty()){
            return ResponseEntity.status(500).body("Failed to save past chat! No DOM Content was available to parse!");
        }

        PastChat pastChat = new PastChat(
                lastWebsiteReference.get(),
                request.getDescription(),
                parsedContent
        );

        try {
            pastChatsService.savePastChat(pastChat);
            lastWebsiteReference.set(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save past chat: " + e.getMessage());
        }

        return ResponseEntity.ok(parsedContent);
    }

    /**
     * Endpoint to retrieve and load saved chats.
     * @return ResponseEntity containing a list of past chats or an error message.
     */
    @GetMapping("/past-chats")
    public ResponseEntity<List<PastChat>> getPastChats() {
        try {
            List<PastChat> pastChats = pastChatsService.loadPastChats();
            return ResponseEntity.ok(pastChats);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}