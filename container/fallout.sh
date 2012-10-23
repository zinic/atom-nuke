#!/bin/sh

JAR_LOCATION=./dist/target/fallout-full.jar

if [ ! -e "${JAR_LOCATION}" ]; then
   pushd .. >> /dev/null 2>&1;

   echo "Unable to locate cli jar, attempting to build project. Please wait...";

   mvn -q clean install >> /dev/null 2>&1;

   if [ $? != 0 ]; then
      echo "Building project failed... please run 'mvn -e clean install' for more info.";
      exit $?;
   fi

   popd >> /dev/null 2>&1;
fi


if [ -z "${DEBUG}" ]; then
    java -jar "${JAR_LOCATION}" $@
else
    java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar "${JAR_LOCATION}" $@
fi
