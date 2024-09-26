val zioVersion                  = "2.0.13"
val scala3_Version              = "3.3.1"
val scala2_13Version            = "2.13.15"
val scala2_12Version            = "2.12.18"
val zioConfigVersion            = "4.0.0-RC16"
val nebulaClientVersion         = "3.6.1"
val logbackVersion              = "1.4.11"
val silencerVersion             = "1.4.2"
val testcontainersNebulaVersion = "0.1.2"

val supportCrossVersionList = Seq(scala3_Version, scala2_13Version, scala2_12Version)

inThisBuild(
  List(
    scalaVersion     := supportCrossVersionList.head,
    homepage         := Some(url("https://github.com/nebula-contrib/zio-nebula")),
    licenses         := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
    organization     := "io.github.jxnu-liguobin",
    organizationName := "梦境迷离",
    developers       := List(
      Developer(
        id = "jxnu-liguobin",
        name = "梦境迷离",
        email = "dreamylost@outlook.com",
        url = url("https://github.com/jxnu-liguobin")
      )
    )
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

val _zioTests = Seq(
  "dev.zio" %% "zio-test-magnolia" % zioVersion,
  "dev.zio" %% "zio-test"          % zioVersion,
  "dev.zio" %% "zio-test-sbt"      % zioVersion
)

lazy val core = project
  .in(file("core"))
  .settings(
    name                     := "zio-nebula",
    crossScalaVersions       := supportCrossVersionList,
    libraryDependencies ++= Seq(
      "com.vesoft"               % "client"                % nebulaClientVersion,
      "dev.zio"                 %% "zio-config-typesafe"   % zioConfigVersion,
      "dev.zio"                 %% "zio-config-magnolia"   % zioConfigVersion,
      "dev.zio"                 %% "zio"                   % zioVersion,
      // see https://github.com/zio/zio-config/issues/1245
      "com.github.ghik"         %% "silencer-lib"          % silencerVersion             % Provided cross CrossVersion.for3Use2_13,
      "ch.qos.logback"           % "logback-classic"       % logbackVersion              % Test,
      "io.github.jxnu-liguobin" %% "testcontainers-nebula" % testcontainersNebulaVersion % Test
    ) ++ _zioTests.map(_ % Test),
    Test / parallelExecution := false,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val examples = project
  .in(file("examples"))
  .settings(
    publish / skip     := true,
    crossScalaVersions := supportCrossVersionList,
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion
    )
  )
  .dependsOn(core)

lazy val `zio-nebula` = project
  .in(file("."))
  .settings(
    crossScalaVersions := Nil,
    publish / skip     := true,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    libraryDependencies ++= _zioTests.map(_ % Test)
  )
  .aggregate(
    core,
    examples
  )
