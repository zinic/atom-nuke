package org.atomnuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public class FatalException extends RuntimeException {
    
    public FatalException(Throwable cause) {
        this(cause.getMessage(), cause);
    }
    
    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }
}
