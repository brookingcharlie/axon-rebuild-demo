#!/bin/bash

while true; do
    echo
    echo "Select a command: "
    echo "1) Load data"
    echo "2) Query all accounts"
    echo "3) Make deposit"
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
        q) exit ;;
    esac
done

