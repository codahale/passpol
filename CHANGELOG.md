# Change Log

## v0.5.0: 2018-02-23

* Changed the HaveIBeenPwned breach database to use their new, _k-anonymous_ backend.
* Improve memory usage and performance slightly.

## v0.4.0: 2017-12-26

* Changed `PasswordPolicy` to return the reason for rejecting a password, if any.
* Made `PasswordPolicy::normalize` static.
* Added support for using breach databases, including Have I Been Pwned?.

## v0.3.0: 2017-11-07

* Increased weak password list to 100,000.

## v0.2.1: 2017-10-28

* Simplified code point counting.

## v0.2.0: 2017-06-26

* Remove Guice dependency.

## v0.1.4: 2017-04-21

* Add optional JSR305 annotations.
* Fix resource path of weak passwords list.

## v0.1.3: 2017-04-20

* Improve password loading.

## v0.1.2: 2017-04-19

* Improve codepoint counting.

## v0.1.1: 2017-04-19

* Count Unicode codepoints, not characters.

## v0.1.0: 2017-04-19

* Initial release.