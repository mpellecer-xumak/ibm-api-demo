.PHONY: help

## ======= Local spring commands =======
install: ## Run Maven package
	./mvnw clean install

run: ## Run Spring Boot application
	./mvnw spring-boot:run

debug: ## Run Spring Boot application in debug mode for development
	./mvnw clean install
	./mvnw clean package -DskipTests
	./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5050"

## ======= Database commands =======
database-up: ## Start up local database
	docker run --name configuration-pg -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d -p 5436:5432 --net=local_network postgres:11

database-down: ## Shutdown local database
	docker stop configuration-pg

database-migrate: ## Use flyway to create required schemas and tables
	docker run --net=local_network --rm -v $(CURDIR)/schemas/flyway_config/localhost:/flyway/conf -v $(CURDIR)/schemas:/flyway/sql flyway/flyway migrate

database-remove:
	docker rm configuration-pg

database-connect: ## Connect to local database
	docker exec -it -u postgres configuration-pg psql

help:
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)
