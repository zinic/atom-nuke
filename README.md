#Nuke#
###The Fast ATOM Framework###

Our Website: [Atom Nuke](http://atomnuke.org)

##What is it?##

Nuke is a collection of utilities built on a simple, fast ATOM implementation
that aims for a minimal dependency footprint.

The ATOM implementation has its own model and utilizes a SAX parser and a StAX
writer. The implementation was designed for immutability, maximum simplicity 
and memory efficiency.

Nuke also contains a polling event framework that can poll multiple sources. Each
source may be registered with a configured polling interval that will govern how
often the source is polled during normal operation.

Each source registered in Nuke may have any number of [ATOM listeners](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/listener/AtomListener.java)
added to its dispatch list. These listeners will begin receiving events on the
next scheduled poll.

##How do I use it?##

###Blog Tutorials###

* [Building a simple ATOM crawler with Atom Nuke, Netbeans 7.2 and Java](http://www.giantflyingsaucer.com/blog/?p=4078)
* [Create an ATOM feed with Atom Nuke, NetBeans 7.2 and Java](http://www.giantflyingsaucer.com/blog/?p=4113)
* [Finding specific ATOM categories using Atom Nuke and Java](http://www.giantflyingsaucer.com/blog/?p=4126)

###The Nuke Event Bus###

Nuke contains a high performance event dispatch kernel that's backed by an
execution pool. The Nuke kernel supports nanosecond polling granularity.

* [Event generator example](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/EventGeneratorMain.java)

For more information see the [Nuke kernel](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/NukeKernel.java)
and the [Nuke kernel run delegate](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/KernelDelegate.java).

###As a Feed Crawler###

By default Nuke comes with an [ATOM source](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/source/AtomSource.java)
that is useful for crawling feeds. The crawler is designed specifically to work 
with [AtomHopper](http://atomhopper.org/).

* [Using the Feed Crawler](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/HDFSMain.java)

###ATOM Entry Event Selection###

Nuke contains a framework for turning ATOM feeds and entries into selectable
events. This framework is called the [event processing system or EPS](https://github.com/zinic/atom-nuke/tree/master/src/main/java/org/atomnuke/listener/eps) for short.

* [Using an EPS Relay](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/EPSMain.java)
* [The Eventlet interface](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/listener/eps/eventlet/AtomEventlet.java)
* [The Selector interface](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/listener/eps/selector/Selector.java)

###Other Java Code Exmaples###

* [HDFS Example Listener](https://github.com/zinic/atom-nuke/blob/master/src/main/java/org/atomnuke/examples/listener/HDFSFeedListener.java)

##Features Missing##

* Extensible elements - Internal elements are currently treated as text content.

##That Legal Thing...##

This software library is released to you under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). See [LICENSE](https://github.com/zinic/atom-nuke/blob/master/LICENSE) for more information.
