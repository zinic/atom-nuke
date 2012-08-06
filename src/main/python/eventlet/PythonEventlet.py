from org.atomnuke.listener.eps.eventlet import AtomEventlet


#class AtomEventlet:
#   def __init__(self):
#      print("init")

# Python <3
class PythonEventlet(AtomEventlet):
   def init(self, taskContext):
      print("Python eventlet start!")

   def destroy(self, taskContext):
      print("Python eventlet stop!")

   def entry(self, entry):
      print("Entry")

