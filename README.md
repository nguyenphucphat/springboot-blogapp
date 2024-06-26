# SPRING BOOT REST API BASIC PROJECT
This is a basic project for Spring Boot REST API. It includes basic CRUD operations for a single entity. The project is built using Maven and uses MySQL database for data storage and JWT to secure. The project is built using Spring Boot 3.2.2
# Project is deployed in AWS Elastic Beanstalk and can be accessed using the following URL
```bash
http://blog.ap-southeast-2.elasticbeanstalk.com/
```
# How to run the project
### 1. From the IDE
1. Clone the project
2. Open the project in your favorite IDE
3. If AWS RDS is not available. Use local MySQL database. Create a database and update the `application.properties` file with your MySQL username and password.
4. Run the project as a Spring Boot application
5. The project will start on port 8080
6. You can access the project at http://localhost:8080
### 2. From the command line with Maven
1. Clone the project
2. Navigate to the project directory
3. Run the following command
```bash
mvn spring-boot:run
```
4. The project will start on port 8080
### 3. From the command line with Jar File
1. Clone the project
2. Navigate to the project directory
3. Run the following command
```bash
mvnw package
```
4. Run the following command
```bash
java -jar target/springboot-blog-application-rest-api-0.0.1-SNAPSHOT.jar
```
5. The project will start on port 8080

# How to test the project with OpenAPI
1. Run the project
2. Open the following URL in your browser
```bash
http://localhost:8080/swagger-ui/index.html
```
3. You can test the project from the Swagger UI
4. You can also test the project using Postman or any other API testing tool
