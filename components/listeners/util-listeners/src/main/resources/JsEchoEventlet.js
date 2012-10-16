importPackage(java.lang);
importPackage(org.atomnuke.listener.eps.eventlet);

var eventlet = function() {
   return new org.atomnuke.listener.eps.eventlet.AtomEventlet() {
      init : function (taskContext) {
            System.out.println("Javascript entry ID echo eventlet initialized.");
      },

      destroy : function () {
            System.out.println("Javascript entry ID echo eventlet destroyed.");
      },

      entry : function (entry) {
         if (entry) {
            System.out.println(entry.id());
         }
      }
   }
};