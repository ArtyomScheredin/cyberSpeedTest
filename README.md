# Cyber Speed Test Task

## Description  
Test task for a job application at **Cyber Speed**.  

## Build and Run  
### 1. Build the Shadow JAR  
Run the following command:  
```sh
./gradlew shadowJar
```
### 2. Run the Application
```sh
java -jar ./build/libs/cyberspeed-test-1.0-SNAPSHOT.jar --config ./src/main/resources/config.json --betting-amount 10
```

