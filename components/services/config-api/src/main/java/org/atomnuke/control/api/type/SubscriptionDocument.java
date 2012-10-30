package org.atomnuke.control.api.type;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * {
 *    "categories" : [
 *       { "scheme" : "ah-cluster-n1.metrics", "term" : "req_per_min" }
 *    ],
 *    "id" : "80e99533-73ee-4576-940d-bdf8091456cd"
 * }
 *
 * @author zinic
 */
public class SubscriptionDocument {

   private List<Category> categories;
   private String id;

   public SubscriptionDocument() {
      categories = new LinkedList<Category>();
   }

   public List<Category> getCategories() {
      return categories;
   }

   public void setCategories(List<Category> categories) {
      this.categories = categories;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }
}
