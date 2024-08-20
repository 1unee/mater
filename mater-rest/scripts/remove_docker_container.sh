#!/bin/bash

# Проверка, что имя контейнера передано в качестве аргумента
if [ -z "$1" ]; then
    echo "Usage: $0 <container_name>"
    exit 1
fi

# Имя контейнера
container_name="$1"

# Проверка существования контейнера
if [ "$(docker ps -a -q -f name=$container_name)" ]; then
    # Удаление контейнера
    docker rm "$container_name"
    echo "Container '$container_name' removed."
else
    echo "Container '$container_name' does not exist."
fi
