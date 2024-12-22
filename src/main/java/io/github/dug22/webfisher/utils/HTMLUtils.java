package io.github.dug22.webfisher.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.LinkedList;
import java.util.List;

public class HTMLUtils {

    /**
     * Extracts the body content of a given website.
     * *
     * @param htmlContent the HTML content of a website.
     * @return the body content of a given website, including HTML tags.
     */
    public static String extractBodyContent(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Element bodyContent = doc.body();
        return bodyContent.toString();
    }

    /**
     * Cleans up and removes white noise data from the extracted body content of a given website.
     * This method removes unwanted elements (e.g., <script> and <style>), then cleans up extra whitespace.
     *
     * @param bodyContent the raw body content of a given website.
     * @return a cleaned version of the body content, with unwanted data removed and extra whitespace trimmed.
     */
    public static String cleanBodyContent(String bodyContent) {
        Document doc = Jsoup.parse(bodyContent);
        doc.select("script, style").remove();
        String cleanedContent = doc.text();
        cleanedContent = cleanedContent.replaceAll("\\s+", " ").trim();
        return cleanedContent;
    }

    /**
     * Splits the DOM content into batches of chunks.
     * @param content the content being batched.
     * @param maxLength the maximum length of each chunk.
     * @return a list of string chunks representing the split DOM content.
     */
    public static List<String> splitDomContent(String content, int maxLength) {
        List<String> chunks = new LinkedList<>();
        for (int i = 0; i < content.length(); i += maxLength) {
            chunks.add(content.substring(i, Math.min(i + maxLength, content.length())));
        }
        return chunks;
    }
}
