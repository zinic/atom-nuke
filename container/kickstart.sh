#!/bin/sh

if [ ! -e ./target/nuke-container.jar ]; then
   mvn -q clean install >> /dev/null 2>&1;

   if [ $? != 0 ]; then
      echo "Building project failed... please run 'mvn -e clean install' for more info.";
      exit $?;
   fi
fi

java -jar ./target/nuke-container.jar $@
