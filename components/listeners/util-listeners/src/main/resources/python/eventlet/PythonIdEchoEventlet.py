from org.atomnuke.listener.eps.eventlet import AtomEventlet

# Python <3
class PythonIdEchoEventlet(AtomEventlet):
   def init(self, taskContext):
      # Save a reference to the logger
      self.log = taskContext.log()

      # Hello world!
      self.log.info("Python ID echo eventlet init.")

   def destroy(self):
      self.log.info("Python ID echo eventlet destroyed.")

   def entry(self, entry):
      self.log.info("From Python: %s" % (entry.id().toString()))

      for category in entry.categories():
         print("Entry category: %s" % (category.term()))
