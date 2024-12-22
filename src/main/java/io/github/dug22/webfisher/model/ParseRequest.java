package io.github.dug22.webfisher.model;


public class ParseRequest {

    private String domContent;
    private String description;
    private int maxChunks;

    /**
     * Gets the DOM content for parsing.
     *
     * @return the DOM content as a string.
     */
    public String getDomContent() {
        return domContent;
    }

    /**
     * Sets the DOM content for parsing.
     *
     * @param domContent the DOM content to be set.
     */
    public void setDomContent(String domContent) {
        this.domContent = domContent;
    }

    /**
     * Gets the description associated with the DOM content.
     *
     * @return the description as a string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description associated with the DOM content.
     *
     * @param description the description to be set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxChunks() {
        return maxChunks;
    }

    public void setMaxChunks(int maxChunks) {
        this.maxChunks = maxChunks;
    }

    /**
     * Returns a string representation of the ParseRequest object.
     *
     * @return a string representing the DOM content and description.
     */
    @Override
    public String toString() {
        return "ParseRequest{" +
                "domContent='" + domContent + '\'' +
                ", description='" + description + '\'' +
                ", maxChunks='" + maxChunks + '\'' +
                '}';
    }
}
