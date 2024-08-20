get_env_var = $(shell yq eval '.${1}' .env.yml)

APP_NAME := $(shell mvn help:evaluate -Dexpression=project.name -q -DforceStdout)
APP_VERSION := $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
CONTAINER_NAME := $(APP_NAME)-container
SERVICE_PORT := $(call get_env_var,'SERVICE.PORT')
TELEGRAM_BOT_TOKEN := $(call get_env_var,'TELEGRAM.BOT.TOKEN')

# ---------------------------------------------------------------

log-vars:
	@echo "App name: $(APP_NAME)."
	@echo "App version: $(APP_VERSION)."
	@echo "Service port: $(SERVICE_PORT)."
	@echo "Telegram bot token: $(TELEGRAM_BOT_TOKEN)."
build:
	chmod +x ./scripts/docker_build.sh
	./scripts/docker_build.sh $(APP_NAME) $(APP_VERSION)
run:
	chmod +x ./scripts/docker_run.sh
	./scripts/docker_run.sh $(APP_NAME) $(SERVICE_PORT) $(TELEGRAM_BOT_TOKEN)
workflow:
	@echo 'Workflow started...'
	chmod +x ./scripts/remove_docker_container.sh
	./scripts/remove_docker_container.sh $(CONTAINER_NAME)
	chmod +x ./scripts/remove_docker_image.sh
	./scripts/remove_docker_image.sh $(APP_NAME)
	mvn clean install
	chmod +x ./scripts/docker_build.sh
	./scripts/docker_build.sh $(APP_NAME) $(APP_VERSION)
	chmod +x ./scripts/docker_run.sh
	./scripts/docker_run.sh $(APP_NAME) $(SERVICE_PORT) $(TELEGRAM_BOT_TOKEN)
start:
	docker start -i $(CONTAINER_NAME)