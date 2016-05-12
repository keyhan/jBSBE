[![Build Status](https://travis-ci.org/keyhan/jBSBE.svg?branch=master)](https://travis-ci.org/keyhan/jBSBE)
# jBSBE
## What is jBSBE
jBSBE is an addon on top of the [j8583](https://github.com/chochos/j8583) which is a java implementation of the [ISO8583](https://en.wikipedia.org/wiki/ISO_8583) implementation, adding some missing functionality and simplifying its usage.

## Why jBSBE
Because j8583 is
- unnecessary cumbersome to setup.
- unnecessary difficult to transform Pojos to ISO Messages.
- Lacks some needed functionality like:
  - Nice printing.
  - Real Binary Fields.

## jBSBE Features
- Annotation based Message Transformation (Introducing @Iso858s and @IsoField)
- Beautiful toString for the ISO Message
- Simplifying creating templates for ISO Message by Setting up Templates in I50Factory.
  - I50Factory.addField(int index, String name, I50Type isoType, int length);
- Supporting Mixture of binary and non binary fields in message (Introducing I50Binary and I50LLLBin Types)
- Support metadata in the I50Message(hashmap), come in handy for tracing for example.

### Creating Iso8583 Template
The following code creates template for all iso messages used, purchase request in the last line is shown in the [next step](#@nnotation-feature). I50Factory is subclass to j8583 MessageFactory so you can use all its functionality here as well. This code is usually what is added to your business logic.
```java
I50Factory factory = new I50Factory();
I50Factory.addField(4, "Amount", I50Type.AMOUNT);
I50Factory.addField(10, "Date", I50Type.DATE10);
I50Factory.addField(11, "stan", I50Type.NUMERIC, 6);
I50Message message = factory.newMessage(purchaseRequest);
```

### @nnotation Feature
The following pojo is fed in the last line of previous step to create an ISO Message (0x200), its fields are set by help from the template set [previousely](#creating-iso8583-template).
```java
@Iso8583(type=0x200)
public class PurchaseRequest {
	@IsoField(index=10)
	public Date date;
	@IsoField(index=4)
	public Long amount;
	@IsoField(index=11)
	public Integer stan;
}
```
### toString
The following code (message is the ISO Message created by the Annotation example above)
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
╔══════════╦══════════╦══════════╦══════════╦══════════╗
║ Field    ║ Name     ║ Type     ║ Length   ║ Value    ║
║ Number   ║          ║          ║          ║          ║
╠══════════╬══════════╬══════════╬══════════╬══════════╣
║ 4        ║ Amount   ║ AMOUNT   ║ 3(12)    ║ 100      ║
╠══════════╬══════════╬══════════╬══════════╬══════════╣
║ 10       ║ Date     ║ DATE10   ║ 10(10)   ║ Thu May  ║
║          ║          ║          ║          ║ 12       ║
║          ║          ║          ║          ║ 13:14:05 ║
║          ║          ║          ║          ║ UTC 2016 ║
╠══════════╬══════════╬══════════╬══════════╬══════════╣
║ 11       ║ stan     ║ NUMERIC  ║ 6(6)     ║ 123456   ║
╚══════════╩══════════╩══════════╩══════════╩══════════╝
```
