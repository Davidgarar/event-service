error id: file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventServiceImpl.java
file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventServiceImpl.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,8]

error in qdox parser
file content:
```java
offset: 35
uri: file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventServiceImpl.java
text:
```scala
@Override
@Transactional
public E@@vent updateEventPartial(Long id, java.util.Map<String, Object> updates) {
    Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    
    if (updates.containsKey("name")) {
        event.setName((String) updates.get("name"));
    }
    if (updates.containsKey("description")) {
        event.setDescription((String) updates.get("description"));
    }
    if (updates.containsKey("eventDate")) {
        String dateStr = (String) updates.get("eventDate");
        event.setEventDate(java.time.LocalDateTime.parse(dateStr));
    }
    if (updates.containsKey("location")) {
        event.setLocation((String) updates.get("location"));
    }
    if (updates.containsKey("price")) {
        Double newPrice = ((Number) updates.get("price")).doubleValue();
        event.setPrice(newPrice);
    }
    if (updates.containsKey("totalCapacity")) {
        Integer newCapacity = ((Number) updates.get("totalCapacity")).intValue();
        event.setTotalCapacity(newCapacity);
        if (event.getAvailableCapacity() > newCapacity) {
            event.setAvailableCapacity(newCapacity);
        }
    }
    
    return eventRepository.save(event);
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:560)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:691)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:688)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:688)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:940)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1575)
```
#### Short summary: 

QDox parse error in file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventServiceImpl.java