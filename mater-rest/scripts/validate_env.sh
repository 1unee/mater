#!/bin/bash

# two args taken
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <env-path-prefix> <default-value>"
    exit 1
fi

ENV_PATH_PREFIX=$1
ENV_FILE="$ENV_PATH_PREFIX/.env.yml"
P12_FILE="$ENV_PATH_PREFIX/copied.oneune.duckdns.org.p12"
UNWANTED_VALUE=$2

# checking existing yq
if ! command -v yq &> /dev/null; then
    echo "yq not installed!"
    exit 1
fi

# checking existing *.env.yml
if [ ! -f "$ENV_FILE" ]; then
    echo "File $ENV_FILE not found!"
    exit 1
fi

# checking existing *.p12
p12_exists=$(ls $P12_FILE 2>/dev/null)
if [ -z "$p12_exists" ]; then
    echo "File $P12_FILE not found!"
    exit 1
fi

# validation *.env.yml
result=$(yq e "select(.[] == \"$UNWANTED_VALUE\") | keys" $ENV_FILE)

if [ -n "$result" ]; then
    echo "Found vars with values \"$UNWANTED_VALUE\":"
    echo "$result"
else
    echo "Validation passed success!"
fi
