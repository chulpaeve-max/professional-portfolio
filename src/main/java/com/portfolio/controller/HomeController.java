package com.portfolio.controller;

import com.portfolio.model.ProfileData;
import com.portfolio.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProfileService profileService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/about";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        ProfileData profile = profileService.getProfileData();
        model.addAttribute("profile", profile);
        return "about";
    }
    
    @GetMapping("/career")
    public String career(Model model) {
        ProfileData profile = profileService.getProfileData();
        model.addAttribute("profile", profile);
        return "career";
    }
    
    @GetMapping("/portfolio")
    public String portfolio(Model model) {
        ProfileData profile = profileService.getProfileData();
        model.addAttribute("profile", profile);
        return "portfolio";
    }
    
    @GetMapping("/contact")
    public String contact(Model model) {
        ProfileData profile = profileService.getProfileData();
        model.addAttribute("profile", profile);
        return "contact";
    }
}
