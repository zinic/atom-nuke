#Nuke#
###The Fast ATOM Framework###

##What is it?##

Nuke is a collection of utilities built on a simple, fast ATOM implementation
that aims for a minimal dependency footprint.

The ATOM implementation has its own model and utilizes a SAX parser and a StAX
writer. The implementation was designed for immutability, maximum simplicity 
and memory efficiency.

Nuke also contains a polling event framework that can poll multiple sources. Each
source may be registered will a configured polling interval that will govern how
often the source is polled during normal operation.

Task polling uses the configured interval in two cases:
* If a feed page encountered has zero entries in it.
* When reading individual ATOM entries.

Each source registered in Nuke may have any number of [ATOM listeners](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/listener/AtomListener.java)
added to its dispatch list. These listeners will begin receiving events on the
next scheduled poll.

##How do I use it?##

###As an Event Bus###

Nuke contains a high performance event dispatch kernel that's backed by an
execution pool. The Nuke kernel supports nanosecond polling granularity.

* [Event generator example](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/EventGeneratorMain.java)

For more information see the [Nuke kernel](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/NukeKernel.java)
and the [Nuke kernel run delegate](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/KernelDelegate.java).

###Feed Crawler###

By default Nuke comes with an [ATOM source](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/source/AtomSource.java)
that is useful for crawling feeds. The crawler is designed specifically to work 
with [AtomHopper](http://atomhopper.org/).

* [Using the Feed Crawler](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/HDFSMain.java)

###Using ATOM Event Selection###

Nuke contains a framework for turning ATOM feeds and entries into selectable
events. This framework is called the [event processing system or EPS](https://github.com/zinic/atom-nuke/tree/master/src/main/java/org/atomnuke/listener/eps) for short.

* [Using an EPS Relay](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/EPSMain.java)
* [The AtomEventHandler interface](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/listener/eps/handler/AtomEventHandler.java)
* [The Selector interface](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/listener/eps/selector/Selector.java)

###Other Java Code Exmaples###

* [HDFS Example Listener](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/listener/HDFSFeedListener.java)

##Features Missing##

* Extensible elements - Internal elements are currently treated as text content.

##That Legal Thing...##

This software library is realease to you under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). See [LICENSE](https://github.com/zinic/atom-nuke/blob/master/LICENSE) for more information.
