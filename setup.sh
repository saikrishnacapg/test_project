#!/bin/bash

DIR=$HOME/gradle-6.4
cd $HOME 
if [ ! -d "$DIR" ]; then
  echo "Installing gradle files in ${DIR}..."
  curl -k https://downloads.gradle-dn.com/distributions/gradle-6.4-bin.zip -o gradle.zip
  unzip gradle.zip
fi

if ! grep -q "GRADLE_HOME" ~/.bashrc; then
   export PATH=$HOME/gradle-6.4/bin:$PATH
   export GRADLE_HOME=$HOME/gradle-6.4 
   . ~/.bashrc
fi
cd -
echo $PATH
pwd

rm -rf build
$HOME/gradle-6.4/bin/gradle build

docker-compose up
