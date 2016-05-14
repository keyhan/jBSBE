[![Build Status](https://travis-ci.org/keyhan/jBSBE.svg?branch=master)](https://travis-ci.org/keyhan/jBSBE)
# jBSBE
## What is jBSBE
jBSBE is an addon on top of the [j8583](https://github.com/chochos/j8583) which is a java implementation of the [ISO8583](https://en.wikipedia.org/wiki/ISO_8583) implementation, adding some missing functionality and simplifying its usage.

License
----------------
Project uses [Apache License 2.0](LICENSE)

## Why jBSBE
Because j8583 is
- unnecessary cumbersome to setup.
- unnecessary difficult to transform Pojos to ISO Messages.
- Lacks some needed functionality like:
  - Nice printing.
  - Real Binary Fields.

## How to get it
```
<dependency>
  <groupId>com.github.keyhan</groupId>
  <artifactId>jbsbe</artifactId>
  <version>0.0.1-SNAPSHOT</version>
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
First thing you need is the message body itself. The following pojo represents an ISO Message (0x200), its fields are set by help from the template set [Next Step](#creating-iso8583-template).
```java
@Iso8583(type=0x200)
public class PurchaseRequest {
	@IsoField(index=10)
	public Date date;
	@IsoField(index=4)
	public Long amount;
	@IsoField(index=11)
	public Integer stan;
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

### Event driven messaging
The code below is a simple example on how to create an ISO Message from Pojo. Think that same scenario could be triggered by a REST request where purchase request is actually the body of the request. Now you have an ISO Message ready to be sent.
```java
I50Factory factory = new I50Factory();
PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(100L).date(new Date()).stan(123456).build();
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
║ 11       ║ stan       ║ NUMERIC  ║ 6(6)     ║ 123456              ║
╠══════════╬════════════╬══════════╬══════════╬═════════════════════╣
║ 35       ║ cardNumber ║ NUMERIC  ║ 16(16)   ║ xxxx-xxxx-xxxx-4567 ║
╚══════════╩════════════╩══════════╩══════════╩═════════════════════╝
```
