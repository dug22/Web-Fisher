# About
Web Fisher is a simple and comprehensive AI-powered web scraping, web application that uses Ollama, Playwright, Spring AI, and other libraries. With the scraped web content you pulled and provided, you can leverage large language models to provide insightful answers/responses to your questions/descriptions.

# Getting Started

### Prerequisites
* Ensure you have Git installed.
* Ensure you have Java 21 or later installed.
* Install Ollama and install one of these three given large language models (Web Fisher supports LLama 3.1, Llama 3.2 or Mistral)

### Quickstart
1. Clone this repository using Git.
   * ```git clone https://github.com/dug22/Web-Fisher.git```
2. Ensure you have Ollama installed.
   * Proceed to [Ollama's](https://ollama.com/download) official website and proceed with the installation of the version compatible with your operating system. 
3. Ensure you have downloaded one of the following large language models: Llama 3.1, Llama 3.2, or Mistral. Open your terminal and type the following commands to install these models onto your machine.
   * Llama 3.1: ```ollama pull llama3.1```
   * Llama 3.2: ```ollama pull llama3.2```
   * Mistral: ```ollama pull mistral```
4. Start your application and go to localhost:8080 with a web browser.
5. The ollama_model.json file has Mistral set as its default model upon startup. You can change this through your code editor or through Web-Fisher's frontend. This file can be accessed at the following path: "src/main/resources/db/ollama_model.json".
   * If you select the large language model through Web-Fisher's frontend, ensure that you click the "Save Model" button to update the model you want to use. 
6. Copy and paste the URL of the website you want to scrape. Web Fisher will then extract the website's body / document object model (DOM) content.
   * Web Fisher will not scrape style tags or script tags.
7. Specify the information you would like the following large language model to process and retrieve from the extracted body / DOM content.
8. Max Chunks allows you to define the maximum length of each chunk and the number of chunks per batch when splitting the extracted body or DOM content for the large language model to process. This batch processing approach helps the large language model efficiently handle and review the segmented text.
9. A response will be generated for you. If the answer satisfies your needs, you will be given the option to download the response.
10. All prompts are saved in a file called past_chats.json, which can be found at the following path: "src/main/resources/db/past_chats.json".


# Usages
WebFisher can be used to do the following:
* Scraping and analyzing the content of a specific web page, for summaries, insights, or key information.
* Extracting job details such as title, description, company, and location from a specific job listing page to track opportunities.
* Pulling content from a specific academic article or research paper, extracting the abstract, conclusions, or references for further study or citation.
* Scraping individual news articles to extract headlines, publication date, and content, useful for tracking current events or specific topics.
* Extracting product details (like pricing, description, reviews) from a single e-commerce product page to gather data for comparison or analysis.
* And a lot more.

# Media
![Image1](https://github.com/dug22/Web-Fisher/blob/master/images/web%20fisher%20visual%2001.png?raw=true)
![Image2](https://github.com/dug22/Web-Fisher/blob/master/images/web%20fisher%20visual%2002.png?raw=true)


# URL Endpoints:
1. **localhost:8080** access the main web application.
2. **localhost:8080/api/scrape/{url}**
3. **localhost:8080/api/parse**
4. **localhost:8080/api/past-chats** accesses past chats.
5. **localhost:8080/api/update-model** updates the model.
6. **localhost:8080/api/current-model** gets the current model in use.

# Contributions
If you'd like to contribute to this repository, feel free to open a pull request with your suggestions, bug fixes, or enhancements. Contributions are always welcome!
