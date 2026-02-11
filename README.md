# GitHub Repository Searcher

Spring Boot backend application that integrates with the GitHub REST API to fetch repository data, store it in PostgreSQL, and expose filtered retrieval endpoints.

---

## 🚀 Features

- Search GitHub repositories by:
  - Repository name (partial/full match)
  - Programming language
  - Sort (stars, forks, updated date)

- Store results in PostgreSQL
- Upsert behavior (updates existing repositories)
- Filter stored repositories by:
  - Language
  - Minimum stars
  - Sorting
  - Pagination
- Global exception handling
- GitHub rate-limit handling
- Optional GitHub Personal Access Token support
- JUnit testing with H2 in-memory database

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- PostgreSQL
- WebClient (WebFlux)
- JUnit 5
- H2 (for testing)

---

## ⚙️ Setup Instructions

### 1️⃣ Clone Project

```bash
git clone <your-repo-url>
cd github-searcher
2️⃣ Configure PostgreSQL

Create database:

CREATE DATABASE githubdb;


Update application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/githubdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword

3️⃣ (Optional) Add GitHub Personal Access Token

To increase GitHub API rate limits:

In application.properties:

github.token=your_github_personal_access_token


If not provided, public rate limit (60/hr) applies.

4️⃣ Run Application

Using Maven:

mvn spring-boot:run


Or run main class from IDE.

Application runs on:

http://localhost:8080

📌 API Endpoints
🔎 1️⃣ Search GitHub Repositories
POST /api/github/search

Request Body:
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}

Response:
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [...],
  "totalCount": 20
}

📂 2️⃣ Retrieve Stored Repositories
GET /api/github/repositories

Query Parameters:
Parameter	Description
language	Filter by programming language
minStars	Minimum stars count
sort	stars / forks / lastUpdated
page	Page number (default 0)
size	Page size (default 10)
Example:
GET /api/github/repositories?language=Java&minStars=100&sort=stars&page=0&size=5

🧪 Running Tests
mvn test


Uses H2 in-memory database

Includes repository layer testing

🧠 Design Decisions

GitHub ID used as primary key for upsert behavior

Layered architecture (Controller → Service → Repository)

Custom exceptions for clean error handling

Secure configuration for GitHub token

Pagination implemented using Spring Data PageRequest

📊 Improvements (Future Scope)

Add caching for GitHub responses

Add OpenAPI (Swagger) documentation

Add CI/CD pipeline

Add request validation layer

👩‍💻 Author

Pravallika Chitturi