name := "cassandra-guide-scala"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-encoding", "UTF-8", "-language:implicitConversions", "-language:postfixOps")

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.0.0"
)
