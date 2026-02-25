Write-Host "Copying environment.properties..." -ForegroundColor Cyan
Copy-Item -Force src/test/resources/environment.properties target/allure-results/environment.properties

Write-Host "Opening Allure report..." -ForegroundColor Cyan
mvn allure:serve
