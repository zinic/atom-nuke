package org.atomnuke.util.config.io.file;

import java.io.File;
import org.atomnuke.util.config.io.UpdateTag;

/**
 *
 * @author zinic
 */
public class FileUpdateTag implements UpdateTag {

   private final long fileLength, lastModifySeconds;

   public FileUpdateTag(File file) {
      this(file.length(), file.lastModified() / 1000);
   }

   public FileUpdateTag(long fileLength, long lastModifySeconds) {
      this.fileLength = fileLength;
      this.lastModifySeconds = lastModifySeconds;
   }

   @Override
   public long tagValue() {
      return fileLength + lastModifySeconds;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 37 * hash + (int) (this.fileLength ^ (this.fileLength >>> 32));
      hash = 37 * hash + (int) (this.lastModifySeconds ^ (this.lastModifySeconds >>> 32));
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final FileUpdateTag other = (FileUpdateTag) obj;
      final long comparedValue = tagValue() - other.tagValue();

      return comparedValue == 0;
   }

   @Override
   public String toString() {
      return String.valueOf(tagValue());
   }
}
