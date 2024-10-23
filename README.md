
# Recommendation-System-Book-Backend

## Project Description
This is the backend service for the Book Recommendation System. The project is built using Spring Boot and provides APIs for recommending books to users based on their ratings and preferences. The backend handles user authentication, book data processing, and interactions with the MySQL database to store and retrieve information.

### Key Features:
- **REST API**: Built using Spring Boot and provides endpoints for user interactions.
- **User Authentication**: JWT-based authentication system using Spring Security.
- **Book Recommendations**: Personalized book recommendations based on collaborative filtering.
- **Data Storage**: Utilizes MySQL for persistent storage of user and book data.
- **File Handling**: Processes CSV and Excel files using Apache POI and OpenCSV.
- **API Documentation**: Integrated with Springdoc OpenAPI for easy API testing and visualization.
  
## Technologies Used
- **Spring Boot** 3.3.4
- **Java** 21
- **MySQL** (with `mysql-connector-j`)
- **JWT** (for secure user authentication with `jjwt`)
- **Spring Security** (for managing access control)
- **OpenCSV** (for handling CSV files)
- **Apache POI** (for handling Excel files)
- **Spring Data JPA** (for database interaction)
- **Springdoc OpenAPI** (for automatic API documentation)
- **JUnit and Spring Security Test** (for testing)

## Getting Started

### Prerequisites
- Java 21 installed.
- Maven installed.
- MySQL database configured.
- A valid `.env` file with the following environment variables:
  ```bash
  DB_HOST=<your_db_host>
  DB_PORT=<your_db_port>
  DB_NAME=<your_db_name>
  DB_USER=<your_db_user>
  DB_PASSWORD=<your_db_password>
  JWT_SECRET=<your_jwt_secret>
  ```

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/mohamedelhaddioui/Recommendation-System-Book-Backend.git
   cd Recommendation-System-Book-Backend
   ```

2. Install the dependencies using Maven:
   ```bash
   mvn clean install
   ```

3. Set up your MySQL database and update the `application.properties` file accordingly:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
   spring.datasource.username=your_db_user
   spring.datasource.password=your_db_password
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### API Documentation
Once the server is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

## Testing
Run the tests using:
```bash
mvn test
```

## License
This project is licensed under the MIT License.
