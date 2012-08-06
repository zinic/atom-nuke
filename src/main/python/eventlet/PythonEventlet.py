from org.atomnuke.listener.eps.eventlet import AtomEventlet
from org.atomnuke.atom.model import Entry
from org.atomnuke.atom.model import Category

# Python <3
class PythonEventlet(AtomEventlet):
   def init(self, taskContext):
      print("Python eventlet start!")

   def destroy(self, taskContext):
      print("Python eventlet stop!")

   def entry(self, entry):
      print("Entry - ID: %s" % (entry.id().toString()))

      for category in entry.categories():
         print("Entry category: %s" % (category.term()))

