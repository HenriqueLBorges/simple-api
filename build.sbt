name := "simple-api"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.twitter" %% "twitter-server" % "19.4.0",
  "com.github.finagle" %%  "finch-core" % "0.31.0",
  "com.github.finagle" %% "finch-circe" % "0.31.0",
  "io.circe" %% "circe-parser" % "0.12.3",
  "io.circe"  %% "circe-core"   % "0.12.3",
  "io.circe"  %% "circe-generic" % "0.12.3",
  "org.apache.kafka" %% "kafka" % "2.1.0",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "org.scalatest" %% "scalatest" % "3.1.0",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)

assemblyMergeStrategy in assembly := {
  case x if x.contains("ivy2") => MergeStrategy.first
  case x if x.contains("io.netty") => MergeStrategy.first
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "BUILD" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)

}

mainClass in assembly := Some("Server")
assemblyJarName in assembly := "simple-api.jar"