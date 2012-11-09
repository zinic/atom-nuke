package org.atomnuke.lifecycle.resolution;

/**
 *
 * @author zinic
 */
public class ResolutionActionImpl implements ResolutionAction {

   private final ResolutionActionType requestedAction;

   public ResolutionActionImpl(ResolutionActionType requestedAction) {
      this.requestedAction = requestedAction;
   }

   @Override
   public ResolutionActionType type() {
      return requestedAction;
   }
}
