The XML Configuration

nuke.cfg.xml:
```xml
   <?xml version="1.0" encoding="UTF-8" standalone="yes"?>

   <nuke xmlns="http://atomnuke.org/configuration">
     <sinks>
       <sink id="pubsub-sink" type="java" href="org.atomnuke.pubsub.sink.PubSubSink"/>
     </sinks>
     <sources>
       <source id="http-source-1" type="java" href="org.atomnuke.http.HttpSource">
         <polling-interval value="10" unit="MILLISECONDS"/>
       </source>
     </sources>
     <eps>
       <relays/>
       <eventlets/>
     </eps>
     <bindings>
       <binding id="44f81044-b49b-47da-a1bd-026f75ee0348" sink="pubsub-sink" source="http-source-1"/>
     </bindings>
   </nuke>
```

Utilizing the above configuration will allow you to perform the following to validate that your system is working. To publish a subscription, edit the subscription.json file provided in this directory.

Once Fallout is started and the configuration model has been deployed, you can initiate a subscription by utilizing the following command:

    curl -X PUT --upload-file ./subscription.json -H 'Content-Type: application/json' "http://localhost:8080/pubsub/subscriptions" -v

This should return a 202 (Accepted). If not, please check your logs and file a bug if the logs do not lead you to a solution.

With the subscription created and the pubsub sink bound to our HTTP publishing source, we can test the flow by sending the source some ATOM.

Issuing the following command will publish a raw metrics event into the pubsub sink:

    curl -X PUT --upload-file ./atom-entry.xml -H "Content-Type: application/atom+xml" "http://localhost:8080/publish" -v

Your callback URL should have now recieved the JSON content as a push notice. If not, please check your logs and file a bug if the logs do not lead you to a solution.
