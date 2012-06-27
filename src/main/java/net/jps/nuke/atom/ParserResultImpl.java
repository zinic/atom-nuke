package net.jps.nuke.atom;

import java.net.URI;

/**
 *
 * @author zinic
 */
public class ParserResultImpl implements ParserResult {

   private FeedMetadata feedMetadata;
   private URI nextLocation;

   public ParserResultImpl() {
   }

   @Override
   public FeedMetadata getFeedMetadata() {
      return feedMetadata;
   }

   public void setFeedMetadata(FeedMetadata feedMetadata) {
      this.feedMetadata = feedMetadata;
   }

   @Override
   public URI getNextLocation() {
      return nextLocation;
   }

   public void setNextLocation(URI nextLocation) {
      this.nextLocation = nextLocation;
   }
}
