#ATOM Feed Crawler#

####What is it?####

The ATOM Feed Crawler is an AtomNuke source for crawling ATOM feeds. Each entry in the feed will be treated as an event and emitted to bound sinks.

####Setting up a Feed Crawler####

```bash
# Add actors
fallout actors add id-echo java org.atomnuke.utilities.sinks.JavaIdEchoSink
fallout actors add crawler-1 java org.atomnuke.source.crawler.FeedCrawlerSource
fallout actors add crawler-2 java org.atomnuke.source.crawler.FeedCrawlerSource

# Add the actors as our sources
fallout sources add crawler-1 15 SECONDS
fallout sources add crawler-2 15 SECONDS

# Bind the source to a target
fallout bindings add crawler-1 id-echo
fallout bindings add crawler-2 id-echo

# Start fallout
fallout server start
```

####Configuration####

The feed crawler reads its configuration from a configuration file, "feed-crawler-targets.cfg.xml" located at the Fallout configuration directory.

An [example configuration](https://github.com/zinic/atom-nuke/blob/master/components/sources/feed-crawler/src/main/resources/META-INF/schema/examples/feed-crawler-targets.cfg.xml) is available to demonstrate usage.
