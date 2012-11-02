package org.atomnuke.control.api.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * {
 * "categories" : [ { "scheme" : "ah-cluster-n1.metrics", "term" : "req_per_min"
 * } ], "id" : "80e99533-73ee-4576-940d-bdf8091456cd", "callback" :
 * "http://endpoint.domain/path" }
 *
 * @author zinic
 */
public class SubscriptionDocument {

   private Set<SubscriptionCategory> categories;
   private SubscriptionContent content;
   private String callback, id;

   public SubscriptionDocument() {
      categories = new HashSet<SubscriptionCategory>();
   }

   public boolean hasCategory(SubscriptionCategory searchCategory) {
      for (SubscriptionCategory myCategory : categories) {
         if (myCategory.equals(searchCategory)) {
            return true;
         }
      }

      return false;
   }

   public Collection<SubscriptionCategory> getCategories() {
      return categories;
   }

   public void addCategory(SubscriptionCategory category) {
      categories.add(category);
   }

   public void setCategories(Set<SubscriptionCategory> categories) {
      this.categories = categories;
   }

   public SubscriptionContent getContent() {
      return content;
   }

   public void setContent(SubscriptionContent content) {
      this.content = content;
   }

   public String getCallback() {
      return callback;
   }

   public void setCallback(String callback) {
      this.callback = callback;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   @Override
   public int hashCode() {
      int hash = 5;

      hash = 59 * hash + (this.categories != null ? this.categories.hashCode() : 0);
      hash = 59 * hash + (this.callback != null ? this.callback.hashCode() : 0);

      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final SubscriptionDocument other = (SubscriptionDocument) obj;

      if ((this.callback == null) ? (other.callback != null) : !this.callback.equals(other.callback)) {
         return false;
      }

      if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
         return false;
      }

      return true;
   }
}
