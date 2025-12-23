package com.example.bookstore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.bookstore.dto.ChatRequest;
import com.example.bookstore.dto.ChatResponse;

@Service
public class AthenaService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public AthenaService() {
        this.restTemplate = new RestTemplate();
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        try {
            System.out.println("=== Athena Service Chat Request ===");
            System.out.println("API Key configured: " + (apiKey != null && !apiKey.isEmpty() ? "Yes" : "No"));
            System.out.println("API URL: " + apiUrl);
            System.out.println("Message: " + chatRequest.getMessage());
            
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Build messages list
            List<Map<String, String>> messages = new ArrayList<>();
            
            // Add system message for Athena's personality and role
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", 
                "You are Athena, a helpful and knowledgeable AI customer service assistant for BookHaven, " +
                "an online bookstore, that was created by the greatest software developer, Soeun Sovannarith. You help customers with book recommendations, order inquiries, " +
                "shipping information, returns and refunds, account issues, and general questions about books. " +
                "Be friendly, professional, and concise. If you don't know something specific about BookHaven's " +
                "policies, politely suggest the customer contact support directly. When recommending books, " +
                "consider the customer's preferences and provide thoughtful suggestions." + "When user ask questions that doesn't related to book or BookHaven, " 
                + " politely deny to answer and describe your role" + "here're the book detail within BookHaven," + 
                "[\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 7,\n" + //
                                        "    \"title\": \"Eat that Frog\",\n" + //
                                        "    \"author\": \"Brian Tracy\",\n" + //
                                        "    \"published_date\": \"2018-01-30\",\n" + //
                                        "    \"stock\": 50,\n" + //
                                        "    \"category\": \"Productivity\",\n" + //
                                        "    \"price\": 20,\n" + //
                                        "    \"description\": \"One of the best productivity\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 19,\n" + //
                                        "    \"title\": \"Rich Dad, Poor Dad\",\n" + //
                                        "    \"author\": \"Robert Kiyosaki\",\n" + //
                                        "    \"published_date\": \"1997-06-18\",\n" + //
                                        "    \"stock\": 20,\n" + //
                                        "    \"category\": \"Self-Help\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"One of the best self-help book\",\n" + //
                                        "    \"imageURL\": \"NAN\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 21,\n" + //
                                        "    \"title\": \"Grokking Algorithms\",\n" + //
                                        "    \"author\": \"Aditya Y Bhargava\",\n" + //
                                        "    \"published_date\": \"2020-06-09\",\n" + //
                                        "    \"stock\": 20,\n" + //
                                        "    \"category\": \"Computer Science\",\n" + //
                                        "    \"price\": 40,\n" + //
                                        "    \"description\": \"The best comprehensive algorithm book for a visual learner\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 22,\n" + //
                                        "    \"title\": \"Digital Minimalism\",\n" + //
                                        "    \"author\": \"Carl Newport\",\n" + //
                                        "    \"published_date\": \"2019-02-05\",\n" + //
                                        "    \"stock\": 90,\n" + //
                                        "    \"category\": \"Productivity\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"The go-to book for people who are tired of over-stimulation\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 13,\n" + //
                                        "    \"title\": \"Deep Work\",\n" + //
                                        "    \"author\": \"Carl Newport\",\n" + //
                                        "    \"published_date\": \"2016-06-07\",\n" + //
                                        "    \"stock\": 60,\n" + //
                                        "    \"category\": \"Productivity\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"The book that will help you to thrive in this distracted world\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 14,\n" + //
                                        "    \"title\": \"Obstacle is the way\",\n" + //
                                        "    \"author\": \"Ryan Holiday\",\n" + //
                                        "    \"published_date\": \"1997-10-14\",\n" + //
                                        "    \"stock\": 30,\n" + //
                                        "    \"category\": \"Self-Help\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"One of the best Self-Help\",\n" + //
                                        "    \"imageURL\": \"NAN\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 23,\n" + //
                                        "    \"title\": \"Never Finished\",\n" + //
                                        "    \"author\": \"David Goggins\",\n" + //
                                        "    \"published_date\": \"2025-12-02\",\n" + //
                                        "    \"stock\": 100,\n" + //
                                        "    \"category\": \"Self-Help\",\n" + //
                                        "    \"price\": 1,\n" + //
                                        "    \"description\": \"The extension of Can't hurt me\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 5,\n" + //
                                        "    \"title\": \"Atomic Habits\",\n" + //
                                        "    \"author\": \"James Clear\",\n" + //
                                        "    \"published_date\": \"2018-01-22\",\n" + //
                                        "    \"stock\": 100,\n" + //
                                        "    \"category\": \"Productivity\",\n" + //
                                        "    \"price\": 0.1,\n" + //
                                        "    \"description\": \"The best productivity book\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 6,\n" + //
                                        "    \"title\": \"The Law of Human Nature\",\n" + //
                                        "    \"author\": \"Robert Greene\",\n" + //
                                        "    \"published_date\": \"2018-01-08\",\n" + //
                                        "    \"stock\": 150,\n" + //
                                        "    \"category\": \"Psychology\",\n" + //
                                        "    \"price\": 15,\n" + //
                                        "    \"description\": \"The best book about human behavior.\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 12,\n" + //
                                        "    \"title\": \"Thinking, Fast and Slow\",\n" + //
                                        "    \"author\": \"Daniel Kahneman\",\n" + //
                                        "    \"published_date\": \"2020-01-07\",\n" + //
                                        "    \"stock\": 70,\n" + //
                                        "    \"category\": \"Psychology\",\n" + //
                                        "    \"price\": 20,\n" + //
                                        "    \"description\": \"The book that helps you understand the science behind decision making\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 9,\n" + //
                                        "    \"title\": \"The 48 Laws of Power\",\n" + //
                                        "    \"author\": \"Robert Greene\",\n" + //
                                        "    \"published_date\": \"1991-06-04\",\n" + //
                                        "    \"stock\": 100,\n" + //
                                        "    \"category\": \"Psychology\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"One of the Best Non-Fiction books ever\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 11,\n" + //
                                        "    \"title\": \"Can't Hurt me\",\n" + //
                                        "    \"author\": \"David Goggin\",\n" + //
                                        "    \"published_date\": \"2018-06-05\",\n" + //
                                        "    \"stock\": 100,\n" + //
                                        "    \"category\": \"Self-Help\",\n" + //
                                        "    \"price\": 40,\n" + //
                                        "    \"description\": \"The guide to building mental toughness\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 10,\n" + //
                                        "    \"title\": \"The Secret\",\n" + //
                                        "    \"author\": \"Rhonda Byrne\",\n" + //
                                        "    \"published_date\": \"2006-05-08\",\n" + //
                                        "    \"stock\": 50,\n" + //
                                        "    \"category\": \"Self-Help\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"The book that exposes you to the law of attraction.\",\n" + //
                                        "    \"imageURL\": \"nth\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 15,\n" + //
                                        "    \"title\": \"The Mountain is you\",\n" + //
                                        "    \"author\": \"Brianna Wiest\",\n" + //
                                        "    \"published_date\": \"2020-06-16\",\n" + //
                                        "    \"stock\": 40,\n" + //
                                        "    \"category\": \"Self-Help\",\n" + //
                                        "    \"price\": 20,\n" + //
                                        "    \"description\": \"This book is all about self-sabotage and also the step-by-step process on how to avoid it. \",\n" + //
                                        "    \"imageURL\": \"NAN\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 16,\n" + //
                                        "    \"title\": \"Slow Productivity\",\n" + //
                                        "    \"author\": \"Carl Newport\",\n" + //
                                        "    \"published_date\": \"2019-07-17\",\n" + //
                                        "    \"stock\": 40,\n" + //
                                        "    \"category\": \"Productivity\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"The book that helps you to get more work done without burning out.\",\n" + //
                                        "    \"imageURL\": \"NAN\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 17,\n" + //
                                        "    \"title\": \"The 7 Habits of Highly Effective People\",\n" + //
                                        "    \"author\": \"Stephen R. Covey\",\n" + //
                                        "    \"published_date\": \"1936-06-09\",\n" + //
                                        "    \"stock\": 40,\n" + //
                                        "    \"category\": \"Productivity\",\n" + //
                                        "    \"price\": 30,\n" + //
                                        "    \"description\": \"The book that helps you win in business and life mastery.\",\n" + //
                                        "    \"imageURL\": \"NAN\"\n" + //
                                        "  },\n" + //
                                        "  {\n" + //
                                        "    \"bookID\": 18,\n" + //
                                        "    \"title\": \"Harry Potter\",\n" + //
                                        "    \"author\": \"J.K. Rowlling\",\n" + //
                                        "    \"published_date\": \"2007-06-21\",\n" + //
                                        "    \"stock\": 20,\n" + //
                                        "    \"category\": \"Novel\",\n" + //
                                        "    \"price\": 50,\n" + //
                                        "    \"description\": \"The best Novel ever\",\n" + //
                                        "    \"imageURL\": \"Fk\"\n" + //
                                        "  }\n" + //
                                        "]"
            );
            messages.add(systemMessage);

            // Add conversation history if provided
            if (chatRequest.getConversationHistory() != null && !chatRequest.getConversationHistory().isEmpty()) {
                for (ChatRequest.ChatMessage msg : chatRequest.getConversationHistory()) {
                    Map<String, String> historyMessage = new HashMap<>();
                    historyMessage.put("role", msg.getRole());
                    historyMessage.put("content", msg.getContent());
                    messages.add(historyMessage);
                }
            }

            // Add current user message
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", chatRequest.getMessage());
            messages.add(userMessage);

            // Prepare request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.3-70b-versatile"); // Using Groq's fast model
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1024);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Make API call
            String endpoint = apiUrl + "/chat/completions";
            System.out.println("Calling endpoint: " + endpoint);
            System.out.println("Request body: " + requestBody);
            
            Map<String, Object> response = restTemplate.postForObject(endpoint, entity, Map.class);
            
            System.out.println("Response received: " + response);

            // Extract response
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) firstChoice.get("message");
                    String content = message.get("content");
                    String model = (String) response.get("model");
                    
                    return new ChatResponse(content, model);
                }
            }

            return new ChatResponse("I apologize, but I'm having trouble processing your request right now. Please try again.", "athena");

        } catch (Exception e) {
            System.err.println("Error calling Groq API: " + e.getMessage());
            e.printStackTrace();
            return new ChatResponse("I'm sorry, I'm experiencing technical difficulties. Please try again later or contact support.", "athena");
        }
    }
}
