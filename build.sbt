scalaVersion := "2.11.4"

scalaBinaryVersion:= CrossVersion.binaryScalaVersion("2.11.4")

crossVersion := CrossVersion.binary

organization := "ch.epfl.data"

version := "0.1"

publishArtifact in packageDoc := false

addCommandAlias("benchmark", ";cake-benchmark/run;generated/clean ;generated/compile")
