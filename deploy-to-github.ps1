# PowerShell script to deploy to GitHub
# Usage: .\deploy-to-github.ps1

Write-Host "=== Portfolio Website - GitHub Deployment ===" -ForegroundColor Cyan
Write-Host ""

# Check if git is initialized
if (-not (Test-Path ".git")) {
    Write-Host "Initializing Git repository..." -ForegroundColor Yellow
    git init
    Write-Host "✓ Git initialized" -ForegroundColor Green
} else {
    Write-Host "✓ Git already initialized" -ForegroundColor Green
}

# Get GitHub repository URL
Write-Host ""
Write-Host "Enter your GitHub repository URL:" -ForegroundColor Yellow
Write-Host "Example: https://github.com/username/portfolio-website.git" -ForegroundColor Gray
$repoUrl = Read-Host "Repository URL"

if ([string]::IsNullOrWhiteSpace($repoUrl)) {
    Write-Host "❌ Repository URL is required!" -ForegroundColor Red
    exit 1
}

# Check if remote exists
$remoteExists = git remote get-url origin 2>$null
if ($remoteExists) {
    Write-Host "Remote 'origin' already exists. Updating..." -ForegroundColor Yellow
    git remote set-url origin $repoUrl
} else {
    Write-Host "Adding remote 'origin'..." -ForegroundColor Yellow
    git remote add origin $repoUrl
}
Write-Host "✓ Remote configured" -ForegroundColor Green

# Add all files
Write-Host ""
Write-Host "Adding files to Git..." -ForegroundColor Yellow
git add .
Write-Host "✓ Files added" -ForegroundColor Green

# Commit
Write-Host ""
Write-Host "Enter commit message (or press Enter for default):" -ForegroundColor Yellow
$commitMsg = Read-Host "Commit message"
if ([string]::IsNullOrWhiteSpace($commitMsg)) {
    $commitMsg = "Update portfolio website"
}

git commit -m $commitMsg
Write-Host "✓ Changes committed" -ForegroundColor Green

# Push to GitHub
Write-Host ""
Write-Host "Pushing to GitHub..." -ForegroundColor Yellow
git branch -M main
git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✓ Successfully pushed to GitHub!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Go to https://render.com" -ForegroundColor White
    Write-Host "2. Click 'New +' → 'Web Service'" -ForegroundColor White
    Write-Host "3. Connect your GitHub repository" -ForegroundColor White
    Write-Host "4. Render will auto-detect settings from render.yaml" -ForegroundColor White
    Write-Host "5. Add your OPENROUTER_API_KEY in Environment Variables" -ForegroundColor White
    Write-Host "6. Click 'Create Web Service'" -ForegroundColor White
    Write-Host ""
    Write-Host "See DEPLOYMENT.md for detailed instructions!" -ForegroundColor Yellow
} else {
    Write-Host ""
    Write-Host "❌ Push failed. Please check the error above." -ForegroundColor Red
    Write-Host "Common issues:" -ForegroundColor Yellow
    Write-Host "- Make sure you have access to the repository" -ForegroundColor White
    Write-Host "- Check if you need to authenticate with GitHub" -ForegroundColor White
    Write-Host "- Verify the repository URL is correct" -ForegroundColor White
}
