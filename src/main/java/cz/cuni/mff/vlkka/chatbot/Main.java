package cz.cuni.mff.vlkka.chatbot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

public class Main {
    public static final String BASE_URL = "http://server:9000";

    public static boolean initialize_llm(HttpClient client) {
        // JSON string for the request body
        String json = "{\"model\": \"Phi2\", \"max_total_tokens\": 300, \"quantization\": \"q8\"}";

        // Building the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/initialize/"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json))
                .build();

        // Sending the request and receiving the response
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void send_query(HttpClient client, String query) {
        // JSON string for the second request
        String json = "{\"prompt\": \"" + query + "\"}";

        // Building the second request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/generate/"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json))
                .build();

        // Sending the request and handling the streaming response
        client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                .thenApply(HttpResponse::body)
                .thenAccept(lines -> lines.forEach(line -> {
                    // Process each line of the response as it arrives
                    System.out.println(line);
                }))
                .join(); // Wait for all lines to be processed
    }

    public static void main(String[] args) throws Exception {
        sleep(5000);
        System.out.println("Hello world!");

        HttpClient client = HttpClient.newHttpClient();
        if (initialize_llm(client)) {
            System.out.println("LLM initialized successfully");
        } else {
            throw new Exception("LLM initialization failed");
        }

        send_query(client, "Human: How are you? AI:");
    }
}
