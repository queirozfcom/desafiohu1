name := "desafiohu-felipe-almeida"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

herokuJdkVersion in Compile := "1.8"

herokuAppName in Compile := Map(
  "test" -> "desafiohu-felipe-almeida-test",
  "stg"  -> "desafiohu-felipe-almeida-stage",
  "prod" -> "desafiohu-felipe-almeida-prod"
).getOrElse(sys.props("appEnv"), "sheltered-citadel-3631")

herokuConfigVars in Compile := Map(
  "JAVA_OPTS" -> "-Dfoobar=$APP_CONFIG -Xmx384m -Xss512k -XX:+UseCompressedOops"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
