from org.atomnuke.listener.eps.eventlet import AtomEventlet
from org.atomnuke.atom.model import *

# Python <3
class PythonEventlet(AtomEventlet):
   def init(self, taskContext):
      print("Python eventlet start!")

   def destroy(self, taskContext):
      print("Python eventlet stop!")

   def entry(self, entry):
      print("Entry - ID: %s" % (entry.id().value))

