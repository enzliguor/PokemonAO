# Project Name

## Description
This is a study project conceived by Davide Costa and developed by:
1. Alfredo Esposito
2. Leonardo Abbatelli
3. Marianna Cilli
4. Nicola Messuti
5. Paolo Labriola
6. Vincenzo Liguori
7. Vitaliy Chayka

## Installation
To run this project in development mode, follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project directory.

### Development Configuration

#### Configure Database
Before running the project, you need to set up the database using Docker.  
Follow these steps to start a MySQL container with the required settings:
1. Open the terminal and navigate to the `utility/db` folder within your project.
2. Build the Docker image for the MySQL container using the provided Dockerfile:
```bash
docker build -t pokemon_ao_db_image .
```  
```bash
docker run -d -p 3310:3306 --name pokemon_ao_db pokemon_ao_db_image
```
Now you are ready to run the project in development mode with the correct database configuration.

## Deployment with Docker

To deploy the project using Docker, follow these steps:

1. Build the project using Gradle and copy the generated JAR file located in `build/libs` folder. Make sure the JAR file matches the one mentioned in the Dockerfile (e.g., PokemonAO-0.0.1-SNAPSHOT.jar).
2. Paste the JAR file in the `utility/deploy` folder where the docker-compose.yml file is located.
3. Open the terminal and navigate to the `utility/deploy` folder and Run the Docker Compose command:
```bash
docker-compose up -d
```