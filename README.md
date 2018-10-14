# Passpol

[![CircleCI](https://circleci.com/gh/codahale/passpol.svg?style=svg)](https://circleci.com/gh/codahale/passpol)

A Java library for validating passwords against [NIST SP-800-63B](https://pages.nist.gov/800-63-3/)
requirements.

## Add to your project

```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>passpol</artifactId>
  <version>0.6.2</version>
</dependency>
```

```java
module net.example.yours {
  requires com.codahale.passpol;
}
```

## Use the thing

```java
import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;

class Example {
  void doIt() {
    final PasswordPolicy policy = new PasswordPolicy(BreachDatabase.haveIBeenPwned(5), 8, 64);
    
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

`PasswordPolicy` checks passwords for minimum and maximum length (i.e. the number of Unicode
codepoints in the string) and can check a set of breach databases to see if the password has been
made public.

The built-in breach databases include an offline list of 100,000 weak passwords from the [SecList
Project](https://github.com/danielmiessler/SecLists)'s collection of breached passwords.

`PasswordPolicy` also provides the means to normalize Unicode passwords into a canonical byte array
representation suitable for inputting into a password hashing algorithm like `bcrypt`.

## License

Copyright © 2017-2018 Coda Hale

Distributed under the Apache License 2.0.
