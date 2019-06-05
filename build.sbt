name := "SchachMSC"

version := "1.0"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.19"

val akkaHttp = "10.1.8"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.0.0" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"

libraryDependencies +="com.google.inject" % "guice" % "4.1.0"

libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.5"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.3"

libraryDependencies += "com.h2database" % "h2" % "1.4.192"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"                     % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core"                 % akkaHttp,
  "com.typesafe.akka" %% "akka-http"                      % akkaHttp,
  "com.typesafe.akka" %% "akka-http-spray-json"           % akkaHttp,
  "com.typesafe.play" %% "play-ws-standalone-json"        % "1.1.8"),
  

