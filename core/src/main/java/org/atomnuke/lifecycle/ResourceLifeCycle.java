package org.atomnuke.lifecycle;

/**
 * The resource life-cycle defines two methods that will be called once during
 * the lifetime of the object bound to the life-cycle.
 *
 * The init method will always be called before the destroy method and ideally
 * will be called when the object is being prepared for use. The init method
 * will never be called after the destroy method.
 *
 * The destroy method may be called at any time and may be called before the
 * init method. Once this method is called, it is assumed that the object will
 * no longer be in use.
 *
 * @author zinic
 */
public interface ResourceLifeCycle<T> extends Reclaimable {

   /**
    * Initializes this task.
    *
    * @param contextObject
    * @throws InitializationException
    */
   void init(T contextObject) throws InitializationException;
}
