# ACME Delegation

A bit of syntactic sugar for delegation in Scala.

The combination of the `Dynamic` type and macros bring you a simple trait `Delegate[T]`
that when added to you class allows you to call methods that are only available on
the delegate which you provide:

```scala
trait Helper { def help: String }
class Friend(message: String = "any time") extends Helper {
  override def help = message
}
class Person

val person = new Person with Delegate[Helper]
person.delegate = new Friend

person.help === "any time"
// rewritten at compile time as
person.delegate.help === "any time"
```

## Useless

The methods that are delegated are not actually added to the class.
That's why it's pretty useless for serious work: once you start
varying where in the chain you implement methods, you find that every configuration
needs its own types to be specified exactly.

Writing this was a day well spent: I've learned something.

All tests pass. I encourage everyone to try and edit them and see how useless this is.

[ACME](http://search.cpan.org/search?query=Acme&mode=module) naming inspired by the
Perl community.


Bart Schuller, Februari 2013
