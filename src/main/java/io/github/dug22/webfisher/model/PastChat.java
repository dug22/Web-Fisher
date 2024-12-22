package io.github.dug22.webfisher.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PastChat {

    private String uuid;
    private String time;
    private String site;
    private String request;
    private String response;

    /**
     * Constructs a new PastChat instance with the provide website scraped, the request made to the model, and the model's response
     * @param site the website that was scraped.
     * @param request the request made to the model.
     * @param response the model's response.
     */
    public PastChat(String site, String request, String response) {
        this.uuid = UUID.randomUUID().toString();
        this.time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
        this.site = site;
        this.request = request;
        this.response = response;
    }

    /**
     * Gets the unique identifier of the past chat.
     * @return the UUID of the past chat.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the unique identifier for the past chat.
     * @param uuid the UUID to set.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets the timestamp of when the past chat occurred
     * @return the timestamp of the past chat.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time stamp for the past chat.
     * @param time the timestamp to set.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets the website that was scraped.
     * @return the website scraped.
     */
    public String getSite() {
        return site;
    }

    /**
     * Sets the website that was scraped.
     * @param site the website to set.
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * Gets the user request made to the large language model.
     * @return the user request made.
     */
    public String getRequest() {
        return request;
    }

    /**
     * Sets the request made to the large language model.
     * @param request the request to set.
     */
    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * Gets the large language model's response.
     * @return the given response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the response from the large language model.
     * @param response the response to set.
     */
    public void setResponse(String response) {
        this.response = response;
    }
}
