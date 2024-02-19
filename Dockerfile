# Base image with Java and Maven
FROM maven:3.8.4-openjdk-17
WORKDIR /app

# Copy the pom.xml file and source code
COPY pom.xml .
COPY . .

# Build the project
RUN mvn clean install package

# The command to run the application
ENTRYPOINT [ "java", "-jar", "target/chatbot-java-1.0-SNAPSHOT.jar" ]
