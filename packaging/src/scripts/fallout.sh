#!/bin/sh

# Nuke env variables
NUKE_HOME="/var/lib/atomnuke";
NUKE_DEPLOY="${NUKE_HOME}/deployed";
NUKE_LIB="${NUKE_HOME}/deployable";
CONFIG_NAME="/etc/atomnuke/nuke.cfg.xml";

# Location of the jar distribution
JAR_LOCATION="/usr/lib/atomnuke/fallout-full.jar";

if [ -z "${DEBUG}" ]; then
    java -jar "${JAR_LOCATION}" $@
else
    java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar "${JAR_LOCATION}" $@
fi
