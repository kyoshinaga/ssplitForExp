import AssemblyKeys._

assemblySettings

name := "ssplitForExp"

scalaVersion := "2.11.7"

version := "0.0"

fork in run := true

parallelExecution in Test := false

crossPaths := false

mainClass in assembly := Some("ssplitForExp.main.Main")

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test->default",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
)