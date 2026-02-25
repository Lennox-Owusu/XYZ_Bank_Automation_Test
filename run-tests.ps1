# Copy environment.properties from resources to allure-results
Write-Host "Copying environment.properties..." -ForegroundColor Cyan
if (Test-Path src/test/resources/environment.properties) {
    Copy-Item -Force src/test/resources/environment.properties target/allure-results/environment.properties
    Write-Host "Environment file copied successfully!" -ForegroundColor Green
}

# Preserve history from previous Maven report
if (Test-Path target/site/allure-maven-plugin/history) {
    Write-Host "Copying history from previous report..." -ForegroundColor Green
    New-Item -ItemType Directory -Force -Path target/allure-results/history | Out-Null
    Copy-Item -Recurse -Force target/site/allure-maven-plugin/history/* target/allure-results/history/
    Write-Host "History copied successfully!" -ForegroundColor Green
} else {
    Write-Host "No previous history found. Building trend data..." -ForegroundColor Yellow
}

# Run tests and generate report
Write-Host "Running tests..." -ForegroundColor Cyan
mvn clean test

# Copy environment file again after clean (since clean deletes target folder)
Write-Host "Copying environment.properties after test run..." -ForegroundColor Cyan
if (Test-Path src/test/resources/environment.properties) {
    Copy-Item -Force src/test/resources/environment.properties target/allure-results/environment.properties
}

Write-Host "Generating Allure report..." -ForegroundColor Cyan
mvn allure:report

# Open report in browser
Write-Host "Opening report in browser..." -ForegroundColor Cyan
mvn allure:serve
