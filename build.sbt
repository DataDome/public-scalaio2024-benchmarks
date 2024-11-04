organization := "co.datadome.pub"
name := "scalaio2024-benchmarks"
description := "Benchmarks for the Scala.io 2024 talk"

version := "1.0"

scalaVersion := "3.5.1"

scalacOptions ++= Seq(
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-explain", // Explain type errors in more detail.

  "-language:implicitConversions",
  "-language:higherKinds",

  "-Wsafe-init", // Wrap field accessors to throw an exception on uninitialized access.
  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn when imports are unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:params", // Warn if a value parameter is unused.
  "-Wunused:privates", // Warn if a private member is unused.
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19"  % Test
)

/* Testing configuration  */
Test / parallelExecution := false

/* Makes processes is SBT cancelable without closing SBT */
Global / cancelable := true

enablePlugins(JmhPlugin)
