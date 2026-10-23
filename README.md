# Student Management System (Full Stack)

A full-stack student management application built with a Vanilla JavaScript frontend and a Java HTTP REST API connected to MySQL via JDBC.

## Architecture
- **Frontend:** HTML5, CSS3, JavaScript (Fetch API, responsive UI)
- **Backend:** Java HTTP Server (`com.sun.net.httpserver`), DAO Pattern
- **Database:** MySQL relational database accessed via JDBC

## Features
- Add student details with validation
- Live student table rendering
- Asynchronous data fetching (no page reloads)
- MySQL persistence with connection handling

## How to Run
1. Ensure MySQL is running with database `student_db`.
2. Start the Java backend server:
   ```bash
   javac -cp "src;lib/*" src/WebServer.java
   java -cp "src;lib/*" WebServer