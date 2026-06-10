package com.portfolio.controller;

import com.portfolio.model.ProfileData;
import com.portfolio.service.AIService;
import com.portfolio.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private AIService aiService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/chat")
    public String chatPage(Model model) {
        ProfileData profile = profileService.getProfileData();
        model.addAttribute("profile", profile);
        return "chat";
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            String userMessage = request.get("message");
            String aiResponse = aiService.chat(userMessage);
            response.put("response", aiResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to get response: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
