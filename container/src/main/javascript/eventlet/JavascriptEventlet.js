// Because I'm lazy
importPackage(java.lang);
importPackage(org.atomnuke.listener.eps.eventlet);

var eventlet = function() {
   return new org.atomnuke.listener.eps.eventlet.AtomEventlet() {
      init : function (taskContext) {
      },

      destroy : function () {
      },

      entry : function (entry) {
         if (entry) {
            System.out.println(entry.id());
         }
      }
   }
};