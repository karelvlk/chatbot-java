#!/bin/bash

# Check for the first argument to determine the environment
if [ "$1" = "--dev" ]; then
    echo "Running in development mode 🛠️"

    # Build the Docker image for development if it hasn't been built yet
    if [[ "$(docker images -q chatbot-java-image.dev 2> /dev/null)" == "" ]]; then
        echo "Building development Docker image..."
        docker build -t chatbot-java-image.dev -f Dockerfile.dev .
    fi

    # Run the Docker container in development mode
    echo "Starting development container..."
    docker run -v $(pwd):/app -it chatbot-java-image.dev

elif [ "$1" = "--prod" ]; then
    echo "Running in production mode 🚀"

    # Build the Docker image for production if it hasn't been built yet
    if [[ "$(docker images -q chatbot-java-image 2> /dev/null)" == "" ]]; then
        echo "Building production Docker image..."
        docker build -t chatbot-java-image -f Dockerfile .
    fi

    # Run the Docker container in production mode
    echo "Starting production container..."
    docker run chatbot-java-image

else
    echo "Invalid argument. Please use --dev or --prod."
fi
