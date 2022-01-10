#!/bin/bash

# run wiremock mock server with json files
echo -e "\n - Running Wiremock server"
docker run --rm \
    -p 5050:8080 \
    --name wiremock \
    wiremock/wiremock:2.32.0 \
    --enable-stub-cors &

# wait until wiremock is up
end=$((SECONDS+600))
until [ "$( docker container inspect -f '{{.State.Status}}' wiremock )" == "running" ]; do
    sleep 0.5;
    echo -e "Not up, waiting . . .\n"
    if [ $SECONDS -gt $end ]; then
        echo "Timeout for docker"
        break
    fi
done;
