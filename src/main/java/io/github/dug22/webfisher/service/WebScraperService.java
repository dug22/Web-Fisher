package io.github.dug22.webfisher.service;

import io.github.dug22.webfisher.utils.HTMLUtils;
import io.github.dug22.webfisher.components.ModelStorageComponent;
import io.github.dug22.webfisher.utils.logger.WebFisherLogger;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.microsoft.playwright.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WebScraperService {

    // A formatted prompt template that guides the AI model in extracting information.
    private static final String PROMPT_TEXT = String.format(
            "You are tasked with extracting specific information from the following text content: %s.\n\n" +
                    "Please follow these instructions carefully:\n" +
                    "1. **Extract Information:** Only extract the information that directly matches the provided description: %s.\n" +
                    "2. **No Extra Content:** Do not include any additional text, comments, or explanations in your response.\n" +
                    "3. **Empty Response:** If no information matches the description, return an empty string ('').\n" +
                    "4. **Direct Data Only:** Your output should contain only the data that is explicitly requested, with no other text.\n",
            "{dom_content}", "{parse_description}"
    );

    private final ModelStorageComponent modelStorageComponent;


    // Injecting the OllamaChatModel instance for processing AI-based requests
    @Autowired
    OllamaChatModel chatModel;

    // Constructor to initialize the model storage component
    @Autowired
    public WebScraperService(ModelStorageComponent ollamaModelUtils) {
        this.modelStorageComponent = ollamaModelUtils;
    }

    /**
     * Scrapes the content of a website using Playwright.
     *
     * @param url the URL of the website to scrape
     * @return the cleaned body content of the website or a failure message if scraping fails
     */
    public String scrapeWebsite(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate(url);
            page.waitForSelector("body", new Page.WaitForSelectorOptions().setTimeout(10000));
            String content = HTMLUtils.extractBodyContent(page.content());
            if (content.isEmpty()) {
                return "COULD NOT SCRAPE";
            }
            return HTMLUtils.cleanBodyContent(content);
        } catch (Exception e) {
            return "COULD NOT SCRAPE";
        }
    }

    /**
     * Parses the DOM content using a description to extract specific data.
     * The content is split into chunks and each chunk is processed by the large language model.
     *
     * @param domContent  the raw DOM content of the scraped website
     * @param description the description of the information to extract
     * @param maxChunks represents the length of max chunks.
     * @return the parsed content or an error message if no relevant content is found
     */
    public String parseContent(String domContent, String description, int maxChunks) {
        if (domContent.isEmpty()) {
            return "Sorry I couldn't process a result! Please scrape a given website's DOM content!";
        }
        AtomicInteger batchCount = new AtomicInteger(0);
        List<String> chunks = HTMLUtils.splitDomContent(domContent, maxChunks);
        List<String> results = chunks.parallelStream()
                .map(chunk -> {
                    WebFisherLogger.logInfo("Batching Process: " + batchCount.incrementAndGet());
                    return processChunkWithAI(chunk, description);
                })
                .collect(Collectors.toList());
        return consolidateResults(results);
    }


    /**
     * Processes a single chunk of DOM content using the large language model to extract information
     * based on the provided description.
     *
     * @param chunk the chunk of DOM content to process
     * @param description the description of the information to extract
     * @return the extracted data or a default message if no match is found
     */
    private String processChunkWithAI(String chunk, String description) {
        try {
            WebFisherLogger.logInfo("Processing chunk: " + chunk.substring(0, Math.min(100, chunk.length())) + "...");
            Map<String, Object> promptParams = Map.of(
                    "dom_content", chunk,
                    "parse_description", description
            );

            PromptTemplate promptTemplate = new PromptTemplate(PROMPT_TEXT);
            Message message = promptTemplate.createMessage(promptParams);
            ChatOptions chatOptions = ChatOptionsBuilder.builder()
                    .withModel(modelStorageComponent.loadModelFromFile())
                    .build();
            Prompt prompt = new Prompt(List.of(message), chatOptions);
            String response = chatModel.call(prompt)
                    .getResult()
                    .getOutput()
                    .getContent();

            WebFisherLogger.logInfo("Large language model response: " + response);
            return response;
        } catch (Exception e) {
            return "No match found";
        }
    }

    /**
     * Consolidates the results from all processed chunks by finding the most frequent matching response.
     *
     * @param results a list of responses from the AI model
     * @return the most frequent response or a message indicating no relevant data was found
     */
    private String consolidateResults(List<String> results) {
        WebFisherLogger.logInfo("Input Results: " + results);
        Map<String, Long> frequencyMap = results.stream()
                .filter(response -> response != null && !response.trim().isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        WebFisherLogger.logInfo("Frequency Map " + frequencyMap);
        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sorry, no relevant information could be extracted.");
    }
}