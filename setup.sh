#!/bin/bash

cd $HOME 
curl -k https://downloads.gradle-dn.com/distributions/gradle-6.4-bin.zip -o gradle.zip
unzip gradle.zip

export PATH=$PATH:$HOME/gradle-6.4/bin
export GRADLE_HOME=$HOME/gradle-6.4

rm -rf build
gradle build

docker-compose up
