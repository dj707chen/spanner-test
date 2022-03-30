import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Scala213 = "2.13.7"

ThisBuild / crossScalaVersions := Seq("2.12.15", Scala213)
ThisBuild / scalaVersion := Scala213

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

val catsV = "2.7.0"
val catsEffectV = "3.3.3"
val fs2V = "3.2.3"
val http4sV = "0.23.7"
val circeV = "0.14.1"
val doobieV = "1.0.0-RC1"
val munitCatsEffectV = "1.0.7"


// Projects
lazy val `spaner-test` = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .aggregate(core)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "spaner-test",

    libraryDependencies ++= Seq(
      "org.typelevel"               %% "cats-core"                  % catsV,
      "org.typelevel"               %% "cats-effect"                % catsEffectV,

      "co.fs2"                      %% "fs2-core"                   % fs2V,
      "co.fs2"                      %% "fs2-io"                     % fs2V,

      "org.http4s"                  %% "http4s-dsl"                 % http4sV,
      "org.http4s"                  %% "http4s-ember-server"        % http4sV,
      "org.http4s"                  %% "http4s-ember-client"        % http4sV,
      "org.http4s"                  %% "http4s-circe"               % http4sV,

      "io.circe"                    %% "circe-core"                 % circeV,
      "io.circe"                    %% "circe-generic"              % circeV,
      "io.circe"                    %% "circe-parser"               % circeV,

      "org.tpolecat"                %% "doobie-core"                % doobieV,
      // "org.tpolecat"                %% "doobie-h2"                  % doobieV,
      "org.tpolecat"                %% "doobie-hikari"              % doobieV,
      // "org.tpolecat"                %% "doobie-postgres"            % doobieV,

      "com.google.cloud" % "google-cloud-spanner-jdbc" % "2.6.3",
      "com.google.cloud" % "google-cloud-spanner" % "6.21.2",

      "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,

    )
  )
