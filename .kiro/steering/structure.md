---
inclusion: auto
---

# Project Structure

## Architecture Pattern

Standard Spring Boot MVC architecture with three-layer separation:
- Controllers: Handle HTTP requests and routing
- Services: Business logic and external integrations
- Models: Data structures

## Directory Layout

```
src/main/java/com/portfolio/
├── controller/          # HTTP request handlers
│   ├── HomeController.java      # Main pages (/, /about, /career, /portfolio)
│   └── ChatController.java      # AI chat endpoints (/chat, /api/chat)
├── service/            # Business logic layer
│   ├── ProfileService.java      # PDF parsing and profile data extraction
│   └── AIService.java           # OpenRouter API integration
├── model/              # Data models
│   └── ProfileData.java         # Profile data structure (name, about, experience, education)
└── PortfolioApplication.java    # Spring Boot entry point

src/main/resources/
├── static/             # Static assets
│   ├── css/           # Stylesheets
│   │   ├── style.css           # Main site styles
│   │   └── chat.css            # Chat interface styles
│   └── js/            # Client-side JavaScript
│       ├── script.js           # Main site interactions
│       └── chat.js             # Chat interface logic
├── templates/          # Thymeleaf HTML templates
│   ├── index.html              # Home page
│   ├── about.html              # About section
│   ├── career.html             # Career timeline
│   ├── portfolio.html          # Projects showcase
│   ├── contact.html            # Contact information
│   └── chat.html               # AI chat interface
└── application.properties       # Spring Boot configuration
```

## Root Level Files

- Profile.pdf: Source of all dynamic content (parsed by ProfileService)
- pom.xml: Maven dependencies and build configuration
- .env: Environment variables (API keys, not in git)
- render.yaml: Render.com deployment configuration
- system.properties: Java version specification for deployment

## Conventions

- Package structure: com.portfolio.{layer}
- Controllers use @GetMapping/@PostMapping annotations
- Services are @Service annotated and injected via constructor
- Models are POJOs with getters/setters
- Templates use Thymeleaf syntax (th:* attributes)
- Static resources served from /static path
- PDF content loaded at application startup via @PostConstruct
