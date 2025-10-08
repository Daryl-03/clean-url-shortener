#!/bin/bash

ENV_FILE=".env"

if [ -f "$ENV_FILE" ]; then
    echo "Chargement des variables d'environnement depuis $ENV_FILE..."
    source "$ENV_FILE"
    echo "Variables chargées."
else
    echo "File not found. Tests will proceed without the env"
fi

echo "Lancement des tests Gradle..."

exit $?