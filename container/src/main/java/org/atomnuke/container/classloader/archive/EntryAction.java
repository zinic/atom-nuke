package org.atomnuke.container.classloader.archive;

public class EntryAction {

   public static final EntryAction SKIP = new EntryAction(ProcessingAction.SKIP, DeploymentAction.defaultAction());

   private final ProcessingAction processingAction;
   private final DeploymentAction packagingAction;

   public EntryAction(ProcessingAction processingAction, DeploymentAction packagingAction) {
      this.processingAction = processingAction;
      this.packagingAction = packagingAction;
   }

   public DeploymentAction deploymentAction() {
      return packagingAction;
   }

   public ProcessingAction processingAction() {
      return processingAction;
   }
}
