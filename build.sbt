name := "desafiohu-felipe-almeida"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  specs2 % Test
)

libraryDependencies += "com.typesafe.play" %% "play-slick" % "1.0.1"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "1.0.1"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1203-jdbc42"

herokuJdkVersion in Compile := "1.8"

herokuAppName in Compile := Map(
  "test" -> "desafiohu-felipe-almeida-test",
  "stg"  -> "desafiohu-felipe-almeida-stage",
  "prod" -> "desafiohu-felipe-almeida-prod"
).getOrElse(sys.props("appEnv"), "sheltered-citadel-3631")

herokuConfigVars in Compile := Map(
  "JAVA_OPTS" -> "-Dconfig.resource=prod.conf -Xmx384m -Xss512k -XX:+UseCompressedOops"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
