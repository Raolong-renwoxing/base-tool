# Spring AI Project with Multiple MySQL and Hive Connections

This project demonstrates a Spring Boot application that integrates Spring AI with multiple MySQL and Hive database connections.

## Features

- Spring AI integration with OpenAI
- Multiple MySQL database connections for relational data storage
- Multiple Hive database connections for big data processing
- OkHttp integration for external API calls
- RESTful API endpoints for AI, MySQL, Hive, and external API operations

## Prerequisites

- Java 20 or higher
- Maven
- MySQL server(s)
- Hive server(s)
- OpenAI API key

## Configuration

### Multiple Database Configuration

The application is configured to connect to multiple MySQL and Hive databases. Update the connection details in `application.yml`:

```yaml
mysql:
  datasources:
    primary:
      url: jdbc:mysql://localhost:3306/primary_db
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
    secondary:
      url: jdbc:mysql://localhost:3306/secondary_db
      username: root
      password: password
      driver-class-name: com.mysql.cj.jdbc.Driver
      
hive:
  datasources:
    primary:
      url: jdbc:hive2://localhost:10000/default
      username: hive
      password: hive
      driver-class-name: org.apache.hive.jdbc.HiveDriver
    secondary:
      url: jdbc:hive2://localhost:10001/secondary
      username: hive
      password: hive
      driver-class-name: org.apache.hive.jdbc.HiveDriver
```

### OpenAI Configuration

Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY=your_api_key_here
```

## Building and Running

To build the project:

```bash
mvn clean package
```

To run the application:

```bash
java -jar target/spring-ai-project-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### AI Endpoints

- `POST /api/ai/generate` - Generate AI responses

### MySQL Endpoints

Primary Database:
- `GET /api/users` - Get all users
- `POST /api/users` - Create a new user

Secondary Database:
- `GET /api/products` - Get all products
- `POST /api/products` - Create a new product

### Hive Endpoints

- `POST /api/hive/primary/query` - Execute a query on the primary Hive connection
- `POST /api/hive/secondary/query` - Execute a query on the secondary Hive connection

### External API Endpoints (OkHttp)

- `POST /api/external` - Call an external API synchronously
- `POST /api/external/async` - Call an external API asynchronously

### Status Endpoint

- `GET /api/status` - Check the status of all database connections

## Dependencies

The project uses the following major dependencies:

- Spring Boot 3.2.3
- Spring AI 0.8.1
- Spring Data JPA
- MySQL Connector
- Hive JDBC 3.1.3
- Hadoop Common 3.3.6
- OkHttp 4.12.0

## Dependency Conflict Resolution

This project carefully manages dependency conflicts between Spring Boot, Hive, and Hadoop libraries by:

1. Excluding conflicting dependencies from Hive and Hadoop
2. Using compatible versions of all libraries
3. Configuring separate data sources for MySQL and Hive

## Multi-Database Architecture

The project uses the following approach to manage multiple database connections:

1. **MySQL Connections**:
   - Uses Spring's `@ConfigurationProperties` to configure multiple data sources
   - Configures separate entity manager factories for each data source
   - Uses separate repository packages for each data source

2. **Hive Connections**:
   - Configures multiple JDBC templates for different Hive connections
   - Provides service methods to interact with each Hive connection

## External API Integration

The project integrates OkHttp for making external API calls:

1. **OkHttp Client**:
   - Configured with appropriate timeouts and connection pooling
   - Includes logging interceptor for debugging

2. **API Client Service**:
   - Provides synchronous and asynchronous methods for GET and POST requests
   - Handles JSON serialization and deserialization
   - Includes error handling and response parsing

3. **REST Endpoints**:
   - `/api/external` for synchronous API calls
   - `/api/external/async` for asynchronous API calls

### Example Usage

```json
// POST to /api/external
{
  "url": "https://api.example.com/data",
  "method": "GET"
}

// POST to /api/external with POST method
{
  "url": "https://api.example.com/data",
  "method": "POST",
  "body": {
    "key1": "value1",
    "key2": "value2"
  }
}
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.