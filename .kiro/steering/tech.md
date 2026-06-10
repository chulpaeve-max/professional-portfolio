---
inclusion: auto
---

# Technology Stack

## Core Technologies

- Java 21
- Spring Boot 3.2.0
- Maven (build system)
- Thymeleaf (templating engine)

## Key Dependencies

- spring-boot-starter-web: REST controllers and web layer
- spring-boot-starter-thymeleaf: Server-side HTML rendering
- Apache PDFBox 2.0.29: PDF parsing and content extraction
- OkHttp 4.12.0: HTTP client for API calls
- Gson 2.10.1: JSON serialization/deserialization

## Frontend

- HTML5 with Thymeleaf templates
- CSS3 (custom stylesheets in static/css/)
- Vanilla JavaScript (no frameworks)

## External Integrations

- OpenRouter API: AI chat functionality using GPT-4o-mini
- API key stored in .env file (OPENROUTER_API_KEY)

## Common Commands

### Build and Run
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Package
```bash
# Windows
mvnw.cmd clean package

# Linux/Mac
./mvnw clean package
```

### Run packaged JAR
```bash
java -jar target/professional-website-1.0.0.jar
```

## Configuration

- Application config: src/main/resources/application.properties
- Default port: 8080
- Environment variables: .env file (not committed to git)
- Deployment config: render.yaml (for Render.com)
- Java version: system.properties (for deployment platforms)
