@echo off

if "%1"=="" (
    docker-compose --profile dev down
    docker-compose --profile dev up -d
) else (
    docker-compose down %1
    docker-compose up -d %1
)