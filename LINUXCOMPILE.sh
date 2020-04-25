#!/bin/bash

clear
echo "Compiling..."
javac -cp .\pool-game\src\main\java\com\killian\*.java -d .\pool-game\target\
sleep 2
echo "Compiled!"