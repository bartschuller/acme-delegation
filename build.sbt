name := "delegation"

organization := "org.smop.acme"

scalaVersion := "2.10.0"

libraryDependencies <++= (scalaVersion)(ver => Seq(
    "org.scala-lang" % "scala-reflect" % ver,
    "org.scala-lang" % "scala-compiler" % ver % "test"
))

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.13" % "test"
)

scalacOptions += "-feature"
