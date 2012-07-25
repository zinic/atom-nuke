#Nuke#
###The Fast, Simple ATOM Implementation###

##What is it?##

Nuke is a collection of utilities built on a simple, fast ATOM implementation.

The ATOM implementation has its own model and utilizes a SAX parser and a StAX
writer. The implementation was designed for immutibility, maximum simplicity 
and memory efficiency.

Nuke also contains a smart ATOM feed crawler implementation that's built ontop of
the ATOM implementation. The ATOM feed crawler is designed specifically to work 
with [AtomHopper](http://atomhopper.org/).

Lastly, the project aspires to maintain a minimal dependency footprint.

##How do I use it?##

###Java Code Exmaples###

[Using the Crawler](https://github.com/zinic/atom-nuke/blob/master/src/main/java/net/jps/nuke/HDFSMain.java)
[HDFS Example Listener](https://github.com/zinic/atom-nuke/blob/master/src/main/java/net/jps/nuke/listener/hadoop/HDFSFeedListener.java)

##Features Missing##

* Extensible elements - Internal elements are currently treated as text content.

##That Legal Thing...##

This software library is realease to you under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). See [LICENSE](https://github.com/zinic/atom-nuke/blob/master/LICENSE) for more information.
