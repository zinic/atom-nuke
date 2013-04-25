#!/bin/sh

# Nuke env variables
export NUKE_HOME="/var/lib/atomnuke";
export NUKE_DEPLOY_DIR="${NUKE_HOME}/deployed";
export NUKE_LIB_DIR="${NUKE_HOME}/deployable";
export NUKE_CONFIG_DIR="/etc/atomnuke";

if [ -z "${JAVA_OPTS}" ]; then
   JAVA_OPTS="-Xms2G -Xmx4G -XX:MaxPermSize=1G"
fi

# Location of the jar distribution
JAR_LOCATION="/usr/lib/atomnuke/fallout-full.jar";

if [ -z "${JPDA}" ]; then
    java ${JAVA_OPTS} -jar "${JAR_LOCATION}" $@;
else
    java ${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar "${JAR_LOCATION}" $@;
fi
