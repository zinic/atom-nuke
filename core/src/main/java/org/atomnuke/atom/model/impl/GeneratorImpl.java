package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Generator;

/**
 *
 * @author zinic
 */
public class GeneratorImpl extends SimpleContentImpl implements Generator {

   private String uri, version;

   public void setUri(String uri) {
      this.uri = uri;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   @Override
   public String uri() {
      return uri;
   }

   @Override
   public String version() {
      return version;
   }

   @Override
   public int hashCode() {
      int hash = 7;

      hash = 97 * hash + (this.uri != null ? this.uri.hashCode() : 0);
      hash = 97 * hash + (this.version != null ? this.version.hashCode() : 0);

      return hash + super.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final GeneratorImpl other = (GeneratorImpl) obj;

      if ((this.uri == null) ? (other.uri != null) : !this.uri.equals(other.uri)) {
         return false;
      }

      if ((this.version == null) ? (other.version != null) : !this.version.equals(other.version)) {
         return false;
      }

      return super.equals(obj);
   }
}
