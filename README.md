[![Build Status](https://travis-ci.org/keyhan/jBSBE.svg?branch=master)](https://travis-ci.org/keyhan/jBSBE)
[![Join the chat at https://gitter.im/keyhan/jBSBE](https://badges.gitter.im/keyhan/jBSBE.svg)](https://gitter.im/keyhan/jBSBE?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
# jBSBE
## What is jBSBE

jBSBE is a library on top of the [j8583](https://github.com/chochos/j8583), adding some missing functionality and simplifying its usage.  [j8583](https://github.com/chochos/j8583) is a java implementation of the [ISO8583](https://en.wikipedia.org/wiki/ISO_8583) standard.

License
----------------
Project uses [Apache License 2.0](LICENSE)

## Why jBSBE
Because j8583 is
- Could be simpler to configure.
- unnecessary difficult to transform Pojos to ISO Messages. Using it in microservice environment could be a pain.
- Lacks some useful functionality like:
  - Nice printing.
  - Real Binary Fields. Either all fields are binary or none.
  - Masking fields for logging.
  - Metadata in iso message.

## How to get it

### Stable Release
```xml
<dependency>
  <groupId>com.github.keyhan</groupId>
  <artifactId>jbsbe</artifactId>
  <version>0.0.1</version>
</dependency>
```
### Snapshot
```xml
<dependency>
  <groupId>com.github.keyhan</groupId>
  <artifactId>jbsbe</artifactId>
  <version>0.0.2-SNAPSHOT</version>
</dependency>
```

## jBSBE Features
- Based on  [j8583](https://github.com/chochos/j8583), so the classes I50Message and I50Factory extends the IsoMessage and Message factory in the j8583 with all its functionalities.
- Annotation based Message Transformation (Introducing @Iso858s and @IsoField)
- Beautiful toString for the ISO Message
- Simplifying creating templates for ISO Message by Setting up Templates in I50Factory.
  - I50Factory.addField(int index, String name, I50Type isoType, int length);
- Supporting Mixture of binary and non binary fields in message (Introducing I50Binary and I50LLLBin Types)
- Support metadata in the I50Message, come in handy for tracing for example.

Following Sections show how easy you can use the library to setup an ISO Message, ready tp be sent on a socket.

### @nnotation Feature
First thing you need is the message body itself. The @Iso8583 tells us that following pojo represents an ISO Message (0x200), the @AutoStan annotation makes use of the j8583 SimpleTraceGenerator to automatically set the stan. Message fields are set by help from the template set [Next Step](#creating-iso8583-template).
```java
@Iso8583(type=0x200)
@AutoStan
public class PurchaseRequest {
	@IsoField(index=10)
	public Date date;
	@IsoField(index=4)
	public Long amount;
	@IsoField(index=35)
	public String cardNumber;
}
```

### Creating Iso8583 Template
The following code creates template for all types of iso messages used. This code is usually located in initialization part of the application. Notice that for the field 35 below we set the masking rule, this will lead into the value be masked everytime it is logged.
```java
I50Factory.addField(4, "Amount", I50Type.AMOUNT);
I50Factory.addField(10, "Date", I50Type.DATE10);
I50Factory.addField(35, "cardNumber", I50Type.NUMERIC, 16, "xxxx-xxxx-xxxx-####");
I50Factory.addField(11, "stan", I50Type.NUMERIC, 6);

```

### Messaging
The code below is a simple example on how to create an ISO Message from Pojo.

SimpleTransformer class is just a simple transformer as it is said here, copying the value of the field in pojo into the ISO message. If you want another behavior you have to extend the SimpleTransformer and change the behavior of its setField method.

Also Think the scenario below could be triggered by a REST request where purchase request is actually the body of the request. Now you have an ISO Message ready to be sent.

```java

I50Factory<SimpleTransformer> factory = new I50Factory(SimpleTransformer.class);
PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(100L).date(new Date())
	.cardNumber("1234567891234567").build();
I50Message message = factory.newMessage(purchaseRequest);
```

### toString
The following code
```java
System.out.println(message);
```
results in following
```
╔══════════╦══════════╗
║ Field    ║ Value    ║
╠══════════╬══════════╣
║ Message  ║ 200      ║
║ Type     ║          ║
╚══════════╩══════════╝
╔══════════╦════════════╦══════════╦══════════╦═════════════════════╗
║ Field    ║ Name       ║ Type     ║ Length   ║ Value               ║
║ Number   ║            ║          ║          ║                     ║
╠══════════╬════════════╬══════════╬══════════╬═════════════════════╣
║ 4        ║ Amount     ║ AMOUNT   ║ 3(12)    ║ 100                 ║
╠══════════╬════════════╬══════════╬══════════╬═════════════════════╣
║ 10       ║ Date       ║ DATE10   ║ 10(10)   ║ Sat May 14 15:56:10 ║
║          ║            ║          ║          ║ CEST 2016           ║
╠══════════╬════════════╬══════════╬══════════╬═════════════════════╣
║ 11       ║ stan       ║ NUMERIC  ║ 1(6)     ║ 1                   ║
╠══════════╬════════════╬══════════╬══════════╬═════════════════════╣
║ 35       ║ cardNumber ║ NUMERIC  ║ 16(16)   ║ xxxx-xxxx-xxxx-4567 ║
╚══════════╩════════════╩══════════╩══════════╩═════════════════════╝
```
