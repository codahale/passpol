# Passpol

[![Build Status](https://secure.travis-ci.org/codahale/passpol.svg)](http://travis-ci.org/codahale/passpol)

A Java library for validating passwords against [NIST SP-800-63B](https://pages.nist.gov/800-63-3/)
requirements.

## Add to your project

```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>passpol</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Use the thing

```java
import com.codahale.passpol.PasswordPolicy;
import java.io.IOException;
import java.util.Arrays;

class Example {
  void doIt() throws IOException {
    final PasswordPolicy policy = new PasswordPolicy();
    System.out.println(policy.test("this is a good, long password")); 
    System.out.println(policy.test("1234567890"));
  } 
}
```

## How it works

`PasswordPolicy` uses a list of 10,000 weak passwords from [Carey Li's
NBP](https://cry.github.io/nbp/) project. Passwords are checked for minimum length, maximum length,
and weakness.

`PasswordPolicy` also provides the means to normalize Unicode passwords into a canonical byte array
representation suitable for inputting into a password hashing algorithm like `bcrypt`.

## License

Copyright © 2017 Coda Hale

Distributed under the Apache License 2.0.
