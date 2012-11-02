package org.atomnuke.control.api.type;

/**
 *
 * @author zinic
 */
public class SubscriptionCategory {

   private String scheme, term;

   public String getScheme() {
      return scheme;
   }

   public void setScheme(String scheme) {
      this.scheme = scheme;
   }

   public String getTerm() {
      return term;
   }

   public void setTerm(String term) {
      this.term = term;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 37 * hash + (this.scheme != null ? this.scheme.hashCode() : 0);
      hash = 37 * hash + (this.term != null ? this.term.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final SubscriptionCategory other = (SubscriptionCategory) obj;

      if ((this.scheme == null) ? (other.scheme != null) : !this.scheme.equals(other.scheme)) {
         return false;
      }

      if ((this.term == null) ? (other.term != null) : !this.term.equals(other.term)) {
         return false;
      }

      return true;
   }
}
