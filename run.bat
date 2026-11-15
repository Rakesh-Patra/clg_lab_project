@echo off
setlocal
cd /d "%~dp0"
where java >nul 2>nul || (echo Install JDK 17 or newer, then reopen this terminal.& exit /b 1)
where mvn >nul 2>nul || (echo Install Apache Maven 3.9 or newer, then reopen this terminal.& exit /b 1)
call mvn clean compile exec:java
