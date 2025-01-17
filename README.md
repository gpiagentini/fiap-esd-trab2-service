# Desafio Técnico | Backend | Ideal CTVM

This is a technical challenge for the backend position at Ideal CTVM. The challenge was completed according to the following [documentation](https://github.com/idealctvm-public/backend-challenge-mid-level).


## Overview do Projeto

- **Framework**: Spring Boot 3.x
- **Java Version**: 21
- **Build Tool**: Maven
- **Architecture**: Hexagonal Architecture (Ports & Adapters)
- **Database**: Postgres
- **Docker**

## Project Structure

```text
src/
 └── main/
     ├── java/
     │   └── br/com/gpiagentini/api/
     │       ├── application/           # Application layer (use cases).
     │       │   ├── dto/               # Data transfer objects.
     │       │   ├── service/           # Application services.
     │       │   └── port/              # Input and output ports (interfaces).
     │       ├── domain/                # Domain layer (core business logic).
     │       ├── infrastructure/        # Infrastructure layer (adapters, controllers, etc.).
     │       │   ├── configuration/     # Framework configurations.
     │       │   ├── exceptions/        # Infrastructure related exceptions.
     │       │   ├── filter/            # Api filters (mainly focused on authentication).
     │       │   └── persistence/       # Repositories and data mappers.
     │       ├── adapters/              # Controllers, handlers and external api calls.
     │       │   ├── controllers/       # Controllers implementation.
     │       │   ├── external/          # External api implementation.
     └── resources/
         ├── application.properties             # Spring Boot default configuration
         ├── application-dev.properties         # Spring Boot configurations for development
         └── application-prod.properties        # Spring Boot configurations for production
````

## Managing Environment

In this project, we have two possible environments (`dev` and `prod`).
To alternate between the environments, we need to use the **environment variable** **SPRING_PROFILES_ACTIVE**.

> The following commands will only change your environment variable for the **current terminal** session. It's not a permanent approach.

### Linux
cmd
```
export SPRING_PROFILES_ACTIVE=<env>
```

### Windows
cmd
```
set SPRING_PROFILES_ACTIVE=<env>
```

Powershell
```
$env:SPRING_PROFILES_ACTIVE="<env>"
```

## Build and Running

### Build
Since our application uses Maven, as our Build Tool, the first thing to do in order to build and test this project is to execute the following command to clean and build the dependencies.
> This step will also generate a .jar file, so you need to run these commands before running the project with **Java** or **Docker**.

```
mvn clean install
```

### Running
To run this project, you can either run with Java itself, which is not recommended, since this project has a strong dependency on external database, or you can run with Docker Compose, which is configured to run our application and the Database.
On the scripts/docker folder, you can run the following command:

```
sudo docker-compose up -d
```

This will start the necessary applications for the project to run.

## Authentication
To use all the available actions in the API, its necessary to authenticate using the **/login** endpoint.
This challenge contains one user for authentication:

 - Login: service-user-1
 - Password: id34lCTVM

Once logged, the generated token must be used in every request, inside the **Authorization** header.
```
Authorization: Bearer <token>
```

## Documentation

With the project running, you can check for the endpoints documentation in http://localhost/swagger-ui/index.html.
It should display the Swagger Documentation for the Rest Endpoints.

![image](https://github.com/user-attachments/assets/6367eb0c-86ad-4a25-9cde-42b2af921587)

## Next Steps

For this challenge, some features are mocked and limited, for example the available asset list.
Here are some next steps:

 - Add real list of available assets in stock markets;
 - Add asset as a FK in **user_list**;
 - Add a user column in **user_list**, to allow more than one user list management.

## Contacts and Feedback
I would appreciate a lot for any feedback that would help me improve my skills!
Any questions, observations or problems, please contact me:

 - gmtpiagentini@gmail.com
 - [linkedin](https://www.linkedin.com/in/gpiagentini/)
 - https://gpiagentini.github.io/CV