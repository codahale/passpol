# Passpol

[![Build Status](https://secure.travis-ci.org/codahale/passpol.svg)](http://travis-ci.org/codahale/passpol)

A Java library for validating passwords against [NIST SP-800-63B](https://pages.nist.gov/800-63-3/)
requirements.

## Add to your project

```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>passpol</artifactId>
  <version>0.5.1</version>
</dependency>
```

## Use the thing

```java
import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;

class Example {
  void doIt() {
    final PasswordPolicy policy = new PasswordPolicy(8, 60, BreachDatabase.haveIBeenPwned());
    
    // validate good passwords
    System.out.println(policy.check("this is a good, long password")); 
    
    // validate bad passwords
    System.out.println(policy.check("password"));
    
    // convert a unicode password to a normalized byte array suitable for hashing
    final byte[] bytes = PasswordPolicy.normalize("✊🏻 unicode 🔥 password");
  } 
}
```

## How it works

`PasswordPolicy` uses a list of 100,000 weak passwords from [Carey Li's
NBP](https://cry.github.io/nbp/) project. Passwords are checked for minimum length, maximum length,
and weakness. In addition, passwords can also be checked against databases of passwords recovered
from major breaches. passpol provides support for checking passwords against [Have I Been
Pwned?](https://haveibeenpwned.com)'s collection of breached passwords.

`PasswordPolicy` also provides the means to normalize Unicode passwords into a canonical byte array
representation suitable for inputting into a password hashing algorithm like `bcrypt`.

## License

Copyright © 2017-2018 Coda Hale

Distributed under the Apache License 2.0.
