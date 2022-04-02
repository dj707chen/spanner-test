package io.chrisdavenport.spannertest

import cats.effect._
import cats.effect.std._
import doobie._
import doobie.implicits._

import java.sql.DriverManager
import scala.concurrent.ExecutionContext
import java.nio.charset.StandardCharsets

object MainEmulator extends IOApp {

  def printActions(implicit console: Console[IO]): IO[Unit] =
    console.print(s"""
      |[Spanner Emulator] Please choose test action:
      |  1. Schema creation
      |  2. Insert
      |  _. Select
      |=> """.stripMargin)

  def run(args: List[String]): IO[ExitCode] = {
    import doobie.util.yolo._

    implicit val console = Console[IO]

    val resultIO = for {
      _      <- printActions
      action <- console.readLineWithCharset(StandardCharsets.UTF_8)
      _      <- console.println("")
      result <- action match {
                  case "1" =>
                    console.println("Test schema creation") *>
                    JDBCStuff.createJDBC(ConfigurationEmulator.spannerJdbcUrl)

                  case "2" =>
                    console.println("Test insert") *>
                    DoobieStuff.transactor(ConfigurationEmulator.spannerJdbcUrl).use { xa =>
                      val yolo = new Yolo(xa); import yolo._
                      DoobieStuff.insert
                        .run(DoobieStuff.Singer(10, "Marc", "Richards", 104100))
                        .transact(xa)
                    }

                  case _ =>
                    console.println("Test select") *>
                    DoobieStuff.transactor(ConfigurationEmulator.spannerJdbcUrl).use { xa =>
                      val yolo = new Yolo(xa); import yolo._
                      DoobieStuff.select
                        .to[List]
                        .transact(xa)
                        .flatTap(a => IO.println(a))
                    }
                }
              
    } yield result

     console.println("") *>
     resultIO *>
     console.println("").as(ExitCode.Success)

  }

}
