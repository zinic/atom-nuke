package net.jps.nuke.bindings;

/**
 *
 * @author zinic
 */
public class LoadResult {

    private final Throwable attachedException;
    private final boolean error;

    public LoadResult() {
        this.attachedException = null;
        this.error = false;
    }

    public LoadResult(Throwable attachedException) {
        this.attachedException = attachedException;
        this.error = true;
    }

    public Throwable getAttachedException() {
        return attachedException;
    }

    public boolean isError() {
        return error;
    }
}
