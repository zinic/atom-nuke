package net.jps.nuke.listener;

/**
 * This is a marker interface that allows the Nuke kernel insight into whether
 * or not the AtomListener described is thread-safe in such a way that it may
 * be scheduled again, even if the previous scheduled run has not yet completed.
 * 
 * Implementing this strategy may provide better utilization of idle resources. 
 * 
 * @author zinic
 */
public interface ReentrantAtomListener extends AtomListener {
}
