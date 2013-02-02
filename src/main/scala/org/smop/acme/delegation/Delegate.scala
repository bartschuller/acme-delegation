package org.smop.acme.delegation

import scala.language.experimental.macros
import scala.language.dynamics
import reflect.macros.Context


// Naming it HasDelegate would be more correct, but this reads better:
// "with Delegate"
trait Delegate[T] extends Dynamic {
  private[this] var _delegate: T = _
  def delegate: T = if (_delegate == null) throw new DelegateNotSetException else _delegate
  def delegate_=(d: T) = _delegate = d
  def applyDynamic(method: String)(args: Any*): Any = macro Delegate.applyDynamicImpl
  def selectDynamic(method: String): Any = macro Delegate.selectDynamicImpl
}

object Delegate {
  def applyDynamicImpl(c: Context)(method: c.Expr[String])(args: c.Expr[Any]*) = {
    import c.universe._
    val Literal(Constant(methodString: String)) = method.tree
    val tree = Apply(Select(Select(c.prefix.tree, newTermName("delegate")), newTermName(methodString)), args.map(_.tree).toList)
    c.Expr(tree)
  }
  def selectDynamicImpl(c: Context)(method: c.Expr[String]) = {
    import c.universe._
    val Literal(Constant(methodString: String)) = method.tree
    val tree = Select(Select(c.prefix.tree, newTermName("delegate")), newTermName(methodString))
    c.Expr(tree)
  }
}

class DelegateNotSetException(msg: String="Delegate not set", cause: Throwable = null) extends RuntimeException(msg, cause)