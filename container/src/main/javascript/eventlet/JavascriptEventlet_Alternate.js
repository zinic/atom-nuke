// Because I'm lazy
importPackage(java.lang);

var eventlet = function() {
   return {
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
