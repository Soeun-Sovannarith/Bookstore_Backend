package com.example.bookstore.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.dto.ChatRequest;
import com.example.bookstore.dto.ChatResponse;
import com.example.bookstore.service.AthenaService;

@RestController
@RequestMapping("/api/athena")
public class AthenaController {

    @Autowired
    private AthenaService athenaService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        try {
            System.out.println("Received chat request: " + chatRequest.getMessage());
            
            if (chatRequest.getMessage() == null || chatRequest.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ChatResponse("Please provide a message.", "athena"));
            }

            ChatResponse response = athenaService.chat(chatRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in chat controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(new ChatResponse("I'm sorry, I'm experiencing technical difficulties. Please try again later.", "athena"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "online");
        response.put("assistant", "Athena");
        response.put("message", "AI Customer Service Assistant is ready to help!");
        return ResponseEntity.ok(response);
    }
}
