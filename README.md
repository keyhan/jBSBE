# JBSBE
The aim of this library is to make usage of the j8583 library much easier and some new features.

This is Done by:
Using Configuration through simple java code instead of XML classes. The method below is used to configure the fields for iso8583 messages:
  I50Factory.addField(int index, String name, I50Type isoType, int length); 
  
Using Annotation to set the messages. No need for unnecessary transformation.

Adding support for mixage of binary and nonbinary fields.
