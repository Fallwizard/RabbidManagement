Rabbid Management
========================

[![Build Status](https://travis-ci.org/Berico-Technologies/RabbidManagement.png)](https://travis-ci.org/Berico-Technologies/RabbidManagement)

Formerly known as RabbitMQ-Management-Java.  This is a utility library for managing and testing the state of a RabbitMQ node/cluster via the RabbitMQ Management Console.  In many cases, it's simply a fluent Java API for the console.  The library also includes a built-in Assertion library for writing integration tests against RabbitMQ, as well as, loader and persistence mechanism for topology configuration.

The library was developed in support of our AMPere project (http://github.com/Berico-Technologies/AMP), but maintains no ties to the project so it can be used independently by other developers or frameworks.

> This library is licensed under the Apache License, Version 2.0.

## Changelog

> Note about versions.  This library was originally keeping in sync with the AMPere project (so it started it's life at 3.1.0.  I'm going to break with tradition an let it version as needed.  Sorry for any confusion.

**v3.8.1**

- Moved all code to io.github.fallwizard.rabbitmq namespace
- First version uploaded to maven central

**v3.8.0**

- Removed the setting for using ssl from `RabbitMmgtService` and made it a contract requirement of `HttpClientProvider`.  Instantiating the client should now make a lot more sense.
- Added autodetection of keystore and truststore types (e.g. PKCS12 or JKS).  If neither can be detected, the framework will default to the default JVM implementation.

**v3.7.1**

- Fixed bug on the Exchange builder where the underlying Map<String, Object> of arguments is null when arg(k, v) is called.

**v3.7.0**

- Added Support for managing Connections.

**v3.6.2**

- Removed Maven Shade plugin.  The intent of the plugin was to provide a portable means of using the library.  I'm moving this out to the RabbidShell project which will use the Scala REPL and perform all of the bundling via SBT.

**v3.6.1**

- Fixed integration tests.

**v3.6.0**

- Adding support for configuring federation under the `rabbitMgmtService.federate()` fluent.
- Added an `AmqpUri` builder to help with creating AMQP URI strings (needed for federation).
- Removed `Optional` from retrieving all exchanges (which was nonsense because you should always have some entries or the HTTP connection fails).
- Added support for Travis CI builds to run Integration Tests.

**v3.5.2**

- Removed "transient" from fields in the `Queue` class which caused GSon to ignore properties coming back from RabbitMQ.  This fixes Github issue #1.

**v3.5.1**

- Fixed bugs in the assertion framework.
- Added Unit Tests.
- Modified POM to generate source and javadoc JARs.

**v3.5.0**

- Added an easier DSL for verifying the delivery of a single message on multiple queues.
- Added overloads on some of the RabbitAssert methods to take Queue objects instead of just a string queue name.  (I intend to go back and add the same for Binding, User, Exchange, etc.

**v3.4.4**

- Added two missing method overloads in the RabbitAssert class that allowed validation of messages using the default vhost.

**v3.4.3**

- Java target now set to 1.6 instead of 1.7.
- Updated Manifest files to reflect the alias change in v3.4.2.

**v3.4.2**

- Added XStream alias so we don't get a funky XML root of `<io.github.fallwizard.rabbitmq.loader.Manifest />`.

**v3.4.1**

- `@Ignore`d the example test case.
- Added Maven Shade to generate Fat JAR.

**v3.4.0**

- Support for Loading and Persisting topology manifests.  In essence, you can save exchanges, queues, etc. from one source and have them loaded into another RabbitMQ instance.  The manifest can be saved as XML, JSON, or YAML.
- Cleaned up model.  There are now parameterized constructors, setters, and builders for the model classes you would instantiate.
- Fixed a bug in which all bindings were executed as Exchange to Queue (Ex to Ex was not working).

**v3.3.0**

- Support for Producing and Consuming using HTTP via the Management Console's pub/sub mechanisms.
- Added `Message`, `ReceivedMessage`, `PublishResponse`, `ConsumeOptions` to support PubSub.
- Revised the `HttpContext` to support the retrieval of objects on HTTP `POST` calls (which is generally the only time outside of a `GET` that you can expect an object back).
- Replaced return types with Google Guava's `Optional<T>` and added more robust error handling (no more GSON parsing exceptions if an entity does exist; i.e. 404 error as HTTP status).  Now you can check to see if you actually have a valid model object instead of incurring an error when it should not occur.  `Optional` is not imposed on methods that should return a response (these are generally the parameterless `GET` methods that will fail only if there is a connection error).
- Support for Integration Testing.  The real reason for the improvements was to allow me to make assertions about RabbitMQ's configuration and the presence of messages.
- Added a builder for `RabbitMgmtService`.

**v3.2.0**

- Bug fixes on some of the models to more accurately reflect the types coming back from the console.

**v3.1.0**

- Initial release including support for most of the RabbitMQ Management Console functions.

**Installation/Configuration**

You can access the library via Maven:

```
<repository>
    <id>nexus.bericotechnologies.com</id>
    <name>Berico Technologies Nexus</name>
    <url>http://nexus.bericotechnologies.com/content/groups/public</url>
</repository>

<dependency>
    <groupId>rabbitmq</groupId>
	<artifactId>mgmt</artifactId>
	<version>3.7.1</version>
</dependency>
```

> I will place it on Maven Central the second the project goes about 25 stars.

##Running##

You can either use the API and progamatically interact with RabbitMQ, or you can edit a manifest file (XML, JSON, and YAML versions included in the `manifests` directory) and load/rollback the topology objects using the `ManifestLoader`:

```bash
# In the root directory of the project.
chmod +x load
# This command uses Maven.  I'll supply a better script in future versions that uses a FatJar.
./load --help
# Load from the specified manifest file
./load manifests/manifest.xml
# Remove all objects defined in the manifest file
./load manifests/manifest.xml --rollback
```


### Examples

There's basic command and control functionality that maps directly to the RabbitMQ Management Console.

Instantiating the Management Service:
```java
RabbitMgmtService mgmt =
  RabbitMgmtService.builder()
    .host(hostname)
    .port(15672)
    .credentials("guest", "guest")
    .build();
```

Working with Exchanges:
```java
// Retrieving all exchanges on the default vhost ("/")
Collection<Exchange> exchanges =  mgmt.exchanges().all();


// Creating exchanges:
// 1.  Instantiate/configure an exchange
// 2.  Use the manager to create the exchange.

// Note: Most objects (at least the ones you would instantiate)
// have overloaded constructors, setters, and companion builders.
// Use the pattern most compatible with your development style.
Exchange ex = new Exchange("berico.tech");

// OR
ex = new Exchange();
ex.setName("berico.tech");
ex.setType("direct");
ex.setAutoDelete(false);
ex.setDurable(true);
ex.setInternal(false);
ex.setVhost("/");

// OR
ex = Exchange.builder().name("berico.tech").direct().build();

// Create the exchange
mgmt.exchanges().create(ex);

// Get the exchange from the server
Optional<Exchange> exBackFromServer = mgmt.exchanges().get("berico.tech");

// Get bindings from the server:

// Downstream = Exchange -> Exchange or Exchange -> Queue
Optional<Collection<Binding>> amqDirectBindings = mgmt.exchanges().downstreamBindings("amq.direct");

// Upstream = Exchange <- Exchange
Optional<Collection<Binding>> exchangeToExchangeBindgings = mgmt.exchanges().upstreamBindings("amq.topic");

// Delete an exchange
mgmt.exchanges().delete(ex.getName());
```
Working with Queues:
```java
Queue q = new Queue();
q.setName("test-queue");
q.setDurable(true);
q.setAutoDelete(false);
q.setVhost("/");

// OR
// Note that "autoDelete" and "vhost" are redundant (these are default values).
q = Queue.builder().name("test-queue").durable().autoDelete(false).vhost("/").build();

// All of the mutation methods are fluent.  Here we create a queue and retrieve that queue from the server.
Queue testQueue = mgmt.queues().create(q).get("test-queue");

// Delete a Queue
mgmt.queues().delete("test-queue");
```

Working with Bindings:
```java
Binding b = new Binding();
b.setSource(ex.getName());
b.setDestination(q.getName());
b.setDestinationType("queue");
b.setRoutingKey("some.generic.key");
b.setVhost("/");

// OR
// "vhost" is redundant and "setExchangeType" is not necessary ("toQueue" sets the correct type).
b = Binding.builder()
  .fromExchange(ex.getName())
  .toQueue(q.getName())
  .routingKey("some.generic.key")
  .vhost("/")
  .build();

// Bind the previous exchange to the queue with the binding above.
// This example demonstrates "and()" which allows you to traverse back to the base fluent interface.
mgmt.exchanges()
      .create(ex)
    .and()
    .queues()
      .create(q)
    .and()
    .bindings()
      .create(b);
```

Manage Users and Permissions:
```java
// Get all users on the default vhost.
Collection<User> users = mgmt.users().all();

// Get the authenticated user account interacting with the console.
User me = mgmt.users().whoAmI();

// Create a user
mgmt.users().create(User.builder().name("bob").password("abc123").build());

// Remove a user
mgmt.users().delete("bob");

// Get permissions on the default vhost
Collection<Permission> allPermissions = mgmt.vhosts().permissions();

// Or get permissions for a particular user (regardless of vhost).
Optional<Collection<Permission>> permissionsForGuest = mgmt.users().permissionsFor("guest");
    		
// Or scope permissions by user and vhost.
Optional<Permission> permissionForGuestOnDefaultVhost = mgmt.permissions().get("/", "guest");
```

You can now even publish and consume test messages with the API now:

```java
mgmt.queues()
        .create(new Queue("q1"))
    .and()
    .exchanges()
        .create(new Exchange("ex1"))
    .and()
    .bindings()
        .create(new Binding("ex1", "q1", "topic1"));

mgmt.exchanges().publish("ex1",
  Message.builder().payload("Hello!").routingKey("topic1").build());

Optional<Collection<ReceivedMessage>> messages =
  mgmt.queues().consume("q1", ConsumeOptions.builder().requeueMessage(false).build());
```

I've also added some helper functionality for testing the state of a RabbitMQ cluster which you can integrate into your integration-tests:

```java
RabbitMgmtService mgmt = RabbitMgmtService.builder().host("rabbit.archnet.mil").build();

RabbitAssert rabbitAssert = new RabbitAssert(mgmt);

rabbitAssert.hasExchange("test.example.exchange", isDirectType());

rabbitAssert.hasQueue("test.example.queue", isNotQDurable());

rabbitAssert.hasBinding("test.example.exchange", "test.example.queue", routingKey("test.topic"), isExToQ());

```

The test framework include support for testing complex message flows in the exchange:

```java
rabbitAssert.verifyDelivery()
  .on("test.example.queue")
  .butNotOn("test.example.queue2")
  .deliver("test.example.exchange",
    Message.builder().routingKey("test.topic").payload("Hello Rabbid Mgmt!").build());
```
