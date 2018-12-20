# A-Collections

This is a library of Java collection classes. It works with Java 8 but is compatible with all later Java releases.

Its key features are

* Mutable and immutable classes, both implementing the same interfaces.
* A rich API with methods like `map`, `filter` or `reduce` on the collections themselves. This API provides
   a lot more functionality than even Java's stream API.
* Immutable classes are ['persistent'](https://en.wikipedia.org/wiki/Persistent_data_structure), i.e. 
   methods to add or remove elements return a modified copy.
* Mutable classes are write-through wrappers around standard Java collections, adding API while maintaining 
   existing classes' nonfunctional properties. This also allows using a-collections 'ad hoc' by wrapping
   them where needed. 
* All collection classes extend `java.util.Collection` and the other standard library interfaces so they work
   well with existing APIs. This also means that all ACollections support Java's stream API.
* Full JSON support: a-collections comes with a [Jackson](https://github.com/FasterXML/jackson) module.

Many of a-collection's algorithms are ported from Scala's standard libraries, so credits and a note of thanks
to the Scala team for their excellent work and support!

## Getting Started

The library is available from Maven Central at the following coordinates:

```
<dependency>
    <groupId>com.ajjpj.a-collections</groupId>
    <artifactId>a-collections</artifactId>
    <version>0.9</version>
</dependency>
```

or in Gradle notation:

```
compile "com.ajjpj.a-collections:a-collections:0.9"
```

### Creating a collection

No better way to explain a library than with a sample of running code:

```$java
AList<String> l = AList             // (1)
    .of(1, 2, 3)                    // (2)
    .filter(n -> n%2==1)            // (3)
    .map(n -> String.valueOf(2*n)); // (4)
```

Let's walk through this sample step by step. 

(1) `AList` is an interface from a-collections representing a list of elements. 
    It extends `java.util.List`, and this is what we start with.
    
(2) `AList` has static factory methods for creating instances. `of` creates an
    immutable instance of `AList` containing the numbers 1, 2 and 3.
    
(3) This line uses `AList`'s `filter` method to retain only odd numbers, i.e. 
    keeping 1 and 3 while removing 2.

(4) `map` transforms each remaining element, creating an `AList` with a string
    representation of each number's doubled value. 

The result of this pipeline is an immutable `AList<String>` which can be used 
wherever a `java.util.List<String>` is required (because `AList` extends 
`java.util.List`).

### Persistent Collections

Let's assume we want to add elements to the list of numbers from
the previous section. It is immutable, so we can not actually modify
it, but we can still add elements:

```
AList<String> l2 = l
    .prepend("0")        // (1)
    .append("10");       // (2)
assert(l.size() == 2);
assert(l2.size() == 4);
l2.add("99");            // (3) throws an UnsupportedOperationException      
```

Line (1) calls `prepend` on the collection. That returns a new list with an 
additional element at the beginning while leaving the original alone.

Line (2) appends an element to the list's end, again returning the new list
and leaving the original unmodified. That leaves us with two lists: `l` with 
exactly the same elements it had when we started, and `l2` with the added
elements.

Calling `add` (inherited from `java.util.Collection`) on this collection (3)
throws an `UnsupportedOperationException` because the collection is immutable.

### Wrapping mutable collections

Another good way to get started is to wrap an existing plain old Java collection:

```
Set<String> sOld = ...; // from some existing API   (1)
ASet<String> s = ASet.wrap(sOld);                   (2)
int numWithA = s.count(x -> x.startsWith("A"));     (3)
s.remove("xyz");                                    (4)
assert(s.getInner() == sOld);                       (5)
```
    
This example begins with an existing `java.util.Set` (1), e.g. provided by
a third party API. 

Line (2) wraps this set, returning a _mutable_ `ASet` instance without incurring 
any copying whatsover. We can still invoke `ASet` API methods on it, e.g. counting
the number of entries starting with `"A"` (3).

Calling `remove` (inherited from `java.util.Collection`) modifies the underlying
collection and removes the element "xyz" (4).

If for whatever reason we want to retrieve the wrapped collection, we can do that
by calling `getInner()` (5). This returns the exact same collection object we
originally wrapped, with all modifications we did applied to it.

So wrapping regular collections is a low-overhead, low-risk way to start using
a-collections where it provides benefit without needing to spread its use to
other places.

## Systematic overview

A-collections consists of _collection_ and _map_ classes as well as some utilities.
There are both mutable and immutable collection and map classes.

### Collections

The interface `ACollection` is the root of all collection classes. The interfaces
`AList` and `ASet` extend it, and all collection classes are either `AList` or `ASet`.
`ASortedSet` is a specialization of `ASet`. These interfaces define the collections'
rich API - for details and explanations see their javadoc.

Each of the interfaces is closely associated with an interface from `java.util` which
it also extends; the following table shows this correspondence:

|  a-collections interface | java.util interface           |
|--------------------------|-------------------------------|
| ACollection              | java.util.Collection          | 
| AList                    | java.util.List                | 
| ASet                     | java.util.Set                 | 
| ASortedSet               | java.util.NavigableSet        | 

For each of these interfaces there are both mutable and immutable implementations, comprehensively
listed in the following table.

| collection class | mutable | interface | description |
|------------------|---------|-----------|-------------|
| AVector | immutable | AList | Optimized for efficient modification and random access. It is backed by a bit-mapped vector trie with a branching factor of 32 and based on Scala's Vector class.| 
| ALinkedList |immutable | AList | Implementation of a linked list. It is a somewhat specialized data structure that is mainly useful for functional programming algorithms based on recursion. Prepending or removing the first element are extremely cheap operations.|
| ARange | immutable | AList | Represents a fixed interval of numbers; it stores only beginning and end of the range and a step width, so it has constant cost regardless of range size.|
| AHashSet | immutable | ASet | Implementation based on a 32-way hash trie. It is optimized for efficient updates - modifications require copying only a small fraction of the total data. The implementation is based on Scala's HashSet class with some optimizations from the [Dexx](https://github.com/andrewoma/dexx) library. |
| ATreeSet | immutable | ASortedSet | Uses an immutable Red/Black tree internally. It is based on Scala's TreeSet class.|
| AMutableListWrapper | mutable | AList | Wraps any `java.util.List`, using a `java.util.ArrayList` by default.|
| MutableArrayWrapper | mutable | AList | Wraps any raw array. It provides a powerful way of working with raw arrays if they are required, e.g. by existing APIs.|
| AMutableSetWrapper | mutable | ASet | Wraps any `java.util.Set`, using a `java.util.HashSet` by default.| 
| AMutableSortedSetWrapper | mutable | ASortedSet | Wraps any `java.util.NavigableSet`, using a `java.util.TreeSet` by default.| 

### Maps

The interface `AMap` is the root of all map classes. There is a sub-interface `ASortedMap` which 
extends `java.util.NavigableMap` and represents maps sorted by their key. For the API provided
by these interfaces, see their respective javadoc.

There are mutable and immutable implementations for these interfaces, comprehensively listed
in the following table.

| collection class | mutable | interface | description |
|------------------|---------|-----------|-------------|
| AHashMap | immutable | AMap | Implementation based on a 32-way has trie. It is optimized for efficient updates - modifications require copying only a small fraction of the total data. The implementation is based on Scala's HashMap class with some optimizations from the [Dexx](https://github.com/andrewoma/dexx) library. | 
| ATreeMap | immutable | ASortedMap | Uses an immutable Red/Black tree internally. It is based on Scala's TreeMap class.|
| AMutableMapWrapper | mutable | AMap | Wraps any `java.util.Map`, using a `java.util.HashMap` by default.| 
| AMutableSortedMapWrapper | mutable | ASortedMap | Wraps any `java.util.NavigableMap`, using a `java.util.TreeMap` by default.| 

#### Maps as collections

In addition to its core 'map' functionality, an `AMap` is also a collection of `Map.Entry`. It can not
actually extend `ACollection` for technical reasons, but the methods are there.

Coming from a Java background, this may appear strange at first sight. But if you think about it, this
is actually a natural way of looking at maps. Why shouldn't they have an `iterator()` method for their
entries, of a `filter()` method to filter entries? Well, in a-collections they do.

Conversely, every `ACollection` has a method `toMap()` which converts a collection of `Map.Entry` into
a map with those entries. 

### AIterator

In a-collections, all iterators are `AIterator` instances which have a rich API for working with data
along the lines of what Java streams offer.

### AOption

A-collections uses its own class `AOption` to represent optional values. There are two reasons for
doing this rather than using `java.util.Optional`:

* `java.util.Optional`'s javadoc is somewhat vague about this but seems to restrict the intended use
  for Optional significantly.

* `AOption` provides a far richer API than `java.util.Optional`, allowing it to seamlessly integrate 
  with the rest of a-collections. `AOption` implements `Iterable` for example.
  
`AOption` has methods to allow simple conversion from and to `Optional` to minimize friction.

### Checked Exceptions

Much of a-collection's uses functional interfaces, most of which do not declare any checked exceptions.
This creates a compile time error if e.g. the transformation function in a call to `ACollection.map()` 
can throw a checked exception:

```
AList<File> files = ...;
files = files.map(f -> f.getCanonicalFile()); // an throw IOException --> compile time error
```  

There are basically three ways of dealing with this: 

* Handle all checked exceptions locally. Forcing clients to _always_ do this is a bad idea,
  some exceptions can only be handled in a meaningful way further up the call stack.
* Wrap checked exceptions in a `RuntimeException`. Some existing libraries do this, but it 
  can complicate error handling significantly.
* Throw checked exceptions without wrapping or declaring them. While this goes against 
  Java's spirit (checked exceptions are supposed to be declared) it is technically possible 
  to do and can be the cleanest solution.
  
In order to give users the choice, a-collections contains a class `AUnchecker` that can 
throw checked exceptions without wrapping or declaring them.  

### Jackson integration

The library contains `ACollectionsModule` which is a [Jackson](https://github.com/FasterXML/jackson) 
module to provide full support for writing a-collections to and reading them from JSON.

To enable this integration, register the module on an ObjectMapper:

```
ObjectMapper om = ...;
om.registerModule(new ACollectionsModule());
```

For details, see `ACollectionsModule` javadoc. 

## Compared to other collection libraries

A-Collections is neither the first nor the only library to provide collection support
beyond standard `java.util.Collection`. Comparing it to alternatives can help 
understand their respective strengths and weaknesses.

### Java streams

Java streams provide some (though not all) of the convenience API that a-collections
brings, and streams are definitely an improvement over Java's naked `java.util.Collection`
API.

There are however two caveats when working with collections: 

* There is often the initial call to `stream()` and a final call to `collect()`, 
  adding clutter and reducing readability.
* Streams provide no immutable collections.    

That said, streams are an important part of Java's collection landscape. 
Therefore `ACollection` classes implement `stream()` and provide `Collector`
implementations.  

Regarding efficiency (which is often named as a key advantage of Java streams): 
Copy operations for immutable collections incur some cost, but a-collection's 
classes are pretty well optimized and the overhead is often outweighed by 
improved readability and robustness. 

### Guava

Google's [Guava](https://github.com/google/guava) library comes with a wide range
of collection classes, some of them immutable. The library is mature, widely used
and well tested.

It focuses on providing a wide range of different collection classes rather than 
a wide API to add convenience when using them. And Guava's immutable collections
are completely immutable rather than 'persistent' so that adding an element 
requires copying the entire collection.

Guava's strength is in providing a wide range of highly memory efficient collections 
while a-collections focuses on convenience working with collections, both mutable
and immutable. 

### Dexx

The [Dexx](https://github.com/andrewoma/dexx) library is a port of large parts of
Scala's standard library to Java and does an excellent job. 

But other considerations aside, it does not integrate with Java's ecosystem (and does
not appear to even try):

* Collection classes do not extend `java.util` interfaces, making them hard to 
  use with third party APIs
* There is no stream support
* There appears to be no Jackson integration
