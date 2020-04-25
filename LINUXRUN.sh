#!/bin/bash

clear
echo "Booting Game..."
cd .\pool-game\target\classes\
java com.killian.App
cd ..\..\..
sleep 1
echo "Should be running!"