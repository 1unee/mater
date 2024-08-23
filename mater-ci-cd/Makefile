# metadata below
PROJECT_PATH_PREFIX := ./..
BACKEND_PATH_PREFIX := $(PROJECT_PATH_PREFIX)/mater-rest
FRONTEND_PATH_PREFIX := $(PROJECT_PATH_PREFIX)/mater-webapp
CI_CD_PATH_PREFIX := $(PROJECT_PATH_PREFIX)/mater-ci-cd
ENV_PATH_PREFIX := $(BACKEND_PATH_PREFIX)/src/main/resources
ENV_DEFAULT_VALUE := DEFAULT
get_env_var = $(shell yq eval '.${1}' '$(ENV_PATH_PREFIX)/.env.yml')

DEFAULT_DOCKER_COMPOSE_FILE := docker-compose.yml
DOCKER_COMPOSE_PREFIX_PLUGIN = docker compose -f ./$(DEFAULT_DOCKER_COMPOSE_FILE)
BACKEND_POM_XML := $(BACKEND_PATH_PREFIX)/pom.xml

# environment variables below
APP_NAME := $(shell mvn -f $(BACKEND_POM_XML) help:evaluate -Dexpression=project.name -q -DforceStdout)
APP_VERSION := $(shell mvn -f $(BACKEND_POM_XML) help:evaluate -Dexpression=project.version -q -DforceStdout)
CONTAINER_NAME := $(APP_NAME)-container
SERVICE_DOMAIN := $(call get_env_var,'SERVICE.DOMAIN')
SERVICE_PORT := $(call get_env_var,'SERVICE.PORT')
TELEGRAM_BOT_TOKEN := $(call get_env_var,'TELEGRAM.BOT.TOKEN')

# scripts location and names below (WARNING: was written when located mater/mater-rest)
REMOVING_CONTAINER_SCRIPT := $(CI_CD_PATH_PREFIX)/scripts/remove-container.sh
REMOVING_IMAGE_SCRIPT := $(CI_CD_PATH_PREFIX)/scripts/remove-image.sh
ENVIRONMENT_VALIDATION_SCRIPT := $(CI_CD_PATH_PREFIX)/scripts/validate-environment.sh
BUILDING_BACKEND_SCRIPT := $(BACKEND_PATH_PREFIX)/scripts/build-backend.sh
BUILDING_FRONTEND_SCRIPT := $(FRONTEND_PATH_PREFIX)/scripts/build-frontend.sh
RUNNING_BACKEND_SCRIPT := $(BACKEND_PATH_PREFIX)/scripts/run-backend.sh
RUNNING_FRONTEND_SCRIPT := $(FRONTEND_PATH_PREFIX)/scripts/run-frontend.sh

# other vars below
START_TIME := $(shell date +%s) # in seconds

# -------------------------------------------------------------------------------------------------

log-vars:
	@echo "App name «$(APP_NAME)»."
	@echo "App version «$(APP_VERSION)»."
	@echo "Service domain «$(SERVICE_DOMAIN)»."
	@echo "Service port «$(SERVICE_PORT)»."
	@echo "Telegram bot token «$(TELEGRAM_BOT_TOKEN)»."
lv: log-vars # short alias

remove-backend-container:
	chmod +x $(REMOVING_CONTAINER_SCRIPT)
	$(REMOVING_CONTAINER_SCRIPT) $(CONTAINER_NAME)
rbc: remove-backend-container # short alias

remove-frontend-container:
	chmod +x $(REMOVING_CONTAINER_SCRIPT)
	$(REMOVING_CONTAINER_SCRIPT) mater-web-container
rfc: remove-frontend-container # short alias

remove-backend-image:
	chmod +x $(REMOVING_IMAGE_SCRIPT)
	$(REMOVING_IMAGE_SCRIPT) $(APP_NAME)
rbi: remove-backend-image # short alias

remove-frontend-image:
	chmod +x $(REMOVING_IMAGE_SCRIPT)
	$(REMOVING_IMAGE_SCRIPT) mater-web
rfi: remove-frontend-image # short alias

validate-env:
	chmod +x $(ENVIRONMENT_VALIDATION_SCRIPT)
	$(ENVIRONMENT_VALIDATION_SCRIPT) $(ENV_PATH_PREFIX) $(ENV_DEFAULT_VALUE)
ve: validate-env # short alias

build-backend: \
	remove-backend-image
	chmod +x $(BUILDING_BACKEND_SCRIPT)
	$(BUILDING_BACKEND_SCRIPT) $(APP_NAME) $(APP_VERSION)
bb: build-backend # short alias

build-frontend: \
	remove-frontend-image
	chmod +x $(BUILDING_FRONTEND_SCRIPT)
	$(BUILDING_FRONTEND_SCRIPT)
bf: build-frontend # short alias

build-all: \
	build-backend \
	build-frontend
ba: build-all # short alias

run-backend:
	chmod +x $(RUNNING_BACKEND_SCRIPT)
	$(RUNNING_BACKEND_SCRIPT) $(APP_NAME) $(SERVICE_PORT) $(TELEGRAM_BOT_TOKEN)
rb: run-backend # short alias

run-frontend:
	chmod +x $(RUNNING_FRONTEND_SCRIPT)
	$(RUNNING_FRONTEND_SCRIPT) mater
rf: run-frontend # short alias

prepare-continuous-delivery:
	@echo "Continuous Delivery started..."

process-maven:
	mvn -f $(BACKEND_POM_XML) clean install

# in seconds
calculate-duration:
	@START_TIME=$(START_TIME); \
	END_TIME=$$(date +%s); \
	TIME_DIFF=$$((END_TIME - START_TIME)); \
	echo "Process duration is $$TIME_DIFF seconds."

start-continuous-delivery-brute-force: \
	prepare-continuous-delivery \
	remove-backend-container \
	remove-frontend-container \
	remove-backend-image \
	remove-frontend-image \
	validate-env \
	process-maven \
	build-backend \
	build-frontend \
	run-backend \
	run-frontend \
	сalculate-duration
scd-br: start-continuous-delivery-brute-force # short alias

composing-up:
	$(DOCKER_COMPOSE_PREFIX_PLUGIN) up
composing-down:
	$(DOCKER_COMPOSE_PREFIX_PLUGIN) down

start-continuous-delivery: \
	prepare-continuous-delivery \
	composing-down \
	remove-backend-image \
	remove-frontend-image \
	remove-frontend-container \
	remove-backend-container \
	validate-env \
	process-maven \
	build-backend \
	build-frontend \
	composing-up \
	сalculate-duration
scd: start-continuous-delivery # short alias
