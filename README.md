# WDM Configuration

A Service with an API to handle a business module configuration. 
Built with Spring-boot, Spring-data, Hibernate, Lombook and much more.
Unit test built mostly with Jupiter and Mockito.

With a BLUE/GREEN table deployment paradigm. 
With an MVC design pattern and some dependency injection available with the Spring Framework.
With Flyway for database migration and Docker. 

Prepared to be deployed in a Cloud enviroment on Kubernetes. 

* [Software Requirements](#software-requirements)
* [Build](#build)
* [Run Locally](#run-locally)
* [Swagger](#swagger-ui-and-documentation)
* [Recommendations](#recommendations)
---
## Software Requirements

* Java 11.0.9 version (OpenJDK Runtime Environment (build 11.0.9+11)
* Docker version 20.10.14, build a224086

---
## Build

Maven is used to build the repository:

```bash
$ make install
```

---
## Run Locally
### Set local network (for docker use) 
```bash
$ make set-local-network
```

### Run the database

```bash
$ make database-up
$ make database-migrate
```

### Run the service

```bash
$ make run
```
---
## List of make commands

| Command                 | Description                                             |
|-------------------------|---------------------------------------------------------|
| `make install`          | Runs the maven install to build the project             |
| `make run`              | Runs the service                                        |
| `make set-local-network`| Creates a "local network" to be used by Docker          |
| `make database-up`      | Creates the database instance in your local Docker      |
| `make database-down`    | Stops the local database container execution            |
| `make database-migrate` | Applies the flyway files to you local database instance |
| `make database-connect` | Connects to your local database container               |


## Swagger UI and Documentation

The project comes with an embedded Swagger UI and schemas are available. An example is provided using localhost

| Resource    | URL                                    |
|-------------|----------------------------------------|
| Swagger UI  | http://localhost:8080/swagger-ui.html  |
| JSON Schema | http://localhost:8080/v3/api-docs      |
| YAML Schema | http://localhost:8080/v3/api-docs.yaml |

## Recommendations

* Consider adding database audit. 
* Adding security using spring-security framework. At least a OTP authentication. 