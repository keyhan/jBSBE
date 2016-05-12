[![Build Status](https://travis-ci.org/keyhan/jBSBE.svg?branch=master)](https://travis-ci.org/keyhan/jBSBE)
# jBSBE
## What is jBSBE
jBSBE is really a flavor of the [j8583](https://github.com/chochos/j8583), adding some missing functionality and simplifying its usage. 

## Features
- Annotation based Message Transformation (Introducing @Iso858s and @IsoField)
- Beautiful toString for the ISO Message
- Simplifying setting Fields for ISO Message by Setting up Templates in I50Factory.
  - I50Factory.addField(int index, String name, I50Type isoType, int length);
- Supporting Mixture of binary and non binary fields in message (Introducing I50Binary and I50LLLBin Types)
