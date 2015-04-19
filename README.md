Nuun Kernel [![Build status](https://travis-ci.org/nuun-io/kernel.svg?branch=master)](https://travis-ci.org/nuun-io/kernel) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.nuun/kernel/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.nuun/kernel) [![Coverage Status](https://coveralls.io/repos/io.nuun/kernel/badge.svg?branch=master)](https://coveralls.io/r/io.nuun/kernel?branch=master)
===========

Nuun is a powerful and flexible **inversion control micro-framework** for building enterprise class stack.

Nuun brings a clean **kernel/plugin design** plus a JSR 330 compliant injection mechanism backed by 
Google Guice. It is designed to be compatible with the major injection frameworks (spring, guice for now, 
tapestry, picocontainer, jodd, etc to come ...). Nuun aims to be a future reference for enterprise
application developement.


Nuun makes a clear separation between stack developers and business developers.
Stack developers will define convention and high level technical aspects by creating plugins.
Then business developers will be free from configuration and will be able to focus on business
issues not technical issues.

## Quick start

### Get Nuun Kernel from Maven central

The easiest way to get Nuun using Maven is to add the following dependency to your pom.

```xml
<dependency>
  <groupId>io.nuun</groupId>
  <artifactId>kernel</artifactId>
  <version>???</version>
</dependency>
```

Where `???` is the current version. See [here](https://github.com/nuun-io/kernel/releases) for the last release.

### Build it from source.

Clone the repo then install it with maven.

```bash
git clone https://github.com/nuun-io/kernel.git
cd kernel
mvn clean install
```

## Documentation

See the current [reference documentation](https://github.com/nuun-io/kernel/wiki).

## Want to contribute ?

Contributions are welcome; please refer to our [contributing guidelines](CONTRIBUTING.md).

## Copyright and license

Code is released under [LGPLv3 license](LICENSE).

##Glossary

- IOC : Inversion Of Control
- DI : Dependency Injection
