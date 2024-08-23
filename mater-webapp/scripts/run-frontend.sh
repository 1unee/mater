#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <app-name>"
    exit 1
fi

# Присваиваем аргументы переменным
APP_NAME=$1-web
CONTAINER_NAME=$APP_NAME-container

docker run \
		--workdir=/app \
		-p "80:4200" \
		--name $CONTAINER_NAME \
		 $APP_NAME
