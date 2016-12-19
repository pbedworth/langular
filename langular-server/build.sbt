name := "langular"

version := "1.0"

scalaVersion := "2.12.1"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies ++= {
  val AkkaVersion       = "2.4.14"
  val AkkaHttpVersion   = "10.0.0"
  Seq(
    "com.typesafe.akka"  %% "akka-slf4j"      % AkkaVersion,
    "com.typesafe.akka"  %% "akka-http"       % AkkaHttpVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback"     % "logback-classic" % "1.1.2",
    "com.typesafe.akka"  %% "akka-http-spray-json" % "10.0.0",
    "com.wix"            %% "accord-core" % "0.6.1",
    "com.github.seratch" %% "awscala" % "0.5.+"
  )
}

mainClass in Global := Some("com.cheekyeye.langular.WebServer")
