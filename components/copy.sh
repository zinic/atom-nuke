#!/bin/sh
NUKE_HOME="${HOME}/.nuke"
NUKE_LIB="${NUKE_HOME}/lib"
NUKE_DEPLOYED="${NUKE_HOME}/deployed"

rm -vf ${NUKE_LIB}/*bundle*.jar
rm -rf ${NUKE_DEPLOYED}/*

cp -vf sinks/target/sink-bundle-*.jar ${NUKE_LIB}/sink-bundle.jar
cp -vf sources/target/source-bundle*.jar ${NUKE_LIB}/source-bundle.jar
cp -vf services/target/service-bundle*.jar ${NUKE_LIB}/service-bundle.jar
