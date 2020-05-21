#!/bin/bash

clear
echo "Compiling..."
javac ./pool-game/src/main/java/com/killian/*.java -d ./target/
sleep 2
echo "Compiled!" 