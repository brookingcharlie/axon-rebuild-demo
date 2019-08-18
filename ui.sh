#!/bin/bash

while true; do
    echo
    echo "Select a command: "
    echo "1) Load data"
    echo "2) Query all accounts"
    echo "3) Make deposit"
    echo "4) Shut down tracker"
    echo "5) Start tracker"
    echo "6) Rebuild tracker"
    echo "7) Check tracking status"
    echo "q) Quit"
    read -n1 input
    echo
    case "${input}" in
        1) curl -sS -X POST 'http://localhost:8080/load' ;;
        2) curl -sS -X GET 'http://localhost:8080/account' | jq -r . ;;
        3)
            accountNumber=$(curl -sS -X GET 'http://localhost:8080/account' | jq -r '.[0].accountNumber')
            curl -sS -X POST "http://localhost:8080/account/${accountNumber}/deposit"
            ;;
        4) curl -sS -X POST 'http://localhost:8080/tracker/shut-down' ;;
        5) curl -sS -X POST 'http://localhost:8080/tracker/start' ;;
        6) curl -sS -X POST 'http://localhost:8080/tracker/rebuild' ;;
        7) curl -sS -X GET 'http://localhost:8080/tracker/status' | jq . ;;
        q) exit ;;
    esac
done

