package org.atomnuke.atom;

/**
 *
 * @author zinic
 */
public class FeedMetadataImpl implements FeedMetadata {

   private String id, title;

   public FeedMetadataImpl() {
   }

   @Override
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   @Override
   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }
}
