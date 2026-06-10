package com.portfolio.service;

import com.portfolio.model.ProfileData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    private String profileContent = "";
    private ProfileData profileData;

    @PostConstruct
    public void init() {
        try {
            loadProfileFromPDF();
            profileData = parseProfileContent(profileContent);
        } catch (IOException e) {
            System.err.println("Could not load Profile.pdf: " + e.getMessage());
            // No fallback - leave empty
            profileContent = "";
            profileData = new ProfileData();
        }
    }

    private void loadProfileFromPDF() throws IOException {
        File pdfFile = new File("Profile.pdf");
        if (!pdfFile.exists()) {
            throw new IOException("Profile.pdf not found");
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            profileContent = stripper.getText(document);
            System.out.println("Successfully loaded profile from PDF");
        }
    }

    public String getProfileContent() {
        return profileContent;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    private ProfileData parseProfileContent(String content) {
        ProfileData data = new ProfileData();
        
        // Extract contact info first (needed for name fallback)
        data.setEmail(extractEmail(content));
        data.setLinkedin(extractLinkedin(content));
        
        // Extract name (with LinkedIn fallback)
        data.setName(extractName(content, data.getLinkedin()));
        
        // Parse About section
        data.setAbout(extractAbout(content));
        
        // Parse Experience
        data.setExperiences(extractExperiences(content));
        
        // Parse Education
        data.setEducations(extractEducation(content));
        
        // Parse Skills
        data.setSkills(extractSkills(content));
        
        return data;
    }

    private String extractName(String content, String linkedinUrl) {
        // First, try to extract from LinkedIn URL (more reliable)
        if (linkedinUrl != null && !linkedinUrl.isEmpty()) {
            Pattern pattern = Pattern.compile("linkedin\\.com/in/([a-zA-Z0-9\\-]+)");
            Matcher matcher = pattern.matcher(linkedinUrl);
            if (matcher.find()) {
                String slug = matcher.group(1);
                // Convert slug to name: "ella-chulpaev" -> "Ella Chulpaev"
                String[] parts = slug.split("-");
                StringBuilder name = new StringBuilder();
                for (String part : parts) {
                    if (!part.isEmpty() && part.matches("[a-zA-Z]+")) {
                        // Capitalize first letter
                        name.append(Character.toUpperCase(part.charAt(0)))
                            .append(part.substring(1).toLowerCase())
                            .append(" ");
                    }
                }
                String extractedName = name.toString().trim();
                if (!extractedName.isEmpty()) {
                    System.out.println("Extracted name from LinkedIn: " + extractedName);
                    return extractedName;
                }
            }
        }
        
        System.out.println("Trying to find name in PDF content");
        
        // Fallback: Try to find the name in the first 20 lines
        String[] lines = content.split("\\n");
        for (int i = 0; i < Math.min(20, lines.length); i++) {
            String line = lines[i].trim();
            
            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }
            
            System.out.println("Checking line " + i + ": '" + line + "'");
            
            // Look for a line that looks like a full name
            // Must be 2-4 words, mostly letters, reasonable length
            if (line.length() >= 3 && line.length() <= 60 &&
                line.matches(".*[A-Z].*[A-Z].*") && // At least 2 capital letters
                line.matches("[A-Za-z\\s\\-\\.]+") && // Only letters, spaces, hyphens, dots
                line.split("\\s+").length >= 2 && // At least 2 words
                line.split("\\s+").length <= 4 && // At most 4 words
                !line.matches("(?i).*(About|Experience|Education|Skills|Contact|Summary|Top|Israel|District|Engineer|Analyst|Manager|Developer|Consultant|Director|Specialist|Coordinator|Page|LinkedIn|Profile|Resume|CV|Full|Stack|Development|Software|Senior|Junior).*")) {
                System.out.println("Found name in PDF: " + line);
                return line;
            }
        }
        
        System.out.println("Could not extract name");
        return ""; // Return empty if not found
    }

    private String extractAbout(String content) {
        System.out.println("=== Starting extractAbout ===");
        
        // Try to extract from Summary section first
        // Match "Summary" followed by text until we hit "Experience" at the start of a line (section header)
        Pattern summaryPattern = Pattern.compile("Summary\\s*([\\s\\S]*?)(?=\\n\\s*Experience\\s*\\n|\\n\\s*Education\\s*\\n|$)", Pattern.CASE_INSENSITIVE);
        Matcher summaryMatcher = summaryPattern.matcher(content);
        if (summaryMatcher.find()) {
            System.out.println("Found Summary section");
            String summary = summaryMatcher.group(1).trim();
            System.out.println("Raw summary length: " + summary.length());
            System.out.println("Raw summary (first 100 chars): " + summary.substring(0, Math.min(100, summary.length())));
            
            // Clean up the text - join lines with spaces
            summary = summary.replaceAll("\\s*\\n\\s*", " ");
            // Remove multiple spaces
            summary = summary.replaceAll("\\s+", " ");
            // Remove any section headers that might have leaked in
            summary = summary.replaceAll("(?i)\\s*(Experience|Education|Skills|Licenses|Certifications)\\s*$", "").trim();
            
            System.out.println("Cleaned summary length: " + summary.length());
            System.out.println("Extracted Summary: " + summary);
            if (!summary.isEmpty() && summary.length() > 20) {
                return summary;
            }
        } else {
            System.out.println("Summary section not found");
        }
        
        // Fallback: Extract text between "About" and "Experience"
        Pattern pattern = Pattern.compile("About\\s*([\\s\\S]*?)(?=\\n\\s*Experience\\s*\\n|\\n\\s*Education\\s*\\n|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            System.out.println("Found About section");
            String about = matcher.group(1).trim();
            System.out.println("Raw about length: " + about.length());
            
            // Clean up the text - join lines with spaces
            about = about.replaceAll("\\s*\\n\\s*", " ");
            // Remove multiple spaces
            about = about.replaceAll("\\s+", " ");
            // Remove any section headers
            about = about.replaceAll("(?i)\\s*(Experience|Education|Skills|Licenses|Certifications)\\s*$", "").trim();
            
            System.out.println("Extracted About: " + about);
            return about.isEmpty() ? "" : about;
        } else {
            System.out.println("About section not found");
        }
        
        System.out.println("No About/Summary section found");
        return "";
    }

    private List<ProfileData.Experience> extractExperiences(String content) {
        List<ProfileData.Experience> experiences = new ArrayList<>();
        
        // Extract Experience section - must be on its own line
        Pattern expSection = Pattern.compile("\\n\\s*Experience\\s*\\n([\\s\\S]*?)(?=\\n\\s*Education\\s*\\n|\\n\\s*Skills\\s*\\n|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = expSection.matcher(content);
        
        if (!matcher.find()) {
            System.out.println("Experience section not found");
            return experiences;
        }
        
        String experienceText = matcher.group(1);
        System.out.println("Found Experience section, length: " + experienceText.length());
        
        String[] lines = experienceText.split("\\n");
        ProfileData.Experience currentExp = null;
        String lastCompanyName = "";
        boolean expectingTitle = false;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }
            
            System.out.println("Processing experience line: " + line);
            
            // Check for date patterns (indicates we have period info)
            if (line.matches(".*\\b\\d{4}\\b.*") && (line.contains("-") || line.contains("Present") || line.contains("·"))) {
                if (currentExp != null) {
                    currentExp.setPeriod(line);
                    System.out.println("  -> Set period: " + line);
                }
                continue;
            }
            
            // Check for location patterns
            if (line.matches(".*(Israel|District|Tel Aviv|Jerusalem|Haifa|Ramat Gan|Raanana|,\\s*[A-Z]{2}\\s*$).*") && 
                !line.matches(".*(Analyst|Engineer|Manager|Developer).*")) {
                if (currentExp != null) {
                    currentExp.setLocation(line);
                    System.out.println("  -> Set location: " + line);
                }
                continue;
            }
            
            // Check for bullet points (achievements) - must start with bullet character
            if (line.matches("^[•\\-*\\u2022\\u2023\\u2043\\u204C\\u204D\\u2219\\u25E6\\u25AA\\u25AB]\\s+.+")) {
                if (currentExp != null) {
                    String achievement = line.replaceFirst("^[•\\-*\\u2022\\u2023\\u2043\\u204C\\u204D\\u2219\\u25E6\\u25AA\\u25AB]\\s*", "").trim();
                    if (!achievement.isEmpty() && achievement.length() > 10) {
                        currentExp.getAchievements().add(achievement);
                        System.out.println("  -> Added achievement");
                    }
                }
                continue;
            }
            
            // Check if this line is a job title (contains common job keywords)
            if (line.matches(".*(Analyst|Engineer|Manager|Developer|Architect|Lead|Director|Consultant|Specialist|Coordinator|Administrator|Technician|Designer|Programmer|Scientist|Researcher|Officer|Associate|Assistant|Supervisor|Executive|VP|President|CEO|CTO|CFO).*")) {
                
                // Save previous experience if exists
                if (currentExp != null && currentExp.getTitle() != null) {
                    experiences.add(currentExp);
                    System.out.println("  -> Saved experience: " + currentExp.getTitle() + " at " + currentExp.getCompany());
                }
                
                // Start new experience
                currentExp = new ProfileData.Experience();
                currentExp.setTitle(line);
                System.out.println("  -> New experience title: " + line);
                
                // Use the last company name we found
                if (!lastCompanyName.isEmpty()) {
                    currentExp.setCompany(lastCompanyName);
                    System.out.println("  -> Set company from last: " + lastCompanyName);
                }
                
                expectingTitle = false;
                continue;
            }
            
            // Check if this line contains "Full-time", "Part-time", "Contract" etc.
            // This usually appears after company name in LinkedIn format
            if (line.matches(".*·\\s*(Full-time|Part-time|Contract|Freelance|Internship).*")) {
                // The company name should have been set already
                expectingTitle = true;
                System.out.println("  -> Found employment type, expecting title next");
                continue;
            }
            
            // If we reach here, this line might be a company name
            // Store it as the last company name
            // But only if it doesn't look like a section header or metadata
            if (line.length() > 2 && line.length() < 100 && 
                !line.matches("^[0-9]+.*") && 
                !line.startsWith("Page") &&
                !line.matches("(?i).*(^Experience$|^Education$|^Skills$|^Summary$|^Contact$|^About$)")) {
                
                // This is likely a company name
                lastCompanyName = line;
                System.out.println("  -> Set last company name: " + line);
                expectingTitle = true;
            }
        }
        
        // Add last experience
        if (currentExp != null && currentExp.getTitle() != null) {
            experiences.add(currentExp);
            System.out.println("  -> Saved last experience: " + currentExp.getTitle() + " at " + currentExp.getCompany());
        }
        
        System.out.println("Total experiences extracted: " + experiences.size());
        return experiences;
    }

    private List<ProfileData.Education> extractEducation(String content) {
        List<ProfileData.Education> educations = new ArrayList<>();
        
        // Extract Education section
        Pattern eduSection = Pattern.compile("Education[\\s\\S]*?(?=Skills|Contact|Licenses|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = eduSection.matcher(content);
        
        if (!matcher.find()) {
            return educations;
        }
        
        String educationText = matcher.group();
        String[] lines = educationText.split("\\n");
        ProfileData.Education currentEdu = null;
        String previousLine = "";
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.equalsIgnoreCase("Education")) {
                previousLine = line;
                continue;
            }
            
            // Check if this line contains a degree (PhD, Master, Bachelor)
            if (line.matches(".*(PhD|Master|Bachelor|Degree).*")) {
                // Save previous education if exists
                if (currentEdu != null && currentEdu.getDegree() != null) {
                    educations.add(currentEdu);
                }
                
                // Start new education
                currentEdu = new ProfileData.Education();
                
                // Extract degree and field from this line
                String[] parts = line.split("·")[0].split(",");
                
                if (parts.length >= 1) {
                    String degreeField = parts[0].trim();
                    degreeField = degreeField.replaceAll("\\s*-\\s*(MS|BS|PhD)\\s*", "");
                    currentEdu.setDegree(degreeField);
                }
                
                if (parts.length >= 2) {
                    currentEdu.setField(parts[1].trim());
                }
                
                // Try to extract dates from current line
                Pattern datePattern = Pattern.compile("\\((.*?\\d{4}.*?\\d{4}.*?)\\)");
                Matcher dateMatcher = datePattern.matcher(line);
                if (dateMatcher.find()) {
                    String period = dateMatcher.group(1).trim();
                    period = period.replaceAll("(January|February|March|April|May|June|July|August|September|October|November|December)\\s+", "");
                    currentEdu.setPeriod(period);
                }
                
                // If no dates found in current line, check next line for dates
                // Format might be split: line 1 ends with "(December" and line 2 has "2023 - September 2025)"
                if (currentEdu.getPeriod() == null && i + 1 < lines.length) {
                    String nextLine = lines[i + 1].trim();
                    // Check if next line contains year pattern with closing parenthesis
                    if (nextLine.matches(".*\\d{4}.*\\d{4}.*\\).*")) {
                        String period = nextLine.replaceAll("[()]", "").trim();
                        period = period.replaceAll("(January|February|March|April|May|June|July|August|September|October|November|December)\\s+", "");
                        currentEdu.setPeriod(period);
                    }
                }
                
                // The previous non-empty line is the institution
                if (!previousLine.isEmpty() && 
                    !previousLine.equalsIgnoreCase("Education") &&
                    !previousLine.matches(".*(PhD|Master|Bachelor|Degree).*")) {
                    currentEdu.setInstitution(previousLine);
                }
            }
            
            previousLine = line;
        }
        
        // Add last education
        if (currentEdu != null && currentEdu.getDegree() != null) {
            educations.add(currentEdu);
        }
        
        return educations;
    }

    private List<String> extractSkills(String content) {
        List<String> skills = new ArrayList<>();
        
        // Company names to exclude (blacklist)
        String[] companyBlacklist = {
            "Maccabi", "Bank Hapoalim", "ACUM", "Amdocs", 
            "Health", "Healthcare", "Services", "Bank", "Group", "Ltd", "Inc", "Corp"
        };
        
        // Extract skills from Summary section (most relevant to current role)
        Pattern summaryPattern = Pattern.compile("Summary\\s*([\\s\\S]*?)(?=\\n\\s*Experience\\s*\\n|\\n\\s*Education\\s*\\n|$)", Pattern.CASE_INSENSITIVE);
        Matcher summaryMatcher = summaryPattern.matcher(content);
        
        String summaryText = "";
        if (summaryMatcher.find()) {
            summaryText = summaryMatcher.group(1);
            System.out.println("Extracting skills from Summary section");
        }
        
        // Common technical and professional skills to look for
        String[] skillKeywords = {
            // Technical
            "Java", "Spring", "Spring Boot", "Hibernate", "JPA", "Maven", "Jenkins", 
            "Git", "DB2", "Oracle", "SQL", "JavaScript", "HTML", "CSS", "jQuery", "AJAX", "JSON",
            "Unix", "Linux", "Docker", "Kubernetes", "AWS", "Azure", "CI/CD", "ANT", "XML",
            "Microservices", "REST", "API", "Big Data",
            // Methodologies & Practices
            "Agile", "Scrum", "DevOps", "TDD",
            // Analysis & Design
            "Systems Analysis", "System Design", "Architecture", "Requirements Analysis",
            "Technical Documentation", "UML", "Data Modeling",
            // Soft Skills & Roles
            "Problem Solving", "Troubleshooting", "Project Management", "Team Leadership",
            "Stakeholder Management", "Technical Leadership",
            // Tools
            "Kiro IDE", "JIRA", "Confluence", "IntelliJ"
        };
        
        // Extract skills mentioned in summary
        for (String skill : skillKeywords) {
            if (summaryText.toLowerCase().contains(skill.toLowerCase())) {
                // Check if this skill matches any company name in blacklist
                boolean isCompanyName = false;
                for (String company : companyBlacklist) {
                    if (skill.equalsIgnoreCase(company)) {
                        isCompanyName = true;
                        System.out.println("  -> Skipping company name: " + skill);
                        break;
                    }
                }
                
                if (!isCompanyName && !skills.contains(skill)) {
                    skills.add(skill);
                    System.out.println("  -> Found skill in summary: " + skill);
                    
                    // Limit to 10 skills
                    if (skills.size() >= 10) {
                        break;
                    }
                }
            }
        }
        
        // If we don't have enough skills from summary, check the first experience entry
        if (skills.size() < 10) {
            System.out.println("Extracting additional skills from Experience section");
            Pattern expPattern = Pattern.compile("\\n\\s*Experience\\s*\\n([\\s\\S]*?)(?=\\n\\s*Education\\s*\\n|$)", Pattern.CASE_INSENSITIVE);
            Matcher expMatcher = expPattern.matcher(content);
            
            if (expMatcher.find()) {
                String expText = expMatcher.group(1);
                // Only check first 500 characters (first job entry)
                String firstExp = expText.substring(0, Math.min(500, expText.length()));
                
                for (String skill : skillKeywords) {
                    if (firstExp.toLowerCase().contains(skill.toLowerCase())) {
                        // Check if this skill matches any company name in blacklist
                        boolean isCompanyName = false;
                        for (String company : companyBlacklist) {
                            if (skill.equalsIgnoreCase(company)) {
                                isCompanyName = true;
                                System.out.println("  -> Skipping company name: " + skill);
                                break;
                            }
                        }
                        
                        if (!isCompanyName && !skills.contains(skill)) {
                            skills.add(skill);
                            System.out.println("  -> Found skill in experience: " + skill);
                            
                            if (skills.size() >= 10) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("Total skills extracted: " + skills.size());
        return skills;
    }

    private String extractEmail(String content) {
        // Extract email using regex pattern
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher matcher = emailPattern.matcher(content);
        if (matcher.find()) {
            return matcher.group();
        }
        return ""; // Return empty string if not found
    }

    private String extractLinkedin(String content) {
        // Extract LinkedIn URL - look for linkedin.com/in/ pattern (case insensitive)
        Pattern linkedinPattern = Pattern.compile("(?i)linkedin\\.com/in/[a-zA-Z0-9\\-]+");
        Matcher matcher = linkedinPattern.matcher(content);
        if (matcher.find()) {
            String url = matcher.group().toLowerCase();
            System.out.println("Found LinkedIn URL: " + url);
            return url;
        }
        
        // Try alternative pattern with www
        Pattern altPattern = Pattern.compile("(?i)www\\.linkedin\\.com/in/[a-zA-Z0-9\\-]+");
        matcher = altPattern.matcher(content);
        if (matcher.find()) {
            String url = matcher.group().toLowerCase().replace("www.", "");
            System.out.println("Found LinkedIn URL (with www): " + url);
            return url;
        }
        
        // Try pattern with https://
        Pattern httpsPattern = Pattern.compile("(?i)https?://(?:www\\.)?linkedin\\.com/in/[a-zA-Z0-9\\-]+");
        matcher = httpsPattern.matcher(content);
        if (matcher.find()) {
            String url = matcher.group().toLowerCase().replaceAll("https?://(?:www\\.)?", "");
            System.out.println("Found LinkedIn URL (with https): " + url);
            return url;
        }
        
        System.out.println("No LinkedIn URL found in PDF");
        return ""; // Return empty string if not found
    }
}
