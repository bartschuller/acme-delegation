import org.smop.acme.delegation.{DelegateNotSetException, Delegate}
import org.specs2.mutable._

class DelegateSpec extends Specification {
  "A simple delegate" should {
    trait Helper { def help: String }
    class Friend(message: String = "any time") extends Helper {
      override def help = message
    }
    class Person
    class SoloPerson extends Person with Helper with Delegate[Helper] { def help = "nope, I got it" }

    "throw when not set" in {
      val person = new Person with Delegate[Helper]
      person.help must throwA[DelegateNotSetException]
    }
    "be called when needed" in {
      val person = new Person with Delegate[Helper]
      person.delegate = new Friend
      person.help === "any time"
    }
    "be switchable" in {
      val person = new Person with Delegate[Helper]
      person.delegate = new Friend
      person.help === "any time"
      person.delegate = new Friend("sure, no problem")
      person.help === "sure, no problem"
    }
    "not be called when not needed" in {
      val person = new SoloPerson
      person.delegate = new Friend
      person.help === "nope, I got it"
    }
    "be chainable" in {
      class Befriended extends Person with Delegate[Delegate[Helper]]
      val person = new Befriended
      val middle = new Person with Delegate[Helper]
      middle.delegate = new Friend
      person.delegate = middle
      person.help === "any time"
    }
    "still not be called when not needed" in {
      class Befriended extends Person with Delegate[Helper with Delegate[Helper]]
      val person = new Befriended
      val middle = new SoloPerson
      middle.delegate = new Friend
      person.delegate = middle
      person.help === "nope, I got it"
    }
  }
}
