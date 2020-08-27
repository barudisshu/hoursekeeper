import com.typesafe.sbt.packager.docker.Cmd

organization in ThisBuild := "info.galudisu"

name := """hoursekeeper"""

maintainer := "Galudisu <galudisu@gmail.com>"

/* scala versions and options */
scalaVersion := "2.12.8"

// dependencies versions
lazy val log4jVersion           = "2.7"
lazy val scalaLoggingVersion    = "3.7.2"
lazy val chillVersion           = "0.9.5"
lazy val slf4jVersion           = "1.7.25"
lazy val akkaHttpVersion        = "10.0.11"
lazy val akkaVersion            = "2.6.8"
lazy val akkaHttpCorsVersion    = "0.4.3"
lazy val scalaJavaCompatVersion = "0.9.1"
lazy val doobiePgVersion        = "0.9.0"
lazy val scalaCsvVersion        = "1.3.6"

// make version compatible with docker for publishing
ThisBuild / dynverSeparator := "-"

// This work for jdk >= 8u131
javacOptions in Universal := Seq(
  "-J-XX:+UnlockExperimentalVMOptions",
  "-J-XX:+UseCGroupMemoryLimitForHeap",
  "-J-XX:MaxRAMFraction=1",
  "-J-XshowSettings:vm"
)

// These options will be used for *all* versions.
scalacOptions := Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)
resolvers ++= Seq(
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

fork in run := true
Compile / run / fork := true
scalafmtOnCompile := true
scalafmtTestOnCompile := true
scalafmtVersion := "1.2.0"

mainClass in (Compile, run) := Some("info.galudisu.Main")

enablePlugins(JavaAppPackaging, DockerPlugin, AshScriptPlugin)

dockerExposedPorts := Seq(1600, 1601, 1602, 8080)
dockerUpdateLatest := true
version in Docker := "latest"
dockerBaseImage := "openjdk:8u171-jre-alpine"
dockerRepository := Some("galudisu")
daemonUser in Docker := "root"
dockerCommands := dockerCommands.value.flatMap {
  case cmd @ Cmd("FROM", _) => List(cmd, Cmd("RUN", "apk update && apk add bash"))
  case other                => List(other)
}

libraryDependencies ++= {
  Seq(
    "org.scala-lang.modules" %% "scala-java8-compat" % scalaJavaCompatVersion,
    // akka
    "com.typesafe.akka" %% "akka-actor"            % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"            % akkaVersion,
    "com.typesafe.akka" %% "akka-remote"           % akkaVersion,
    "com.typesafe.akka" %% "akka-stream"           % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster"          % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools"    % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
    "com.typesafe.akka" %% "akka-http"             % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json"  % akkaHttpVersion,
    // misc
    "ch.megard"         %% "akka-http-cors" % akkaHttpCorsVersion,
    "com.typesafe.akka" %% "akka-http-xml"  % akkaHttpVersion,
    // log
    "org.apache.logging.log4j"   % "log4j-core"       % log4jVersion,
    "org.apache.logging.log4j"   % "log4j-api"        % log4jVersion,
    "org.apache.logging.log4j"   % "log4j-slf4j-impl" % log4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging"   % scalaLoggingVersion,
    // doobie
    "org.tpolecat" %% "doobie-core"     % doobiePgVersion,
    "org.tpolecat" %% "doobie-postgres" % doobiePgVersion,
    // csv
    "com.github.tototoshi" %% "scala-csv" % scalaCsvVersion,
    // chill
    "com.twitter" %% "chill-akka" % chillVersion,
    // cron4s
    "dev.zio"                       %% "zio"         % "1.0.1",
    "com.github.alonsodomin.cron4s" %% "cron4s-core" % "0.6.1",
    // test
    "com.typesafe.akka" %% "akka-testkit"        % akkaVersion     % Test,
    "com.typesafe.akka" %% "akka-http-testkit"   % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-testkit"        % akkaVersion     % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion     % Test
  )
}
