package com.portfolio.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.portfolio.model.ProfileData;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.model}")
    private String model;

    @Autowired
    private ProfileService profileService;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private String getSystemPrompt() {
        String profileContent = profileService.getProfileContent();
        ProfileData profileData = profileService.getProfileData();
        String name = profileData.getName();
        
        if (name == null || name.isEmpty()) {
            name = "the professional";
        }
        
        String email = profileData.getEmail() != null ? profileData.getEmail() : "";
        String linkedin = profileData.getLinkedin() != null ? profileData.getLinkedin() : "";
        
        return "You are " + name + "'s digital twin - an AI assistant that represents their professional career and expertise.\n\n" +
               "Here is " + name + "'s complete professional profile information extracted from their LinkedIn PDF:\n\n" +
               profileContent + "\n\n" +
               "Contact Information:\n" +
               "- Email: " + email + "\n" +
               "- LinkedIn: " + linkedin + "\n\n" +
               "Your role:\n" +
               "- Answer questions about " + name + "'s career, experience, skills, and education based on the profile information above\n" +
               "- Provide insights into their technical expertise and project experience\n" +
               "- Help visitors understand their qualifications and professional background\n" +
               "- Be professional, friendly, and informative\n" +
               "- If asked about something not in the profile, politely say you don't have that information\n" +
               "- Encourage visitors to reach out via email or LinkedIn for more detailed discussions";
    }

    public String chat(String userMessage) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);

        JsonArray messages = new JsonArray();
        
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", getSystemPrompt());
        messages.add(systemMessage);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userMessage);
        messages.add(userMsg);

        requestBody.add("messages", messages);

        RequestBody body = RequestBody.create(
            gson.toJson(requestBody),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            
            return jsonResponse
                .getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("message")
                .get("content")
                .getAsString();
        }
    }
}
