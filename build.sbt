name := "FinancialCentre"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= {
  val akkaV             = "2.4.18"
  val akkaHttpV         = "10.0.7"
  val circeVersion      = "0.9.0"


  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,

    // ============== DB block ================
    // Migration for SQL databases
    "org.flywaydb" % "flyway-core" % "4.2.0",
    // Postgres for flyway
    "org.postgresql" % "postgresql" % "9.4.1208",
    // Quill Async + Postgres
    "io.getquill" %% "quill-async-postgres" % "2.3.2",

    // ============== Serialization ================
    // JSON
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    // sugar akka-http-json
    "de.heikoseeberger" %% "akka-http-circe" % "1.19.0",

    //logger
//    "ch.qos.logback" % "logback-classic" % "1.2.3",
//    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  )
}

