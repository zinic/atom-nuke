package org.atomnuke.source.action;

/**
 *
 * @author zinic
 */
public class AtomSourceActionImpl<T> implements AtomSourceAction<T> {

   private final ActionType actionType;
   private final T value;

   public AtomSourceActionImpl(ActionType actionType) {
      this(actionType, null);
   }

   public AtomSourceActionImpl(ActionType actionType, T value) {
      this.actionType = actionType;
      this.value = value;
   }

   @Override
   public boolean hasValue() {
      return value != null;
   }

   @Override
   public T value() {
      return value;
   }

   @Override
   public ActionType action() {
      return actionType;
   }
}
