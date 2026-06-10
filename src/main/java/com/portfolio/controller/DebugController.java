package com.portfolio.controller;

import com.portfolio.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/debug/pdf-content")
    public String getPdfContent() {
        return "<pre>" + profileService.getProfileContent().replace("<", "&lt;").replace(">", "&gt;") + "</pre>";
    }
    
    @GetMapping("/debug/profile-data")
    public String getProfileData() {
        var profile = profileService.getProfileData();
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Profile Data</h2>");
        sb.append("<p><strong>Name:</strong> ").append(profile.getName() != null ? profile.getName() : "NULL").append("</p>");
        sb.append("<p><strong>Email:</strong> ").append(profile.getEmail() != null ? profile.getEmail() : "NULL").append("</p>");
        sb.append("<p><strong>LinkedIn:</strong> ").append(profile.getLinkedin() != null ? profile.getLinkedin() : "NULL").append("</p>");
        sb.append("<p><strong>About:</strong> ").append(profile.getAbout() != null ? profile.getAbout() : "NULL").append("</p>");
        sb.append("<p><strong>Experiences:</strong> ").append(profile.getExperiences() != null ? profile.getExperiences().size() : 0).append("</p>");
        
        if (profile.getExperiences() != null) {
            sb.append("<h3>Experience Details:</h3>");
            for (int i = 0; i < profile.getExperiences().size(); i++) {
                var exp = profile.getExperiences().get(i);
                sb.append("<div style='margin: 10px; padding: 10px; border: 1px solid #ccc;'>");
                sb.append("<p><strong>").append(i).append(". Title:</strong> ").append(exp.getTitle() != null ? exp.getTitle() : "NULL").append("</p>");
                sb.append("<p><strong>Company:</strong> ").append(exp.getCompany() != null ? exp.getCompany() : "NULL").append("</p>");
                sb.append("<p><strong>Period:</strong> ").append(exp.getPeriod() != null ? exp.getPeriod() : "NULL").append("</p>");
                sb.append("<p><strong>Location:</strong> ").append(exp.getLocation() != null ? exp.getLocation() : "NULL").append("</p>");
                sb.append("</div>");
            }
        }
        
        sb.append("<p><strong>Education:</strong> ").append(profile.getEducations() != null ? profile.getEducations().size() : 0).append("</p>");
        sb.append("<p><strong>Skills:</strong> ").append(profile.getSkills() != null ? profile.getSkills().size() : 0).append("</p>");
        return sb.toString();
    }
}

