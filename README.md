# Professional Portfolio Website

A dynamic portfolio website built with Spring Boot that reads content from a PDF file and features an AI-powered chat assistant.

## Features

- 🎨 Modern, responsive design
- 📄 Dynamic content from PDF (Profile.pdf) - No hardcoded data
- 💼 Career timeline with work experiences
- 🎓 Education history
- 🤖 AI-powered chat assistant (OpenRouter integration)
- 📧 Contact information
- ⚡ Fast and lightweight

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 21
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **PDF Processing**: Apache PDFBox
- **AI Integration**: OpenRouter API (GPT-4o-mini)
- **Build Tool**: Maven

## Local Development

### Prerequisites
- Java 21
- Maven (or use included mvnw)

### Setup

1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/portfolio-website.git
cd portfolio-website
```

2. Create `.env` file in the project root:
```
OPENROUTER_API_KEY=your_api_key_here
```

3. Place your `Profile.pdf` in the project root

4. Run the application:
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

5. Open http://localhost:8080

## Project Structure

```
src/
├── main/
│   ├── java/com/portfolio/
│   │   ├── controller/     # Web controllers
│   │   ├── model/          # Data models
│   │   └── service/        # Business logic
│   └── resources/
│       ├── static/         # CSS, JS files
│       ├── templates/      # HTML templates (Thymeleaf)
│       └── application.properties
└── Profile.pdf             # Source of all content
```

## How It Works

The website reads your profile from `Profile.pdf` and automatically extracts:
- Name (from LinkedIn URL)
- Summary/About section
- Work Experience (companies, titles, dates, achievements)
- Education (degrees, institutions, dates)
- Skills (extracted from summary)
- Contact information (email, LinkedIn)

Just update the PDF and restart - no code changes needed!

## Pages

- **Home** (`/`) - Landing page with name and summary
- **About** (`/about`) - About section, skills, current position, education
- **Career** (`/career`) - Professional experience timeline
- **Portfolio** (`/portfolio`) - Projects showcase
- **Contact** (`/contact`) - Contact information
- **AI Chat** (`/chat`) - Chat with AI about your career

## Configuration

Edit `src/main/resources/application.properties` to configure:
- Server port (default: 8080)
- OpenRouter API settings
- Application name

## Deployment

See [DEPLOYMENT.md](DEPLOYMENT.md) for deployment instructions to Render.com or other platforms.

## License

Private project - All rights reserved
