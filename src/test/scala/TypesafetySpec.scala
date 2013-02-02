import java.io.{StringWriter, PrintWriter}
import org.smop.acme.delegation.Delegate
import org.specs2.mutable._
import tools.nsc.interpreter.IMain
import tools.nsc.Settings

class TypesafetySpec extends Specification {

  "The Delegate macro" should {
    "reject delegation to methods the delegate doesn't have" in {
      val settings = new Settings()
      settings.embeddedDefaults[Delegate.type]
      val writer = new StringWriter()
      val interpreter = new IMain(settings, new PrintWriter(writer))
      val code = """
import org.smop.acme.delegation.Delegate
class Helper {
  def help = "sure"
}
class Person
object Main extends App {
  val player = new Person with Delegate[Helper]
  println(player.help)
  player.shouldNotCompile
}
"""
      interpreter.compileString(code) must beFalse
      writer.toString must contain("value shouldNotCompile is not a member of Helper")
    }
  }
}
