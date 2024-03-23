#!/bin/bash

while :
do
    response_code=$(curl -sL -w "%{http_code}" -o /dev/null "http://localhost:8083/connector-plugins")
    echo "Response code: $response_code"

    if [ "$response_code" -eq 200 ];
    then
        echo "Kafka connect plugins installed, proceeding with connectors."
        break
    else
        echo "Installing connect plugins..."
        sleep 10
    fi
done