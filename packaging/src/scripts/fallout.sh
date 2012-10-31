#!/bin/sh

# Nuke env variables
export NUKE_HOME="/var/lib/atomnuke";
export NUKE_DEPLOY="${NUKE_HOME}/deployed";
export NUKE_LIB="${NUKE_HOME}/deployable";
export NUKE_CONFIG="/etc/atomnuke/nuke.cfg.xml";

# Location of the jar distribution
JAR_LOCATION="/usr/lib/atomnuke/fallout-full.jar";

if [ -z "${DEBUG}" ]; then
    java -jar "${JAR_LOCATION}" $@
else
    java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar "${JAR_LOCATION}" $@
fi
