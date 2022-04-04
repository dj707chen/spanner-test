package io.chrisdavenport.spannertest

import cats.effect._
import cats.effect.std._
import doobie._
import doobie.implicits._

import java.sql.DriverManager
import scala.concurrent.ExecutionContext
import java.nio.charset.StandardCharsets

/* Accessing Spanner in cloud from local is not working, might need to use service accounts.
Please use MainEmulator to test. Below is the error.
----------------------------------------------------------------------------------------------
➜  ~/repoMy/spanner-test git:(main) ✗ sbt core/run
[info] welcome to sbt 1.6.1 (Eclipse Adoptium Java 17.0.1)
[info] loading global plugins from /Users/dj.chen/.sbt/1.0/plugins
[info] loading settings for project spanner-test-build-build-build from metals.sbt ...
[info] loading project definition from /Users/dj.chen/repoMy/spanner-test/project/project/project
[info] loading settings for project spanner-test-build-build from metals.sbt ...
[info] loading project definition from /Users/dj.chen/repoMy/spanner-test/project/project
[success] Generated .bloop/spanner-test-build-build.json
[success] Total time: 0 s, completed Apr 1, 2022, 11:08:21 PM
[info] loading settings for project spanner-test-build from metals.sbt,plugins.sbt ...
[info] loading project definition from /Users/dj.chen/repoMy/spanner-test/project
[success] Generated .bloop/spanner-test-build.json
[success] Total time: 1 s, completed Apr 1, 2022, 11:08:23 PM
[info] loading settings for project spaner-test from build.sbt ...
[info] set current project to spaner-test (in build file:/Users/dj.chen/repoMy/spanner-test/)
[info] compiling 2 Scala sources to /Users/dj.chen/repoMy/spanner-test/core/target/scala-2.13/classes ...

Multiple main classes detected. Select one to run:
 [1] io.chrisdavenport.spannertest.MainCloud
 [2] io.chrisdavenport.spannertest.MainEmulator

Enter number: 1
[info] running io.chrisdavenport.spannertest.MainCloud


[Cloud] Please choose test action:
  1. Schema creation
  2. Insert
  _. Select
=> 1

Test select
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
2022-04-01 23:08:35.748-0500  warn [DefaultCredentialsProvider] Your application has authenticated using end user credentials from Google Cloud SDK. We recommend that most server applications use service accounts instead. If your application continues to use end user credentials from Cloud SDK, you might receive a "quota exceeded" or "API not enabled" error. For more information about service accounts, see https://cloud.google.com/docs/authentication/.

com.google.cloud.spanner.jdbc.JdbcSqlExceptionFactory$JdbcSqlTimeoutException: DEADLINE_EXCEEDED: Operation did not complete in the given time
	at com.google.cloud.spanner.jdbc.JdbcSqlExceptionFactory.of(JdbcSqlExceptionFactory.java:204)
	at com.google.cloud.spanner.jdbc.AbstractJdbcConnection.getParser(AbstractJdbcConnection.java:93)
	at com.google.cloud.spanner.jdbc.AbstractJdbcStatement.<init>(AbstractJdbcStatement.java:48)
	at com.google.cloud.spanner.jdbc.JdbcStatement.<init>(JdbcStatement.java:52)
	at com.google.cloud.spanner.jdbc.AbstractJdbcPreparedStatement.<init>(AbstractJdbcPreparedStatement.java:48)
	at com.google.cloud.spanner.jdbc.JdbcPreparedStatement.<init>(JdbcPreparedStatement.java:41)
	at com.google.cloud.spanner.jdbc.JdbcConnection.prepareStatement(JdbcConnection.java:68)
	at com.google.cloud.spanner.jdbc.JdbcConnection.prepareStatement(JdbcConnection.java:43)
	at com.zaxxer.hikari.pool.ProxyConnection.prepareStatement(ProxyConnection.java:337)
	at com.zaxxer.hikari.pool.HikariProxyConnection.prepareStatement(HikariProxyConnection.java)
	at doobie.free.KleisliInterpreter$ConnectionInterpreter.$anonfun$prepareStatement$1(kleisliinterpreter.scala:721)
	at doobie.free.KleisliInterpreter.$anonfun$primitive$2(kleisliinterpreter.scala:113)
	at print @ io.chrisdavenport.spannertest.MainCloud$.printActions(MainCloud.scala:15)
	at flatMap @ doobie.WeakAsync$$anon$1.flatMap(WeakAsync.scala:28)
	at $anonfun$tailRecM$1 @ doobie.util.transactor$Transactor$$anon$4.$anonfun$apply$4(transactor.scala:166)
	at flatMap @ doobie.WeakAsync$$anon$1.flatMap(WeakAsync.scala:28)
	at $anonfun$tailRecM$1 @ doobie.util.transactor$Transactor$$anon$4.$anonfun$apply$4(transactor.scala:166)
	at uncancelable @ doobie.WeakAsync$$anon$1.uncancelable(WeakAsync.scala:36)
	at flatMap @ doobie.WeakAsync$$anon$1.flatMap(WeakAsync.scala:28)
	at $anonfun$tailRecM$1 @ doobie.util.transactor$Transactor$$anon$4.$anonfun$apply$4(transactor.scala:166)
	at flatMap @ doobie.WeakAsync$$anon$1.flatMap(WeakAsync.scala:28)
	at $anonfun$tailRecM$1 @ doobie.util.transactor$Transactor$$anon$4.$anonfun$apply$4(transactor.scala:166)
	at flatMap @ doobie.WeakAsync$$anon$1.flatMap(WeakAsync.scala:28)
	at $anonfun$tailRecM$1 @ doobie.util.transactor$Transactor$$anon$4.$anonfun$apply$4(transactor.scala:166)
	at flatMap @ doobie.WeakAsync$$anon$1.flatMap(WeakAsync.scala:28)
	at $anonfun$tailRecM$1 @ doobie.util.transactor$Transactor$$anon$4.$anonfun$apply$4(transactor.scala:166)
	at uncancelable @ doobie.WeakAsync$$anon$1.uncancelable(WeakAsync.scala:36)
	at flatMap @ doobie.WeakAsync$$anon$1.flatMap(WeakAsync.scala:28)
Caused by: com.google.cloud.spanner.SpannerException: DEADLINE_EXCEEDED: Operation did not complete in the given time
	at com.google.cloud.spanner.SpannerExceptionFactory.newSpannerExceptionPreformatted(SpannerExceptionFactory.java:284)
	at com.google.cloud.spanner.SpannerExceptionFactory.newSpannerException(SpannerExceptionFactory.java:61)
	at com.google.cloud.spanner.SpannerExceptionFactory.propagateTimeout(SpannerExceptionFactory.java:87)
	at com.google.cloud.spanner.SessionPool.getDialect(SessionPool.java:1944)
	at com.google.cloud.spanner.DatabaseClientImpl.getDialect(DatabaseClientImpl.java:58)
	at com.google.cloud.spanner.connection.ConnectionImpl.getDialect(ConnectionImpl.java:332)
	at com.google.cloud.spanner.jdbc.AbstractJdbcConnection.getParser(AbstractJdbcConnection.java:91)
	at com.google.cloud.spanner.jdbc.AbstractJdbcStatement.<init>(AbstractJdbcStatement.java:48)
	at com.google.cloud.spanner.jdbc.JdbcStatement.<init>(JdbcStatement.java:52)
	at com.google.cloud.spanner.jdbc.AbstractJdbcPreparedStatement.<init>(AbstractJdbcPreparedStatement.java:48)
	at com.google.cloud.spanner.jdbc.JdbcPreparedStatement.<init>(JdbcPreparedStatement.java:41)
	at com.google.cloud.spanner.jdbc.JdbcConnection.prepareStatement(JdbcConnection.java:68)
	at com.google.cloud.spanner.jdbc.JdbcConnection.prepareStatement(JdbcConnection.java:43)
	at com.zaxxer.hikari.pool.ProxyConnection.prepareStatement(ProxyConnection.java:337)
	at com.zaxxer.hikari.pool.HikariProxyConnection.prepareStatement(HikariProxyConnection.java)
	at doobie.free.KleisliInterpreter$ConnectionInterpreter.$anonfun$prepareStatement$1(kleisliinterpreter.scala:721)
	at doobie.free.KleisliInterpreter.$anonfun$primitive$2(kleisliinterpreter.scala:113)
	at cats.effect.unsafe.WorkerThread.blockOn(WorkerThread.scala:573)
	at scala.concurrent.package$.blocking(package.scala:124)
	at cats.effect.IOFiber.runLoop(IOFiber.scala:949)
	at cats.effect.IOFiber.asyncContinueSuccessfulR(IOFiber.scala:1300)
	at cats.effect.IOFiber.run(IOFiber.scala:125)
	at cats.effect.unsafe.WorkerThread.run(WorkerThread.scala:505)
Caused by: java.util.concurrent.TimeoutException: Waited 60 seconds (plus 2451271 nanoseconds delay) for SettableFuture@6c3ea968[status=PENDING]
	at com.google.common.util.concurrent.AbstractFuture.get(AbstractFuture.java:527)
	at com.google.common.util.concurrent.AbstractFuture$TrustedFuture.get(AbstractFuture.java:121)
	at com.google.cloud.spanner.SessionPool.getDialect(SessionPool.java:1938)
	... 19 more
*/
object MainCloud extends IOApp
 {

  def printActions(implicit console: Console[IO]): IO[Unit] =
    // Change console text color, https://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html#8-colors
    console.print(s"""
      |  \u001b[31m Accessing Spanner in cloud from local is not working, refer the comment in MainCloud.scala,
      |Please use MainEmulator.
      |\u001b[37m
      |""".stripMargin)

  def run(args: List[String]): IO[ExitCode] = {
    import doobie.util.yolo._

    val spannerJdbcUrl = ConfigurationCloud.spannerJdbcUrl

    implicit val console = Console[IO]

    val resultIO = for {
      result      <- printActions
      /*
      action <- console.readLineWithCharset(StandardCharsets.UTF_8)
      _      <- console.println("")
      result <- action match {
                  case "1" =>
                    console.println("Test schema creation") *>
                    JDBCStuff.createJDBC(spannerJdbcUrl)

                  case "2" =>
                    console.println("Test insert") *>
                    DoobieStuff.transactor(spannerJdbcUrl).use { xa =>
                      val yolo = new Yolo(xa); import yolo._
                      DoobieStuff.insert
                        .run(DoobieStuff.Singer(10, "Marc", "Richards", 104100))
                        .transact(xa)
                    }

                  case _ =>
                    console.println("Test select") *>
                    DoobieStuff.transactor(spannerJdbcUrl).use { xa =>
                      val yolo = new Yolo(xa); import yolo._
                      DoobieStuff.select
                        .to[List]
                        .transact(xa)
                        .flatTap(a => IO.println(a))
                    }
                }
      */
    } yield result

     console.println("") *>
     resultIO *>
     console.println("").as(ExitCode.Success)

  }

}
