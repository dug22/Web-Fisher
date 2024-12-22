let pastChats = [];

//Loads past chats from src/main/resources/db/past_chats.json
async function loadPastChats() {
    try {
        console.log("Loading past chats from the backend...");

        const response = await fetch('/api/past-chats');

        if (!response.ok) {
            throw new Error('Failed to load past chats from the backend.');
        }

        pastChats = await response.json();
        console.log("Loaded past chats: ", pastChats);
        updateSidebar();
    } catch (error) {
        console.error("Error loading past chats:", error);
        alert("An error occurred while loading past chats.");
    }
}

//Saves a new past chat object once a request to the AI model is declared.
function savePastChat(request) {
    const currentTime = new Date().toLocaleString();
    pastChats.push({ request, time: currentTime });
    updateSidebar();
    localStorage.setItem('pastChats', JSON.stringify(pastChats));
    console.log("Saved past chats to localStorage: ", pastChats);
}

//Updates the sidebar of past chats.
function updateSidebar() {
    const pastChatsList = document.getElementById('pastChatsList');
    pastChatsList.innerText = '';

    if (pastChats.length === 0) {
        console.log("No past chats to display.");
    }
    pastChats.forEach((chat, index) => {
        const listItem = document.createElement('li');
        listItem.textContent = `${chat.time}: ${chat.request || "No request available"}`;
        listItem.onclick = () => showChatDetails(index);
        pastChatsList.appendChild(listItem);
    });
}

//If click shows details about the past chat.
function showChatDetails(index) {
    const chat = pastChats[index];
    alert(`Full content for:\n\nTime: ${chat.time}\nRequest: ${chat.request}`);
}


//Parses the given content.
async function parseContent() {
    const urlInput = document.getElementById("urlInput");
    const domContent = document.getElementById("domContent").value;
    const parseDescription = document.getElementById("parseDescription").value;
    const maxChunks = parseInt(document.getElementById("maxChunks").value, 10); // Use 10 as radix for decimal

    console.log("Dropdown element:", document.getElementById("maxChunks"));
    console.log("Selected value:", document.getElementById("maxChunks").value);
    console.log("Parsed maxChunks value:", maxChunks);  // Log the parsed value

    const processCatchButton = document.querySelector(".process-catch-button");
    const castLineButton = document.querySelector("button[onclick='scrapeWebsite()']");

    processCatchButton.disabled = true;
    urlInput.disabled = true;
    castLineButton.disabled = true;

    try {
        const response = await fetch("/api/parse", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ domContent, description: parseDescription, maxChunks }),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.text();
        document.getElementById("parsedResult").innerText = result;
        savePastChat(parseDescription);
    } catch (error) {
        console.error("Error during content parsing:", error);
        document.getElementById("parsedResult").innerText = "An error occurred while processing the content. Please try again.";
    } finally {
        processCatchButton.disabled = false;
        urlInput.disabled = false;
        castLineButton.disabled = false;
    }
}


//Updates the selected model.
function updateModel() {
    const selectedModel = document.getElementById("modelSelector").value;
    fetch('/api/update-model', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ model: selectedModel }),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to update model. Please try again.");
        }
        return response.text();
    })
    .then(() => {
        return fetch('/api/current-model');
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch the current model.");
        }
        return response.text();
    })
    .then(currentModel => {
        if (currentModel === selectedModel) {
            alert(`Model successfully updated to: ${currentModel}`);
        } else {
            alert(`Model update verification failed. Current model is: ${currentModel}`);
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("An error occurred. Check the console for details.");
    });
}

//Send a request to scrape a given website.
async function scrapeWebsite() {
    const urlInput = document.getElementById("urlInput");
    const castLineButton = document.querySelector("button[onclick='scrapeWebsite()']");
    const processCatchButton = document.querySelector(".process-catch-button");

    const url = urlInput.value;

    if (!url || url.trim() === "") {
        alert("Please enter a valid URL.");
        return;
    }

    castLineButton.disabled = true;
    processCatchButton.disabled = true;
    urlInput.disabled = true;

    try {
        const response = await fetch("/api/scrape", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ url }),
        });

        if (!response.ok) {
            console.error('Error scraping website:', response.statusText);
            alert('Failed to scrape website. Status code: ' + response.status);
            return;
        }

        const domContent = await response.text();
        document.getElementById("domContent").value = domContent;
    } catch (error) {
        console.error("Error during scraping:", error);
        alert("An error occurred while scraping the website.");
    } finally {
        castLineButton.disabled = false;
        urlInput.disabled = false;
        processCatchButton.disabled = false;
    }
}

//On load or refreshes, in realtime displays the list of past chats, and gets the current large language model in use.
window.onload = async () => {
    loadPastChats();
    console.log("Page loaded. Past chats should be displayed.");

    try {
        const response = await fetch('/api/current-model');
        if (!response.ok) {
            throw new Error("Failed to load current model");
        }
        const currentModel = await response.text();
        const modelSelector = document.getElementById("modelSelector");
        modelSelector.value = currentModel;
    } catch (error) {
        console.error("Error loading model:", error);
    }
};
