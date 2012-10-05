package org.atomnuke.container.classloader.archive;

public enum ProcessingAction {

   PROCESS_AS_CLASS,
   PROCESS_AS_RESOURCE,
   DESCEND_INTO_JAR_FORMAT_ARCHIVE,
   SKIP
}
