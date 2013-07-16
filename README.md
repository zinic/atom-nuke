#[Nuke - The Fast ATOM Bus](http://atomnuke.org)#

###Deprecation Notice: Please note that this project has been deprecated###

**Current version**: 1.1.16
<br />
**Latest RPM**: [Nuke Fallout 1.1.16 RPM](http://maven.research.rackspacecloud.com/content/repositories/releases/org/atomnuke/packaging/nuke-fallout/1.1.16/nuke-fallout-1.1.16.rpm)

##What is it?##

Nuke is a collection of utilities built on a simple, fast ATOM implementation
that aims for a minimal dependency footprint.

###The ATOM Implementation###

The ATOM implementation within in Nuke has its own model that utilizes a SAX parser
and StAX writer for serialization and deserialization. The model interfaces are
immutable for simplicity and with the model itself emphasizing memory efficiency.

###Fallout###

Nuke also contains a runtime container called [Fallout]() to host packages that
contain the ATOM bus components.

Fallout is a polling bus based off of an multi-core event dispatch kernel. The
dispatch is backed by an generic execution pool. Fallout, by default, will attempt
to take advantage of all available processors.

The Nuke kernel supports polling granularity as grainular as the underlying JVM
implementation's timer.

For more information see the [Nuke kernel](https://github.com/zinic/atom-nuke/blob/master/core/src/main/java/org/atomnuke/NukeKernel.java)
and the [Nuke kernel run delegate](https://github.com/zinic/atom-nuke/blob/master/core/src/main/java/org/atomnuke/kernel/GenericKernelDelegate.java).


##How do I use it?##

###Maven Repository###

```xml
<repository>
	<id>releases.maven.research.rackspace.com</id>

	<name>Rackspace Research Releases</name>
	<url>http://maven.research.rackspacecloud.com/content/repositories/releases</url>
</repository>
```

###Installing and Configuring Fallout###

####Fallout Directories####

Fallout directories are configured via setting environment variables. See the
[Fallout run script](https://github.com/zinic/atom-nuke/blob/master/packaging/src/scripts/fallout.sh)
for more information.

<table>
	<thead>
    	<tr><th>Environment Variable Name</th><th>Usage</th><th>Default</th>
    </thead>
    <tbody>
    	<tr><td>NUKE_HOME</td><td>Nuke home directory. Only setting this utilizes the defaults for all other environment variables.</td><td>~/.nuke</td></tr>
        <tr><td>NUKE_LIB_DIR</td><td>Directory to load packages from.</td><td>${NUKE_HOME}/lib</td></tr>
        <tr><td>NUKE_CFG_DIR</td><td>Default directory for configurations without absolute paths.</td><td>${NUKE_HOME}/etc</td></tr>
        <tr><td>NUKE_DEPLOY_DIR</td><td>Directory to deploy packages to.</td><td>${NUKE_HOME}/deployed</td></tr>
    </tbody>
</table>

####Using the Fallout RPM####

The Fallout RPM provides the default set of services as a collection of package
along with the Fallout libraries necessarry.

The latest RPM may be downloaded at: [Nuke Fallout 1.1.15 RPM](http://maven.research.rackspacecloud.com/content/repositories/releases/org/atomnuke/packaging/nuke-fallout/1.1.15/nuke-fallout-1.1.15.rpm)


```bash
rpm -ivh nuke-fallout-1.1.15.rpm
```

###Interacting with the Fallout CLI###

Fallout is a persistent actor, source-sink bus, designed to be explicitly simple.

####Adding an Actor####

An actor is one of either interfaces, an [ATOM source](https://github.com/zinic/atom-nuke/blob/master/core/src/main/java/org/atomnuke/source/AtomSource.java)
or an [ATOM sink](https://github.com/zinic/atom-nuke/blob/master/core/src/main/java/org/atomnuke/sink/AtomSink.java).

Actors may be defined as one of three languages: javascript, java or python.

```bash
fallout actors add http-in-1 java org.atomnuke.http.HttpSource
fallout actors add id-echo java org.atomnuke.utilities.sinks.JavaIdEchoSink
```

The actor may have some simple parameters set on it as well.

```bash
fallout params http-in-1 set debug true
```

####Making an Actor a Polling Target####

An actor that implements the ATOMSource interface may be treated as a polling 
target. The polling interval may be configured anywhere from nanoseconds 
(JVM implementation dependent) to days.

```bash
# Poll the HTTP source every 10 milliseconds; a max of 100 events per second
fallout sources add http-in-1 10 milliseconds
```

####Binding a Source to a Sink Actor####

An actor that implements the ATOMSink interface may be bound to any number of
configured sources. Once bound, the sink actor will receive events emitted by
the source actors.

```bash
# Bind the ID echo sink to the HTTP source to emit the ID of any events published to the HTTP endpoint
fallout bindings add http-in-1 id-echo
```

####Starting Fallout####

Once the configuration has been completed, you can start Fallout!

```bash
fallout server start
```

###Other Fallout CLI Commands###

####Listing Configuration Elements####

```bash
fallout actors
fallout sources
fallout params <actor-id>
fallout bindings
```

####Removing Configuration Elements####

```bash
fallout actors rm <actor-id>
fallout sources rm <actor-id>
fallout params <actor-id> rm <param-name>
fallout bindings rm <binding-id>
```

###As a Feed Crawler###

By default Nuke comes with an ATOM Source that is useful for crawling feeds. The
crawler is designed specifically to work with [AtomHopper](http://atomhopper.org/).

For more information, please see the [nuke feed crawler source README](https://github.com/zinic/atom-nuke/blob/master/components/sources/feed-crawler).

###Other Java Code Exmaples###

##Model Features Missing##

* Extensible elements - Internal elements are currently treated as text content.

##Release Notes##

* [Release Notes](https://github.com/zinic/atom-nuke/wiki/Release-Notes) can be found in the wiki.

##That Legal Thing...##

This software library is released to you under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). See [LICENSE](https://github.com/zinic/atom-nuke/blob/master/LICENSE) for more information.
