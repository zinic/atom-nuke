package org.atomnuke.sink;

/**
 *
 * @author zinic
 */
public interface SinkResult {

    SinkAction action();

    String message();
}
