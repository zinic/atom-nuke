#!/bin/sh
NUKE_HOME="${HOME}/.nuke"
NUKE_LIB_DIR="${NUKE_HOME}/lib"
NUKE_DEPLOYED_DIR="${NUKE_HOME}/deployed"
NUKE_CONFIG_DIR="${NUKE_HOME}/etc"

rm -vf ${NUKE_LIB_DIR}/*bundle*.jar
rm -rf ${NUKE_DEPLOYED_DIR}/*

cp -vf sinks/target/sink-bundle-*.jar ${NUKE_LIB_DIR}/sink-bundle.jar
cp -vf sources/target/source-bundle*.jar ${NUKE_LIB_DIR}/source-bundle.jar
cp -vf services/target/service-bundle*.jar ${NUKE_LIB_DIR}/service-bundle.jar
