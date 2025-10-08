#!/bin/bash

ENV_FILE=".env"

if [ -f "$ENV_FILE" ]; then
    echo "Loading environment variables from $ENV_FILE..."
    source "$ENV_FILE"
    echo "Variables loaded."
else
    echo "File not found. Tests will proceed without the env"
fi

echo "Starting Gradle tests..."

exit $?