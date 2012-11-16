function JsIdEchoEventlet() {
   var taskContext;
}

JsIdEchoEventlet.prototype.init = function (ctx) {
   taskContext = ctx;

   taskContext.log().info("JS entry ID echo eventlet initialized.");
};

JsIdEchoEventlet.prototype.destroy = function () {
   taskContext.log().info("JS entry ID echo eventlet destroyed.");
};

JsIdEchoEventlet.prototype.entry = function (entry) {
    taskContext.log().info("From JavaScript: " + entry.id());
};

var JsEchoEventletFactory = function () {
   return new JsIdEchoEventlet();
};